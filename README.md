# Exchange to Excel

Получение данных с криптобирж (Bybit, Kucoin) и экспорт в Excel с синхронизацией по времени.

**📖 [English](README_EN.md)**

## 🚀 Быстрый запуск

### Вариант 1: Готовый JAR (рекомендуется)
1. Скачайте `exchange-to-excel-0.0.1-SNAPSHOT.jar` из [Releases](https://github.com/burbone/exchange-to-excel/releases)
2. Запустите: `java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar`
3. Откройте: `http://localhost:8081`

### Вариант 2: Из исходников
```bash
git clone https://github.com/burbone/exchange-to-excel.git
cd exchange-to-excel
./mvnw.cmd clean package -DskipTests  # Windows
java -jar target/exchange-to-excel-0.0.1-SNAPSHOT.jar
```

## 📋 Требования
- Java 17+
- Браузер с JavaScript

## 🎯 Возможности
- ✅ Поддержка Bybit и Kucoin
- ✅ Проверка существования символов
- ✅ Синхронизация данных по времени
- ✅ Экспорт в Excel с настраиваемыми колонками
- ✅ Веб-интерфейс

## 📊 Как использовать
1. Откройте `http://localhost:8081`
2. Выберите биржу и введите символ (например, "BTC USDT")
3. Нажмите "Проверить" для валидации символа
4. Укажите временной диапазон и интервал
5. Настройте колонки для экспорта
6. Скачайте Excel-файл

## 🔧 API
- `POST /api/check-symbol` - проверка символа
- `POST /api/export-table` - экспорт в Excel

## 🚀 Планы на будущее
- [ ] Добавление новых бирж
- [ ] Экспорт в другие форматы (CSV, JSON)

## 🐛 Устранение неполадок
- **Порт занят**: `java -jar app.jar --server.port=8082`
- **Нет данных**: проверьте символ и временной диапазон
- **Java не найдена**: установите Java 17+

## 📞 Контакты
- **Email:** m.d.burobin@gmail.com
- **Telegram:** @bur_bone

---

**⭐ Поставьте звездочку, если проект понравился!**