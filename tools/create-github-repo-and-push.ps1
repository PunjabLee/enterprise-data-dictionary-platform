param(
    [string]$RepoName = "enterprise-data-dictionary-platform",
    [string]$Description = "Enterprise data dictionary, metadata governance, data architecture and lineage management platform.",
    [ValidateSet("public", "private")]
    [string]$Visibility = "public",
    [string]$RemoteName = "origin"
)

$ErrorActionPreference = "Stop"
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12

if ([string]::IsNullOrWhiteSpace($env:GITHUB_TOKEN)) {
    throw "GITHUB_TOKEN is not set. Set it in your shell before running this script. Do not commit or paste tokens into files."
}

if (-not (Test-Path -LiteralPath ".git")) {
    throw "Current directory is not a Git repository."
}

$headers = @{
    "Accept"               = "application/vnd.github+json"
    "Authorization"        = "Bearer $env:GITHUB_TOKEN"
    "X-GitHub-Api-Version" = "2022-11-28"
    "User-Agent"           = "codex-repo-bootstrap"
}

Write-Host "Resolving authenticated GitHub user..."
$user = Invoke-RestMethod -Method Get -Uri "https://api.github.com/user" -Headers $headers
$owner = $user.login

Write-Host "Creating repository $owner/$RepoName ..."
$body = @{
    name        = $RepoName
    description = $Description
    private     = ($Visibility -eq "private")
    auto_init   = $false
    has_issues  = $true
    has_projects = $false
    has_wiki    = $false
} | ConvertTo-Json

try {
    $repo = Invoke-RestMethod -Method Post -Uri "https://api.github.com/user/repos" -Headers $headers -Body $body -ContentType "application/json"
}
catch {
    $response = $_.Exception.Response
    if ($null -ne $response -and [int]$response.StatusCode -eq 422) {
        Write-Host "Repository may already exist. Resolving existing repository..."
        $repo = Invoke-RestMethod -Method Get -Uri "https://api.github.com/repos/$owner/$RepoName" -Headers $headers
    }
    else {
        throw
    }
}

$remoteUrl = $repo.clone_url
Write-Host "Repository URL: $remoteUrl"

$remoteNames = @(git remote)
if ($remoteNames -contains $RemoteName) {
    $existingRemote = git remote get-url $RemoteName
    if ($existingRemote -ne $remoteUrl) {
        Write-Host "Updating remote $RemoteName from $existingRemote to $remoteUrl"
        git remote set-url $RemoteName $remoteUrl
    }
}
else {
    Write-Host "Adding remote $RemoteName"
    git remote add $RemoteName $remoteUrl
}

Write-Host "Pushing main, develop and tags..."
git push -u $RemoteName main
git push -u $RemoteName develop
git push $RemoteName --tags

Write-Host "Done."
Write-Host "Remote: $remoteUrl"
