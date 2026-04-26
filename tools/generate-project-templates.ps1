$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
$outDir = Join-Path $root "docs\07-模板与表单\templates"
New-Item -ItemType Directory -Force -Path $outDir | Out-Null

function Escape-XmlText {
    param([AllowNull()][string]$Value)
    if ($null -eq $Value) { return "" }
    return [System.Security.SecurityElement]::Escape($Value)
}

function Get-ExcelColumnName {
    param([int]$Index)
    $name = ""
    while ($Index -gt 0) {
        $remainder = ($Index - 1) % 26
        $name = [char](65 + $remainder) + $name
        $Index = [math]::Floor(($Index - 1) / 26)
    }
    return $name
}

function Add-ZipEntry {
    param(
        [System.IO.Compression.ZipArchive]$Zip,
        [string]$Name,
        [string]$Content
    )
    $entry = $Zip.CreateEntry($Name)
    $stream = $entry.Open()
    $writer = New-Object System.IO.StreamWriter($stream, (New-Object System.Text.UTF8Encoding($false)))
    $writer.Write($Content)
    $writer.Dispose()
    $stream.Dispose()
}

function New-WorksheetXml {
    param([object[]]$Rows)
    $maxCols = 1
    foreach ($row in $Rows) {
        if ($row.Count -gt $maxCols) { $maxCols = $row.Count }
    }
    $lastCol = Get-ExcelColumnName $maxCols
    $lastRow = [math]::Max(1, $Rows.Count)
    $sb = New-Object System.Text.StringBuilder
    [void]$sb.Append('<?xml version="1.0" encoding="UTF-8" standalone="yes"?>')
    [void]$sb.Append('<worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">')
    [void]$sb.Append('<sheetViews><sheetView workbookViewId="0"><pane ySplit="1" topLeftCell="A2" activePane="bottomLeft" state="frozen"/></sheetView></sheetViews>')
    [void]$sb.Append('<sheetFormatPr defaultRowHeight="18"/>')
    [void]$sb.Append('<cols>')
    for ($i = 1; $i -le $maxCols; $i++) {
        [void]$sb.Append("<col min=""$i"" max=""$i"" width=""22"" customWidth=""1""/>")
    }
    [void]$sb.Append('</cols><sheetData>')
    for ($r = 0; $r -lt $Rows.Count; $r++) {
        $rowNumber = $r + 1
        [void]$sb.Append("<row r=""$rowNumber"">")
        for ($c = 0; $c -lt $Rows[$r].Count; $c++) {
            $colName = Get-ExcelColumnName ($c + 1)
            $cellRef = "$colName$rowNumber"
            $style = if ($r -eq 0) { ' s="1"' } else { "" }
            $text = Escape-XmlText ([string]$Rows[$r][$c])
            [void]$sb.Append("<c r=""$cellRef"" t=""inlineStr""$style><is><t>$text</t></is></c>")
        }
        [void]$sb.Append('</row>')
    }
    [void]$sb.Append('</sheetData>')
    if ($Rows.Count -gt 1) {
        [void]$sb.Append("<autoFilter ref=""A1:$lastCol$lastRow""/>")
    }
    [void]$sb.Append('</worksheet>')
    return $sb.ToString()
}

