@echo off
chcp 65001 >nul
setlocal

REM 自动查找当前目录或上级目录中的 JDK
if exist "%CD%\tools\jdk" (
    set JAVA_HOME=%CD%\tools\jdk
    set PATH=%JAVA_HOME%\bin;%PATH%
    echo 使用本地 JDK: %JAVA_HOME%
) else if exist "%CD%\..\tools\jdk" (
    set JAVA_HOME=%CD%\..\tools\jdk
    set PATH=%JAVA_HOME%\bin;%PATH%
    echo 使用本地 JDK: %JAVA_HOME%
)

REM 自动查找 Maven
if exist "%CD%\tools\maven\bin" (
    set MAVEN_HOME=%CD%\tools\maven
    set PATH=%MAVEN_HOME%\bin;%PATH%
    echo 使用本地 Maven: %MAVEN_HOME%
) else if exist "%CD%\..\tools\maven\bin" (
    set MAVEN_HOME=%CD%\..\tools\maven
    set PATH=%MAVEN_HOME%\bin;%PATH%
    echo 使用本地 Maven: %MAVEN_HOME%
)

REM 检查 Java
java -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未找到 Java，请先安装 JDK 17+ 并配置环境变量，或将 JDK 解压到 tools/jdk 目录
    pause
    exit /b 1
)

REM 检查 Maven
mvn -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未找到 Maven，请先安装 Maven 3.8+ 并配置环境变量，或将 Maven 解压到 tools/maven 目录
    pause
    exit /b 1
)

echo 正在编译并启动后端服务...
cd /d "%~dp0"
mvn clean package -DskipTests
if errorlevel 1 (
    echo [错误] 编译失败
    pause
    exit /b 1
)

java -jar target\ams-app-1.0.0.jar --spring.profiles.active=dev
pause
