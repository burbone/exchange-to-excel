# Exchange to Excel

Проект для получения данных с криптобирж (Bybit, Kucoin) и экспорта в Excel с синхронизацией по времени.

**📖 [English Documentation](README_EN.md) | [Quick Start EN](QUICK_START_EN.md) | [Deployment EN](DEPLOYMENT_EN.md) | [Release Notes EN](RELEASE_NOTES_EN.md)**

## 🚀 Быстрый запуск

### Вариант 1: Запуск готового JAR-файла (рекомендуется)

1. **Скачайте готовый JAR-файл:**
   - Перейдите в раздел [Releases](https://github.com/burbone/exchange-to-excel/releases)
   - Скачайте файл `exchange-to-excel-0.0.1-SNAPSHOT.jar`

2. **Запустите приложение:**
   ```bash
   java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar
   ```

3. **Откройте браузер:**
   - Перейдите по адресу: `http://localhost:8080`

### Вариант 2: Сборка из исходного кода

1. **Клонируйте репозиторий:**
   ```bash
   git clone https://github.com/burbone/exchange-to-excel.git
   cd exchange-to-excel
   ```

2. **Соберите проект:**
   ```bash
   # Windows
   ./mvnw.cmd clean package -DskipTests
   
   # Linux/Mac
   ./mvnw clean package -DskipTests
   ```

3. **Запустите приложение:**
   ```bash
   java -jar target/exchange-to-excel-0.0.1-SNAPSHOT.jar
   ```

## 📋 Требования

- **Java 17** или выше
- **Maven** (только для сборки из исходного кода)
- **Браузер** с поддержкой JavaScript

## 🎯 Основные возможности

- ✅ **Поддержка множественных бирж:** Bybit, Kucoin
- ✅ **Синхронизация данных по времени** между биржами
- ✅ **Экспорт в Excel** с правильным форматированием
- ✅ **Веб-интерфейс** для удобного использования
- ✅ **Масштабируемая архитектура** для добавления новых бирж
- ✅ **Валидация входных данных**
- ✅ **Обработка ошибок**

## 📊 Как использовать

1. **Откройте веб-интерфейс** в браузере
2. **Выберите биржи** (Bybit, Kucoin или обе)
3. **Введите параметры:**
   - **Символ:** например, `BTC USDT`, `ETH USDT`
   - **Интервал:** 1m, 5m, 15m, 30m, 1h, 4h, 6h, 12h
   - **Время начала:** в формате `YYYY-MM-DD HH:mm`
   - **Время окончания:** в формате `YYYY-MM-DD HH:mm`
4. **Нажмите "Export to Excel"**
5. **Скачайте готовый Excel-файл**

## 🔧 API Endpoints

### POST `/api/export-table`
Экспорт данных в Excel

**Структура запроса:**
- `symbol` - символ торговой пары (например, "BTC USDT")
- `interval` - интервал времени (1m, 5m, 15m, 30m, 1h, 4h, 6h, 12h)
- `startTime` - время начала в формате "YYYY-MM-DD HH:mm"
- `endTime` - время окончания в формате "YYYY-MM-DD HH:mm"
- `columns` - массив колонок для экспорта:
  - `type` - тип данных (open, high, low, close)
  - `exchange` - биржа (bybit, kucoin)

**Пример запроса:**
```json
{
  "symbol": "BTC USDT",
  "interval": "12h",
  "startTime": "2025-06-02 20:54",
  "endTime": "2025-06-09 20:54",
  "columns": [
    {"type": "open", "exchange": "bybit"},
    {"type": "close", "exchange": "bybit"},
    {"type": "high", "exchange": "bybit"},
    {"type": "low", "exchange": "bybit"},
    {"type": "open", "exchange": "kucoin"},
    {"type": "close", "exchange": "kucoin"},
    {"type": "high", "exchange": "kucoin"},
    {"type": "low", "exchange": "kucoin"}
  ]
}
```

**Ответ:** Excel-файл с данными

## 🏗️ Архитектура проекта

```
src/main/java/bybit/exchange-to-excel/
├── Controller/
│   └── ExportController.java          # REST API контроллер
├── api/
│   ├── ExchangeDataPrepare.java       # Интерфейс для подготовки данных
│   ├── BybitApi/
│   │   └── BybitKlineApi.java         # API Bybit
│   └── KucoinApi/
│       └── KucoinKlineApi.java        # API Kucoin
├── Data/
│   ├── BybitDataPrepare.java          # Подготовка данных для Bybit
│   └── KucoinDataPrepare.java         # Подготовка данных для Kucoin
├── service/
│   ├── ExchangeProcessor.java         # Обработка бирж
│   ├── DataCollectorService.java      # Сбор данных
│   ├── DataProcessingService.java     # Обработка данных
│   ├── ExcelService.java              # Генерация Excel
│   └── ColumnsInfoService.java        # Обработка колонок
└── model/
    ├── Candle.java                    # Модель свечи
    └── ColumnsInfo.java               # Модель колонки
```

## 🚀 Развертывание

### Локальный запуск
```bash
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar
```

### Запуск на сервере
```bash
# Запуск в фоне
nohup java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

# Или с настройками
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar --server.port=8080
```

### Docker (опционально)
```dockerfile
FROM openjdk:17-jre-slim
COPY target/exchange-to-excel-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🔍 Логирование

Приложение выводит логи в консоль. Для продакшена рекомендуется настроить внешнее логирование.

## 🐛 Устранение неполадок

### Проблема: "Port 8080 is already in use"
**Решение:**
```bash
# Измените порт
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar --server.port=8081
```

### Проблема: "Java not found"
**Решение:** Установите Java 17 или выше

### Проблема: "No data in Excel"
**Решение:** Проверьте правильность символа и временного диапазона

## 🤝 Вклад в проект

1. Форкните репозиторий
2. Создайте ветку для новой функции
3. Внесите изменения
4. Создайте Pull Request

## 📄 Лицензия

Этот проект лицензирован под MIT License.

## 📞 Контакты

- **Email:** m.d.burobin@gmail.com
- **Telegram:** @bur_bone

---

**⭐ Если проект вам понравился, поставьте звездочку!**