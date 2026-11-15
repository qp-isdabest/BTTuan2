$JAVA_HOME = "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2.2\jbr"
if (-not (Test-Path $JAVA_HOME)) {
    $JAVA_HOME = "$env:USERPROFILE\.jdks\openjdk-25"
}
$mvn = "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2.2\plugins\maven\lib\maven3\bin\mvn.cmd"
$TOMCAT_HOME = "D:\Study\2026-2027\HK1\DOT2\WEB\apache-tomcat-11.0.27"
$WAR_NAME = "Week2.war"

# 1. Build WAR
Write-Host ">>> Building WAR with Maven..." -ForegroundColor Cyan
$env:JAVA_HOME = $JAVA_HOME
& $mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host ">>> BUILD FAILED!" -ForegroundColor Red
    exit 1
}

# 2. Copy WAR vào webapps của Tomcat
$warSrc = "target\$WAR_NAME"
$warDst = "$TOMCAT_HOME\webapps\$WAR_NAME"
Write-Host ">>> Copying $WAR_NAME to Tomcat webapps..." -ForegroundColor Cyan
Copy-Item $warSrc $warDst -Force

# 3. Cấu hình biến môi trường và Khởi động Tomcat
Write-Host ">>> Starting Tomcat 11..." -ForegroundColor Green
$env:JAVA_HOME = $JAVA_HOME
$env:JRE_HOME = $JAVA_HOME
$env:CATALINA_HOME = $TOMCAT_HOME
$env:CATALINA_BASE = $TOMCAT_HOME

& "$TOMCAT_HOME\bin\startup.bat"

Write-Host ">>> Tomcat started! Open: http://localhost:8080/Week2" -ForegroundColor Green
