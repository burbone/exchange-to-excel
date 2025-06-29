@echo off
echo.
echo ========================================
echo    Exchange to Excel - Запуск
echo ========================================
echo.
echo Проверяю Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo ❌ Java не найдена! Установите Java 17+
    echo 📥 Скачать: https://adoptium.net/
    pause
    exit /b 1
)

echo ✅ Java найдена
echo.
echo 🚀 Запускаю приложение...
echo 📱 Браузер откроется автоматически через 5 секунд...
echo.

start /min java -jar target/exchange-to-excel-0.0.1-SNAPSHOT.jar

timeout /t 5 /nobreak >nul

echo 🌐 Открываю браузер...
start http://localhost:8081

echo.
echo ✅ Приложение запущено!
echo 🌐 Адрес: http://localhost:8081
echo 📞 Поддержка: m.d.burobin@gmail.com | @bur_bone
echo.
echo Нажмите любую клавишу для выхода...
pause >nul 