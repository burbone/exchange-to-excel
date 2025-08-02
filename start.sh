#!/bin/bash

echo "========================================"
echo "   Exchange to Excel - Запуск"
echo "========================================"
echo ""

# Проверяем Java
echo "Проверяю Java..."
if ! command -v java &> /dev/null; then
    echo "❌ Java не найдена! Установите Java 17+"
    echo "📥 Скачать: https://adoptium.net/"
    exit 1
fi

echo "✅ Java найдена"
echo ""
echo "🚀 Запускаю приложение..."
echo "📱 Браузер откроется автоматически через 5 секунд..."
echo ""

# Запускаем приложение в фоне
java -jar target/exchange-to-excel-0.0.1-SNAPSHOT.jar &
APP_PID=$!

# Ждем 5 секунд
sleep 5

# Открываем браузер
echo "🌐 Открываю браузер..."
if command -v xdg-open &> /dev/null; then
    xdg-open http://localhost:8081
elif command -v open &> /dev/null; then
    open http://localhost:8081
else
    echo "⚠️  Не удалось открыть браузер автоматически"
fi

echo ""
echo "✅ Приложение запущено!"
echo "🌐 Адрес: http://localhost:8081"
echo "📞 Поддержка: m.d.burobin@gmail.com | @bur_bone"
echo ""
echo "Нажмите Ctrl+C для остановки..."
wait $APP_PID 