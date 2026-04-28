[CmdletBinding()]
param(
    [switch]$Wait,
    [switch]$Pull,
    [string]$ComposeFile = (Join-Path $PSScriptRoot "..\deploy\docker-compose.yml")
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest

$composeFilePath = (Resolve-Path -LiteralPath $ComposeFile).Path
$deployDir = Split-Path -Parent $composeFilePath
$envFile = Join-Path $deployDir ".env"
$envExample = Join-Path $deployDir ".env.example"

if (-not (Test-Path -LiteralPath $envFile) -and (Test-Path -LiteralPath $envExample)) {
    Copy-Item -LiteralPath $envExample -Destination $envFile
    Write-Host "Created local environment file: $envFile"
}

$composeArgs = @("compose")
if (Test-Path -LiteralPath $envFile) {
    $composeArgs += @("--env-file", $envFile)
}
$composeArgs += @("-f", $composeFilePath)

function Wait-ForContainerHealth {
    param(
        [Parameter(Mandatory = $true)]
        [string]$ContainerName,
        [int]$TimeoutSeconds = 90
    )

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)

    while ((Get-Date) -lt $deadline) {
        $status = & docker inspect --format "{{if .State.Health}}{{.State.Health.Status}}{{else}}{{.State.Status}}{{end}}" $ContainerName 2>$null
        if ($LASTEXITCODE -eq 0 -and $status -eq "healthy") {
            Write-Host "$ContainerName is healthy"
            return
        }

        if ($status -eq "exited" -or $status -eq "dead") {
            throw "$ContainerName stopped before it became healthy"
        }

        Start-Sleep -Seconds 2
    }

    throw "$ContainerName did not become healthy within $TimeoutSeconds seconds"
}

if ($Pull) {
    & docker @composeArgs pull
}

& docker @composeArgs up -d

if ($Wait) {
    Wait-ForContainerHealth -ContainerName "metadata-platform-postgres"
    Wait-ForContainerHealth -ContainerName "metadata-platform-redis"
}

& docker @composeArgs ps

Write-Host ""
Write-Host "PostgreSQL default: localhost:5432 database=metadata_platform user=metadata_app"
Write-Host "Redis default:      localhost:6379"
Write-Host "Override ports and credentials in $envFile when needed."
