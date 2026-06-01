# Script para analizar cobertura JaCoCo
$modules = @(
    "ms-usuarios",
    "ms-reportes",
    "ms-geolocalizacion",
    "ms-coincidencias",
    "ms-notificaciones",
    "api-gateway",
    "ms-proyectos"
)

foreach ($module in $modules) {
    $csvPath = "c:\Users\tamar\Desktop\Sanos-y-Salvos\$module\target\site\jacoco\jacoco.csv"
    
    if (Test-Path $csvPath) {
        # Leer el CSV y calcular cobertura total
        $csv = Get-Content $csvPath | ConvertFrom-Csv
        $totalLineCovered = ($csv | Measure-Object -Property LINE_COVERED -Sum).Sum
        $totalLineMissed = ($csv | Measure-Object -Property LINE_MISSED -Sum).Sum
        
        if ($totalLineCovered -gt 0 -or $totalLineMissed -gt 0) {
            $lineCoverage = [math]::Round(($totalLineCovered / ($totalLineCovered + $totalLineMissed)) * 100, 1)
        } else {
            $lineCoverage = 0
        }
        
        Write-Output "$module : $lineCoverage% (Lines: $totalLineCovered/$($totalLineCovered + $totalLineMissed))"
    } else {
        Write-Output "$module : No report found"
    }
}
