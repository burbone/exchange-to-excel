# Exchange to Excel

Project for getting data from cryptocurrency exchanges (Bybit, Kucoin) and exporting to Excel with time synchronization.

**📖 [Русская документация](README.md) | [Быстрый старт](QUICK_START.md) | [Развертывание](DEPLOYMENT.md) | [Заметки о релизе](RELEASE_NOTES.md)**

## 🚀 Quick Start

### Option 1: Run ready JAR file (recommended)

1. **Download the JAR file:**
   - Go to [Releases](https://github.com/burbone/exchange-to-excel/releases)
   - Download `exchange-to-excel-0.0.1-SNAPSHOT.jar`

2. **Run the application:**
   ```bash
   java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar
   ```

3. **Open browser:**
   - Go to: `http://localhost:8080`

### Option 2: Build from source code

1. **Clone the repository:**
   ```bash
   git clone https://github.com/burbone/exchange-to-excel.git
   cd exchange-to-excel
   ```

2. **Build the project:**
   ```bash
   # Windows
   ./mvnw.cmd clean package -DskipTests
   
   # Linux/Mac
   ./mvnw clean package -DskipTests
   ```

3. **Run the application:**
   ```bash
   java -jar target/exchange-to-excel-0.0.1-SNAPSHOT.jar
   ```

## 📋 Requirements

- **Java 17** or higher
- **Maven** (only for building from source code)
- **Browser** with JavaScript support

## 🎯 Main Features

- ✅ **Multiple exchange support:** Bybit, Kucoin
- ✅ **Time synchronization** between exchanges
- ✅ **Excel export** with proper formatting
- ✅ **Web interface** for easy use
- ✅ **Scalable architecture** for adding new exchanges
- ✅ **Input validation**
- ✅ **Error handling**

## 📊 How to use

1. **Open web interface** in browser
2. **Select exchanges** (Bybit, Kucoin or both)
3. **Enter parameters:**
   - **Symbol:** e.g., `BTC USDT`, `ETH USDT`
   - **Interval:** 1m, 5m, 15m, 30m, 1h, 4h, 6h, 12h
   - **Start time:** in format `YYYY-MM-DD HH:mm`
   - **End time:** in format `YYYY-MM-DD HH:mm`
4. **Click "Export to Excel"**
5. **Download the ready Excel file**

## 🔧 API Endpoints

### POST `/api/export-table`
Export data to Excel

**Request structure:**
- `symbol` - trading pair symbol (e.g., "BTC USDT")
- `interval` - time interval (1m, 5m, 15m, 30m, 1h, 4h, 6h, 12h)
- `startTime` - start time in format "YYYY-MM-DD HH:mm"
- `endTime` - end time in format "YYYY-MM-DD HH:mm"
- `columns` - array of columns for export:
  - `type` - data type (open, high, low, close)
  - `exchange` - exchange (bybit, kucoin)

**Request example:**
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

**Response:** Excel file with data

## 🏗️ Project Architecture

```
src/main/java/bybit/exchange-to-excel/
├── Controller/
│   └── ExportController.java          # REST API controller
├── api/
│   ├── ExchangeDataPrepare.java       # Interface for data preparation
│   ├── BybitApi/
│   │   └── BybitKlineApi.java         # Bybit API
│   └── KucoinApi/
│       └── KucoinKlineApi.java        # Kucoin API
├── Data/
│   ├── BybitDataPrepare.java          # Data preparation for Bybit
│   └── KucoinDataPrepare.java         # Data preparation for Kucoin
├── service/
│   ├── ExchangeProcessor.java         # Exchange processing
│   ├── DataCollectorService.java      # Data collection
│   ├── DataProcessingService.java     # Data processing
│   ├── ExcelService.java              # Excel generation
│   └── ColumnsInfoService.java        # Column processing
└── model/
    ├── Candle.java                    # Candle model
    └── ColumnsInfo.java               # Column model
```

## 🚀 Deployment

### Local run
```bash
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar
```

### Server deployment
```bash
# Run in background
nohup java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

# Or with settings
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar --server.port=8080
```

### Docker (optional)
```dockerfile
FROM openjdk:17-jre-slim
COPY target/exchange-to-excel-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🔍 Logging

The application outputs logs to console. For production, external logging is recommended.

## 🐛 Troubleshooting

### Problem: "Port 8080 is already in use"
**Solution:**
```bash
# Change port
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar --server.port=8081
```

### Problem: "Java not found"
**Solution:** Install Java 17 or higher

### Problem: "No data in Excel"
**Solution:** Check symbol and time range correctness

## 🤝 Contributing

1. Fork the repository
2. Create a branch for new feature
3. Make changes
4. Create Pull Request

## 📄 License

This project is licensed under MIT License.

## 📞 Contacts

- **Email:** m.d.burobin@gmail.com
- **Telegram:** @bur_bone

---

**⭐ If you liked the project, give it a star!** 