function New-Xlsx {
    param(
        [string]$Path,
        [hashtable]$Sheets
    )
    if (Test-Path -LiteralPath $Path) {
        Remove-Item -LiteralPath $Path -Force
    }
    Add-Type -AssemblyName System.IO.Compression
    Add-Type -AssemblyName System.IO.Compression.FileSystem
    $fs = [System.IO.File]::Open($Path, [System.IO.FileMode]::CreateNew)
    $zip = New-Object System.IO.Compression.ZipArchive($fs, [System.IO.Compression.ZipArchiveMode]::Create)
    try {
        $contentTypes = New-Object System.Text.StringBuilder
        [void]$contentTypes.Append('<?xml version="1.0" encoding="UTF-8" standalone="yes"?>')
        [void]$contentTypes.Append('<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">')
        [void]$contentTypes.Append('<Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>')
        [void]$contentTypes.Append('<Default Extension="xml" ContentType="application/xml"/>')
        [void]$contentTypes.Append('<Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>')
        [void]$contentTypes.Append('<Override PartName="/xl/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"/>')
        [void]$contentTypes.Append('<Override PartName="/docProps/core.xml" ContentType="application/vnd.openxmlformats-package.core-properties+xml"/>')
        [void]$contentTypes.Append('<Override PartName="/docProps/app.xml" ContentType="application/vnd.openxmlformats-officedocument.extended-properties+xml"/>')
        for ($i = 1; $i -le $Sheets.Count; $i++) {
            [void]$contentTypes.Append("<Override PartName=""/xl/worksheets/sheet$i.xml"" ContentType=""application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml""/>")
        }
        [void]$contentTypes.Append('</Types>')
        Add-ZipEntry $zip "[Content_Types].xml" $contentTypes.ToString()

        Add-ZipEntry $zip "_rels/.rels" '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships"><Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/><Relationship Id="rId2" Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties" Target="docProps/core.xml"/><Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties" Target="docProps/app.xml"/></Relationships>'

        $workbook = New-Object System.Text.StringBuilder
        [void]$workbook.Append('<?xml version="1.0" encoding="UTF-8" standalone="yes"?>')
        [void]$workbook.Append('<workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"><sheets>')
        $rels = New-Object System.Text.StringBuilder
        [void]$rels.Append('<?xml version="1.0" encoding="UTF-8" standalone="yes"?><Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">')
        $index = 1
        foreach ($sheetName in $Sheets.Keys) {
            $safeName = Escape-XmlText $sheetName
            [void]$workbook.Append("<sheet name=""$safeName"" sheetId=""$index"" r:id=""rId$index""/>")
            [void]$rels.Append("<Relationship Id=""rId$index"" Type=""http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet"" Target=""worksheets/sheet$index.xml""/>")
            Add-ZipEntry $zip "xl/worksheets/sheet$index.xml" (New-WorksheetXml $Sheets[$sheetName])
            $index++
        }
        [void]$workbook.Append('</sheets></workbook>')
        [void]$rels.Append("<Relationship Id=""rId$index"" Type=""http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles"" Target=""styles.xml""/>")
        [void]$rels.Append('</Relationships>')
        Add-ZipEntry $zip "xl/workbook.xml" $workbook.ToString()
        Add-ZipEntry $zip "xl/_rels/workbook.xml.rels" $rels.ToString()

        Add-ZipEntry $zip "xl/styles.xml" '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><styleSheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main"><fonts count="2"><font><sz val="11"/><name val="Calibri"/></font><font><b/><sz val="11"/><name val="Calibri"/></font></fonts><fills count="2"><fill><patternFill patternType="none"/></fill><fill><patternFill patternType="gray125"/></fill></fills><borders count="1"><border><left/><right/><top/><bottom/><diagonal/></border></borders><cellStyleXfs count="1"><xf numFmtId="0" fontId="0" fillId="0" borderId="0"/></cellStyleXfs><cellXfs count="2"><xf numFmtId="0" fontId="0" fillId="0" borderId="0" xfId="0"/><xf numFmtId="0" fontId="1" fillId="0" borderId="0" xfId="0" applyFont="1"/></cellXfs><cellStyles count="1"><cellStyle name="Normal" xfId="0" builtinId="0"/></cellStyles></styleSheet>'
        Add-ZipEntry $zip "docProps/core.xml" '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><cp:coreProperties xmlns:cp="http://schemas.openxmlformats.org/package/2006/metadata/core-properties" xmlns:dc="http://purl.org/dc/elements/1.1/"><dc:title>企业级数据字典管理系统模板</dc:title><dc:creator>Codex</dc:creator></cp:coreProperties>'
        Add-ZipEntry $zip "docProps/app.xml" '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><Properties xmlns="http://schemas.openxmlformats.org/officeDocument/2006/extended-properties"><Application>Codex</Application></Properties>'
    }
    finally {
        $zip.Dispose()
        $fs.Dispose()
    }
}

