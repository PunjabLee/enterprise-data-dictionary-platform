param(
    [string[]]$Skills = @(
        "cli-creator",
        "doc",
        "playwright",
        "playwright-interactive",
        "screenshot",
        "security-best-practices",
        "security-ownership-map",
        "security-threat-model"
    ),
    [string]$Repo = "openai/skills",
    [string]$Ref = "main",
    [string]$BasePath = "skills/.curated"
)

$ErrorActionPreference = "Stop"

[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12

function Get-CodexHome {
    if (-not [string]::IsNullOrWhiteSpace($env:CODEX_HOME)) {
        return $env:CODEX_HOME
    }
    return (Join-Path $env:USERPROFILE ".codex")
}

function Assert-ChildPath {
    param(
        [string]$Root,
        [string]$Path
    )
    $rootFull = [System.IO.Path]::GetFullPath($Root)
    $pathFull = [System.IO.Path]::GetFullPath($Path)
    if (-not $pathFull.StartsWith($rootFull, [System.StringComparison]::OrdinalIgnoreCase)) {
        throw "Refusing to operate outside root. Root=$rootFull Path=$pathFull"
    }
}

function Get-GitHubContents {
    param([string]$Path)
    $encodedPath = ($Path -split "/" | ForEach-Object { [System.Uri]::EscapeDataString($_) }) -join "/"
    $url = "https://api.github.com/repos/$Repo/contents/$encodedPath" + "?ref=$Ref"
    return Invoke-RestMethod -Uri $url -Headers @{ "User-Agent" = "codex-skill-install" }
}

function Copy-GitHubDirectory {
    param(
        [string]$RemotePath,
        [string]$LocalPath
    )
    New-Item -ItemType Directory -Force -Path $LocalPath | Out-Null
    $items = Get-GitHubContents -Path $RemotePath
    foreach ($item in $items) {
        $target = Join-Path $LocalPath $item.name
        if ($item.type -eq "dir") {
            Copy-GitHubDirectory -RemotePath $item.path -LocalPath $target
        }
        elseif ($item.type -eq "file") {
            Invoke-WebRequest -Uri $item.download_url -Headers @{ "User-Agent" = "codex-skill-install" } -OutFile $target
        }
        else {
            Write-Host "Skipping unsupported GitHub item type: $($item.type) $($item.path)"
        }
    }
}

$codexHome = Get-CodexHome
$skillRoot = Join-Path $codexHome "skills"
New-Item -ItemType Directory -Force -Path $skillRoot | Out-Null

$installed = New-Object System.Collections.Generic.List[string]
$skipped = New-Object System.Collections.Generic.List[string]

foreach ($skill in $Skills) {
    if ($skill -match "[\\/]" -or [string]::IsNullOrWhiteSpace($skill)) {
        throw "Invalid skill name: $skill"
    }

    $dest = Join-Path $skillRoot $skill
    Assert-ChildPath -Root $skillRoot -Path $dest

    if (Test-Path -LiteralPath $dest) {
        Write-Host "SKIP existing: $skill"
        $skipped.Add($skill) | Out-Null
        continue
    }

    $tmp = Join-Path $skillRoot (".installing-" + $skill + "-" + [Guid]::NewGuid().ToString("N"))
    Assert-ChildPath -Root $skillRoot -Path $tmp

    try {
        Copy-GitHubDirectory -RemotePath "$BasePath/$skill" -LocalPath $tmp
        if (-not (Test-Path -LiteralPath (Join-Path $tmp "SKILL.md"))) {
            throw "Downloaded skill is missing SKILL.md: $skill"
        }
        Move-Item -LiteralPath $tmp -Destination $dest
        Write-Host "INSTALLED: $skill"
        $installed.Add($skill) | Out-Null
    }
    catch {
        if (Test-Path -LiteralPath $tmp) {
            Assert-ChildPath -Root $skillRoot -Path $tmp
            Remove-Item -LiteralPath $tmp -Recurse -Force
        }
        throw
    }
}

Write-Host ""
Write-Host "Install complete."
Write-Host ("Installed: " + (($installed | ForEach-Object { $_ }) -join ", "))
Write-Host ("Skipped: " + (($skipped | ForEach-Object { $_ }) -join ", "))
Write-Host "Destination: $skillRoot"
