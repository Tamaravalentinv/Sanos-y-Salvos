param(
    [switch]$RunTests
)

$ErrorActionPreference = "Stop"

function Format-Percent {
    param(
        [int]$Covered,
        [int]$Missed
    )

    $total = $Covered + $Missed
    if ($total -eq 0) {
        return "N/A"
    }

    $percent = [math]::Round(($Covered / $total) * 100, 1)
    return ("{0:N1}%" -f $percent).Replace(".", ",")
}

function Get-CounterPercent {
    param(
        [xml]$Report,
        [string]$Type
    )

    $counter = $Report.report.counter | Where-Object { $_.type -eq $Type } | Select-Object -First 1
    if ($null -eq $counter) {
        return "N/A"
    }

    return Format-Percent -Covered ([int]$counter.covered) -Missed ([int]$counter.missed)
}

function Format-Pct {
    param(
        [double]$Percent
    )

    return ("{0:N1}%" -f $Percent).Replace(".", ",")
}

function Format-Pct2 {
    param(
        [double]$Percent
    )

    return ("{0:N2}%" -f $Percent).Replace(".", ",")
}

$root = Split-Path -Parent $PSScriptRoot
$modules = @(
    "api-gateway",
    "ms-coincidencias",
    "ms-geolocalizacion",
    "ms-notificaciones",
    "ms-proyectos",
    "ms-recursos-humanos",
    "ms-reportes",
    "ms-usuarios"
)

if ($RunTests) {
    foreach ($module in $modules) {
        $modulePath = Join-Path $root $module
        if (-not (Test-Path $modulePath)) {
            continue
        }

        $wrapper = Join-Path $modulePath "mvnw.cmd"
        Write-Host "Running tests for $module..." -ForegroundColor DarkGray

        if (Test-Path $wrapper) {
            Push-Location $modulePath
            try {
                & $wrapper test | Out-Null
            }
            finally {
                Pop-Location
            }
        }
        else {
            Push-Location $modulePath
            try {
                mvn test | Out-Null
            }
            finally {
                Pop-Location
            }
        }
    }

    $frontendPath = Join-Path $root "frontend"
    if (Test-Path $frontendPath) {
        Write-Host "Running tests for frontend..." -ForegroundColor DarkGray
        Push-Location $frontendPath
        try {
            npm test | Out-Null
        }
        finally {
            Pop-Location
        }
    }
}

Write-Host ""
Write-Host "Backend coverage" -ForegroundColor Cyan
Write-Host ""
Write-Host ("{0,-28} {1,8} {2,8} {3,16}" -f "Modulo", "Lineas", "Ramas", "Instrucciones")
Write-Host ("{0,-28} {1,8} {2,8} {3,16}" -f "------", "------", "------", "-------------")

$backendTotals = @{
    LINE = @{ covered = 0; missed = 0 }
    BRANCH = @{ covered = 0; missed = 0 }
    INSTRUCTION = @{ covered = 0; missed = 0 }
}

foreach ($module in $modules) {
    $reportPath = Join-Path $root "$module\target\site\jacoco\jacoco.xml"

    if (-not (Test-Path $reportPath)) {
        Write-Host ("{0,-28} {1,8} {2,8} {3,16}" -f $module, "Sin", "reporte", "-")
        continue
    }

    [xml]$report = Get-Content $reportPath
    $linePercent = Get-CounterPercent -Report $report -Type "LINE"
    $branchPercent = Get-CounterPercent -Report $report -Type "BRANCH"
    $instructionPercent = Get-CounterPercent -Report $report -Type "INSTRUCTION"

    foreach ($type in @("LINE", "BRANCH", "INSTRUCTION")) {
        $counter = $report.report.counter | Where-Object { $_.type -eq $type } | Select-Object -First 1
        if ($null -ne $counter) {
            $backendTotals[$type].covered += [int]$counter.covered
            $backendTotals[$type].missed += [int]$counter.missed
        }
    }

    Write-Host ("{0,-28} {1,8} {2,8} {3,16}" -f $module, $linePercent, $branchPercent, $instructionPercent)
}

$backendLines = Format-Percent -Covered $backendTotals.LINE.covered -Missed $backendTotals.LINE.missed
$backendBranches = Format-Percent -Covered $backendTotals.BRANCH.covered -Missed $backendTotals.BRANCH.missed
$backendInstructions = Format-Percent -Covered $backendTotals.INSTRUCTION.covered -Missed $backendTotals.INSTRUCTION.missed

Write-Host ""
Write-Host "Total backend agregado: $backendLines lineas, $backendBranches ramas, $backendInstructions instrucciones."
Write-Host ""

Write-Host "Frontend coverage" -ForegroundColor Cyan
Write-Host ""
Write-Host ("{0,-10} {1,12} {2,8} {3,7} {4,7}" -f "Frontend", "Statements", "Branch", "Funcs", "Lines")
Write-Host ("{0,-10} {1,12} {2,8} {3,7} {4,7}" -f "--------", "----------", "------", "-----", "-----")

$frontendSummaryPath = Join-Path $root "frontend\coverage\coverage-summary.json"
if (Test-Path $frontendSummaryPath) {
    $summary = Get-Content $frontendSummaryPath -Raw | ConvertFrom-Json
    $statementPercent = Format-Pct2 -Percent ([double]$summary.total.statements.pct)
    $branchPercent = Format-Pct2 -Percent ([double]$summary.total.branches.pct)
    $functionPercent = Format-Pct2 -Percent ([double]$summary.total.functions.pct)
    $linePercent = Format-Pct2 -Percent ([double]$summary.total.lines.pct)

    Write-Host ("{0,-10} {1,12} {2,8} {3,7} {4,7}" -f "Total", $statementPercent, $branchPercent, $functionPercent, $linePercent)
}
else {
    Write-Host ("{0,-10} {1,12} {2,8} {3,7} {4,7}" -f "Total", "Sin reporte", "-", "-", "-")
}
