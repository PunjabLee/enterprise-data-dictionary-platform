[CmdletBinding()]
param(
    [string]$ComposeFile = (Join-Path $PSScriptRoot "..\deploy\docker-compose.yml")
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest

$composeFilePath = (Resolve-Path -LiteralPath $ComposeFile).Path
$deployDir = Split-Path -Parent $composeFilePath
$envFile = Join-Path $deployDir ".env"

$composeArgs = @("compose")
if (Test-Path -LiteralPath $envFile) {
    $composeArgs += @("--env-file", $envFile)
}
$composeArgs += @("-f", $composeFilePath)

& docker @composeArgs ps
