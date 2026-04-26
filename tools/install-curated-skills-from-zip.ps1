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
    [string]$RepoOwner = "openai",
    [string]$RepoName = "skills",
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

$codexHome = Get-CodexHome
$skillRoot = Join-Path $codexHome "skills"
New-Item -ItemType Directory -Force -Path $skillRoot | Out-Null

$workRoot = Join-Path ([System.IO.Path]::GetTempPath()) ("codex-skills-" + [Guid]::NewGuid().ToString("N"))
$zipPath = Join-Path $workRoot "skills.zip"
$extractPath = Join-Path $workRoot "extract"
New-Item -ItemType Directory -Force -Path $workRoot | Out-Null

try {
    $zipUrl = "https://codeload.github.com/$RepoOwner/$RepoName/zip/refs/heads/$Ref"
    Write-Host "Downloading $zipUrl"
    Invoke-WebRequest -Uri $zipUrl -Headers @{ "User-Agent" = "codex-skill-install" } -OutFile $zipPath

    Write-Host "Extracting archive"
    Expand-Archive -LiteralPath $zipPath -DestinationPath $extractPath -Force

    $repoRoot = Get-ChildItem -LiteralPath $extractPath -Directory | Select-Object -First 1
    if ($null -eq $repoRoot) {
        throw "Unable to find extracted repo root."
    }

    $installed = New-Object System.Collections.Generic.List[string]
    $skipped = New-Object System.Collections.Generic.List[string]

    foreach ($skill in $Skills) {
        if ($skill -match "[\\/]" -or [string]::IsNullOrWhiteSpace($skill)) {
            throw "Invalid skill name: $skill"
        }

        $source = Join-Path $repoRoot.FullName (Join-Path $BasePath $skill)
        $dest = Join-Path $skillRoot $skill
        Assert-ChildPath -Root $skillRoot -Path $dest

        if (Test-Path -LiteralPath $dest) {
            Write-Host "SKIP existing: $skill"
            $skipped.Add($skill) | Out-Null
            continue
        }

        if (-not (Test-Path -LiteralPath $source)) {
            throw "Skill path not found in archive: $source"
        }
        if (-not (Test-Path -LiteralPath (Join-Path $source "SKILL.md"))) {
            throw "Skill is missing SKILL.md: $skill"
        }

        Copy-Item -LiteralPath $source -Destination $dest -Recurse
        Write-Host "INSTALLED: $skill"
        $installed.Add($skill) | Out-Null
    }

    Write-Host ""
    Write-Host "Install complete."
    Write-Host ("Installed: " + (($installed | ForEach-Object { $_ }) -join ", "))
    Write-Host ("Skipped: " + (($skipped | ForEach-Object { $_ }) -join ", "))
    Write-Host "Destination: $skillRoot"
}
finally {
    if (Test-Path -LiteralPath $workRoot) {
        Assert-ChildPath -Root ([System.IO.Path]::GetTempPath()) -Path $workRoot
        Remove-Item -LiteralPath $workRoot -Recurse -Force
    }
}
