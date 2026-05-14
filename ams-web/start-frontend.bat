@echo off
chcp 65001 >nul
cd /d "%~dp0"

REM 检查 Node.js
node -v >nul 2>&1
if errorlevel 1 (
    echo [错误] 未找到 Node.js，请先安装 Node.js 18+
    pause
    exit /b 1
)

REM 检查 node_modules
if not exist "node_modules" (
    echo 正在安装依赖...
    npm install
    if errorlevel 1 (
        echo [错误] 依赖安装失败
        pause
        exit /b 1
    )
)

echo 正在启动前端开发服务器...
npm run dev
pause