$sheets = [ordered]@{
    "填写说明" = @(
        @("模板", "用途", "填写责任人", "备注"),
        @("首批纳管范围", "确定首批业务域和系统群", "项目牵头人/企业架构师", "建议 1 到 2 个业务域，5 到 10 个核心系统"),
        @("系统清单", "建立企业系统底账", "系统 Owner", "作为平台系统资产初始数据"),
        @("数据库清单", "明确可采集数据库范围", "DBA/系统 Owner", "先提供只读元数据权限"),
        @("字段清单", "补齐业务定义和敏感属性", "Data Steward", "采集不到的业务语义必须人工维护"),
        @("RACI矩阵", "明确职责边界", "项目牵头人", "R=执行，A=最终负责，C=咨询，I=知会")
    )
    "首批纳管范围" = @(
        @("业务域", "系统名称", "系统编码", "是否首批", "纳管理由", "关键痛点", "负责人", "计划批次", "验收目标"),
        @("", "", "", "是/否", "", "", "", "第1批", "跑通架构评审、责任认领、表级血缘和影响分析")
    )
    "系统清单" = @(
        @("系统名称", "系统编码", "业务域", "系统类型", "生命周期状态", "业务负责人", "技术负责人", "系统Owner", "上游系统", "下游系统", "主要数据库", "主要接口", "是否核心系统", "备注"),
        @("", "", "", "业务系统/数据平台/报表系统/集成平台", "在建/运行/改造/下线", "", "", "", "", "", "", "", "是/否", "")
    )
    "数据库清单" = @(
        @("数据库类型", "版本", "实例地址", "库名", "Schema", "所属系统", "DBA", "是否可采集", "采集方式", "敏感限制", "备注"),
        @("Oracle/MySQL/SQL Server/PostgreSQL/Hive/ClickHouse", "", "", "", "", "", "", "是/否", "直连/导出/API/暂不支持", "", "")
    )
    "表清单" = @(
        @("所属系统", "数据库", "Schema", "表名", "表中文名", "业务说明", "是否核心表", "数据Owner", "Data Steward", "生命周期状态", "来源方式", "备注"),
        @("", "", "", "", "", "", "是/否", "", "", "待确认/已确认/已发布/废弃", "采集/人工导入", "")
    )
    "字段清单" = @(
        @("所属系统", "表名", "字段名", "字段中文名", "数据类型", "长度", "是否主键", "是否必填", "业务定义", "取值范围", "是否关键字段", "是否敏感", "分类分级", "标准映射", "责任人", "备注"),
        @("", "", "", "", "", "", "是/否", "是/否", "", "", "是/否", "是/否", "", "", "", "")
    )
    "接口清单" = @(
        @("接口名称", "接口编码", "提供方系统", "消费方系统", "协议/方式", "调用频率", "数据对象", "字段清单位置", "负责人", "是否核心接口", "变更审批要求", "备注"),
        @("", "", "", "", "API/消息/文件/ESB/手工", "", "", "", "", "是/否", "强制/可选", "")
    )
    "ETL调度清单" = @(
        @("任务名称", "任务编码", "工具/平台", "来源系统/表", "目标系统/表", "调度频率", "脚本/配置位置", "负责人", "是否关键链路", "是否可解析", "备注"),
        @("", "", "DataX/Kettle/Informatica/Airflow/DolphinScheduler/Flink/Spark/自研", "", "", "", "", "", "是/否", "是/否", "")
    )
    "报表清单" = @(
        @("报表名称", "报表编码", "BI平台", "使用部门", "数据集/主题", "核心指标", "刷新频率", "来源说明", "负责人", "是否核心报表", "备注"),
        @("", "", "Power BI/Tableau/FineBI/帆软/Superset/自研", "", "", "", "", "", "", "是/否", "")
    )
    "指标清单" = @(
        @("指标名称", "指标编码", "业务定义", "计算口径", "统计周期", "维度", "度量", "来源报表/数据集", "数据Owner", "状态", "备注"),
        @("", "", "", "", "日/月/季/年/实时", "", "", "", "", "草稿/已发布/修订中/废止", "")
    )
    "数据标准清单" = @(
        @("标准名称", "标准编码", "标准类型", "定义", "数据类型/格式", "长度/精度", "取值范围", "适用范围", "责任部门", "状态", "备注"),
        @("", "", "基础标准/命名标准/编码标准/指标标准/主数据标准/参考数据标准", "", "", "", "", "", "", "草稿/评审中/已发布/废止", "")
    )
    "业务术语清单" = @(
        @("术语名称", "英文名", "缩写", "同义词", "业务定义", "适用范围", "归属部门", "责任人", "状态", "备注"),
        @("", "", "", "", "", "", "", "", "草稿/已发布/废止", "")
    )
    "RACI矩阵" = @(
        @("工作项", "企业架构师", "数据架构师", "数据Owner", "Data Steward", "系统Owner", "DBA", "数据平台负责人", "安全合规", "备注"),
        @("首批范围确认", "A", "C", "C", "I", "C", "I", "I", "I", ""),
        @("系统资产确认", "C", "I", "I", "I", "A/R", "C", "I", "I", ""),
        @("数据标准发布", "C", "C", "A", "R", "I", "I", "I", "C", ""),
        @("模型变更审批", "A", "R", "C", "C", "R", "C", "I", "C", ""),
        @("血缘确认", "I", "C", "C", "R", "C", "I", "A/R", "I", ""),
        @("敏感字段确认", "I", "C", "C", "R", "C", "I", "I", "A", "")
    )
    "元模型字段清单" = @(
        @("对象类别", "对象名称", "属性名称", "数据类型", "是否必填", "是否唯一", "来源方式", "说明", "示例"),
        @("架构对象", "系统", "系统编码", "文本", "是", "是", "人工/CMDB", "系统唯一标识", "CRM"),
        @("数据对象", "字段", "业务定义", "长文本", "是", "否", "人工", "字段业务含义", "客户首次开户日期"),
        @("治理对象", "审批单", "审批状态", "枚举", "是", "否", "流程", "草稿/审批中/已通过/已驳回", "审批中")
    )
    "架构评审检查表" = @(
        @("检查项编号", "检查项", "通过标准", "责任角色", "结果", "问题说明", "整改责任人", "截止日期"),
        @("AR-001", "是否登记系统和业务域", "系统名称、编码、业务域、Owner 完整", "企业架构师", "通过/不通过/不适用", "", "", ""),
        @("AR-002", "是否引用数据标准", "核心字段和指标已引用标准或说明例外", "数据架构师", "通过/不通过/不适用", "", "", ""),
        @("AR-003", "是否完成影响分析", "核心表、接口、报表变更已生成影响清单", "系统Owner", "通过/不通过/不适用", "", "", "")
    )
    "模型变更审批表" = @(
        @("变更单号", "变更类型", "资产对象", "变更前", "变更后", "影响范围", "提交人", "审批人", "审批结论", "备注"),
        @("", "新增/修改/删除/废弃", "系统/表/字段/接口/指标", "", "", "", "", "", "通过/驳回/补充材料", "")
    )
    "血缘确认表" = @(
        @("血缘编号", "上游资产", "下游资产", "关系类型", "生成方式", "确认人", "确认状态", "修正说明", "备注"),
        @("", "", "", "同步/加工/引用/派生/消费", "自动解析/人工维护/导入", "", "待确认/已确认/需修正", "", "")
    )
    "首期验收检查表" = @(
        @("验收项", "验收标准", "证据材料", "责任人", "结果", "问题", "是否阻断上线"),
        @("治理闭环", "能完成系统建设评审、模型变更审批、标准引用、责任追溯", "审批记录、截图、演示脚本", "", "通过/不通过", "", "是/否"),
        @("血缘能力", "核心报表可追溯到上游表和关键字段", "血缘图、影响分析报告", "", "通过/不通过", "", "是/否"),
        @("资产质量", "核心资产具备定义、责任人、标准映射和更新时间", "资产清单、质量看板", "", "通过/不通过", "", "是/否")
    )
}

