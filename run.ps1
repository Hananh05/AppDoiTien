param([switch]$CompileOnly)
$ErrorActionPreference = 'Stop'
try {
    Set-Location -LiteralPath $PSScriptRoot
    $compiler = (Get-Command javac.exe -ErrorAction Stop).Source
    $runtime = Join-Path (Split-Path $compiler) 'java.exe'
    if (-not (Test-Path -LiteralPath $runtime)) {
        throw 'Cannot find java.exe beside javac.exe. Install a complete JDK.'
    }
    Write-Host 'Java compiler and runtime:'
    & $compiler -version
    & $runtime -version
    $sources = @(Get-ChildItem -LiteralPath (Join-Path $PSScriptRoot 'src') -Recurse -Filter '*.java' | Select-Object -ExpandProperty FullName)
    if ($sources.Count -eq 0) { throw 'No Java source files found.' }
    $outputDir = Join-Path $PSScriptRoot 'out'
    New-Item -ItemType Directory -Force -Path $outputDir | Out-Null
    & $compiler -encoding UTF-8 -d $outputDir $sources
    if ($LASTEXITCODE -ne 0) { throw 'Compilation failed. See errors above.' }
    Write-Host 'Compilation successful.' -ForegroundColor Green
    if (-not $CompileOnly) {
        & $runtime -cp $outputDir vn.currencyconverter.Main
        if ($LASTEXITCODE -ne 0) { throw 'Application exited with an error.' }
    }
} catch {
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}