New-Xlsx -Path (Join-Path $outDir "首批资产盘点模板.xlsx") -Sheets $sheets

$markdownFiles = @{
    "模板使用说明.md" = @"
# 模板使用说明

## 使用顺序

1. 先填写 `首批纳管范围`，明确首批业务域和系统。
2. 再填写系统、数据库、表、字段、接口、ETL、报表、指标清单。
3. 同步填写 `RACI矩阵`，锁定每类事项的最终负责人与执行人。
4. 基于资产盘点结果补齐 `元模型字段清单`。
5. 用 `架构评审检查表`、`模型变更审批表`、`血缘确认表` 和 `首期验收检查表` 跑通治理闭环。

## 填写原则

- 不要求一开始完整，但必须标记负责人、状态和缺口。
- 技术元数据优先自动采集，业务定义、责任人、标准映射必须人工确认。
- 首期验收以治理闭环为主，不以资产数量堆砌为主。
"@
    "前期工作执行清单.md" = @"
# 前期工作执行清单

## 必须完成

- 确认项目牵头人、架构负责人、数据治理负责人和首批系统 Owner。
- 确认首批 1 到 2 个业务域，5 到 10 个核心系统。
- 收集系统、数据库、接口、ETL、报表、指标、标准和术语清单。
- 协调 DBA 提供只读元数据采集权限。
- 协调数据平台团队提供 ETL、调度、BI 元数据导出或访问方式。
- 确认哪些流程必须接入平台评审。
- 确认敏感字段和分类分级规则。
- 建立缺失资产、责任不明、口径冲突、血缘断点、标准缺失的问题台账。

## 工作坊安排

| 工作坊 | 参与人 | 目标产出 |
| --- | --- | --- |
| 资产盘点工作坊 | 系统 Owner、DBA、数据平台、BI 负责人 | 首批资产清单和缺口台账 |
| 元模型确认工作坊 | 架构师、数据架构师、数据治理团队 | 核心对象、属性、关系和状态 |
| 治理流程确认工作坊 | 架构、数据治理、系统团队、安全团队 | 评审、审批、认责、血缘确认流程 |
"@
    "RACI角色责任矩阵.md" = @"
# RACI 角色责任矩阵

| 工作项 | 企业架构师 | 数据架构师 | 数据Owner | Data Steward | 系统Owner | DBA | 数据平台负责人 | 安全合规 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 首批范围确认 | A | C | C | I | C | I | I | I |
| 系统资产确认 | C | I | I | I | A/R | C | I | I |
| 数据标准发布 | C | C | A | R | I | I | I | C |
| 模型变更审批 | A | R | C | C | R | C | I | C |
| 血缘确认 | I | C | C | R | C | I | A/R | I |
| 敏感字段确认 | I | C | C | R | C | I | I | A |

说明：R=执行，A=最终负责，C=咨询，I=知会。
"@
    "元模型字段清单.md" = @"
# 元模型字段清单

| 对象类别 | 对象名称 | 属性名称 | 数据类型 | 是否必填 | 是否唯一 | 来源方式 | 说明 | 示例 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 架构对象 | 系统 | 系统编码 | 文本 | 是 | 是 | 人工/CMDB | 系统唯一标识 | CRM |
| 数据对象 | 字段 | 业务定义 | 长文本 | 是 | 否 | 人工 | 字段业务含义 | 客户首次开户日期 |
| 治理对象 | 审批单 | 审批状态 | 枚举 | 是 | 否 | 流程 | 草稿/审批中/已通过/已驳回 | 审批中 |
"@
    "架构评审检查表.md" = @"
# 架构评审检查表

| 检查项编号 | 检查项 | 通过标准 | 责任角色 | 结果 | 问题说明 | 整改责任人 | 截止日期 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| AR-001 | 是否登记系统和业务域 | 系统名称、编码、业务域、Owner 完整 | 企业架构师 | 通过/不通过/不适用 |  |  |  |
| AR-002 | 是否引用数据标准 | 核心字段和指标已引用标准或说明例外 | 数据架构师 | 通过/不通过/不适用 |  |  |  |
| AR-003 | 是否完成影响分析 | 核心表、接口、报表变更已生成影响清单 | 系统Owner | 通过/不通过/不适用 |  |  |  |
| AR-004 | 是否完成安全分级 | 敏感字段已识别并具备访问控制要求 | 安全合规 | 通过/不通过/不适用 |  |  |  |
"@
    "数据模型变更审批表.md" = @"
# 数据模型变更审批表

| 字段 | 填写说明 |
| --- | --- |
| 变更单号 | 平台自动生成或项目统一编号 |
| 变更类型 | 新增、修改、删除、废弃 |
| 变更对象 | 系统、表、字段、接口、指标 |
| 变更原因 | 说明业务背景和技术原因 |
| 变更前 | 原模型、字段或接口定义 |
| 变更后 | 新模型、字段或接口定义 |
| 影响范围 | 下游系统、ETL、报表、指标、负责人 |
| 审批结论 | 通过、驳回、补充材料 |
"@
    "数据血缘确认表.md" = @"
# 数据血缘确认表

| 血缘编号 | 上游资产 | 下游资产 | 关系类型 | 生成方式 | 确认人 | 确认状态 | 修正说明 |
| --- | --- | --- | --- | --- | --- | --- | --- |
|  |  |  | 同步/加工/引用/派生/消费 | 自动解析/人工维护/导入 |  | 待确认/已确认/需修正 |  |

## 确认规则

- 自动解析血缘必须由资产责任方确认后才能进入影响分析。
- 无法自动解析的关键链路必须人工补录。
- 首期只要求表级和关键字段级，不承诺字段级全覆盖。
"@
    "首期验收检查表.md" = @"
# 首期验收检查表

| 验收项 | 验收标准 | 证据材料 | 责任人 | 结果 | 问题 | 是否阻断上线 |
| --- | --- | --- | --- | --- | --- | --- |
| 治理闭环 | 能完成系统建设评审、模型变更审批、标准引用、责任追溯 | 审批记录、截图、演示脚本 |  | 通过/不通过 |  | 是/否 |
| 血缘能力 | 核心报表可追溯到上游表和关键字段 | 血缘图、影响分析报告 |  | 通过/不通过 |  | 是/否 |
| 资产质量 | 核心资产具备定义、责任人、标准映射和更新时间 | 资产清单、质量看板 |  | 通过/不通过 |  | 是/否 |
| 使用效果 | 架构、数据治理、系统团队能实际使用平台完成工作 | 会议纪要、用户反馈、操作记录 |  | 通过/不通过 |  | 是/否 |
"@
}

foreach ($name in $markdownFiles.Keys) {
    Set-Content -LiteralPath (Join-Path $outDir $name) -Value $markdownFiles[$name] -Encoding UTF8
}

Write-Host "Generated templates in $outDir"
