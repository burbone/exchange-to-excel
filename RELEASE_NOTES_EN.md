# Release Notes - Exchange to Excel v1.0.0

## 🎉 First Release

### ✅ What's Ready

#### Core Features
- ✅ Data retrieval from Bybit API
- ✅ Data retrieval from Kucoin API
- ✅ Time synchronization between exchanges
- ✅ Excel export with proper formatting
- ✅ Web interface for easy use
- ✅ Input validation
- ✅ Error handling

#### Architecture
- ✅ Scalable architecture for adding new exchanges
- ✅ SOLID principles
- ✅ Clean code without duplication
- ✅ Interfaces for extensibility

#### Deployment
- ✅ Ready JAR file for launch
- ✅ Docker support
- ✅ Launch scripts for Windows/Linux
- ✅ Detailed documentation

### 🚀 How to Use

#### Quick Start
1. Download `exchange-to-excel-0.0.1-SNAPSHOT.jar`
2. Run: `java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar`
3. Open: `http://localhost:8080`

#### Build from Source
```bash
git clone https://github.com/burbone/exchange-to-excel.git
cd exchange-to-excel
./mvnw clean package -DskipTests
java -jar target/exchange-to-excel-0.0.1-SNAPSHOT.jar
```

### 📊 Supported Exchanges

| Exchange | Status | Symbols | Intervals |
|----------|--------|---------|-----------|
| Bybit | ✅ | BTC USDT, ETH USDT, etc. | 1m, 5m, 15m, 30m, 1h, 4h, 6h, 12h |
| Kucoin | ✅ | BTC-USDT, ETH-USDT, etc. | 1m, 5m, 15m, 30m, 1h, 4h, 6h, 12h |

### 🔧 Technical Requirements

- **Java:** 17 or higher
- **RAM:** 512 MB (recommended 2 GB)
- **Disk:** 100 MB free space
- **Network:** Internet access

### 📁 File Structure

```
exchange-to-excel/
├── target/
│   └── exchange-to-excel-0.0.1-SNAPSHOT.jar    # Ready JAR file
├── src/                                  # Source code
├── start.bat                            # Launch script for Windows
├── start.sh                             # Launch script for Linux/Mac
├── Dockerfile                           # Docker configuration
├── docker-compose.yml                   # Docker Compose
├── README.md                            # Main documentation
├── DEPLOYMENT.md                        # Deployment guide
└── RELEASE_NOTES.md                     # This file
```

### 🐳 Docker Deployment

```bash
# Build and run
docker-compose up -d

# Or manually
docker build -t exchange-to-excel .
docker run -p 8080:8080 exchange-to-excel
```

### 🔍 API Endpoints

- **POST** `/api/export-table` - Export data to Excel
- **GET** `/` - Web interface

### 📈 Performance

- Startup time: ~10-15 seconds
- JAR file size: ~50 MB
- Memory usage: ~200-500 MB
- Support for 1000+ candles simultaneously

### 🛡️ Security

- Validation of all input data
- Protection against SQL injection (no DB used)
- API error handling
- Logging for debugging

### 🔮 Future Plans

- [ ] Adding new exchanges (Binance, OKX)
- [ ] WebSocket support for real-time data
- [ ] Data caching
- [ ] User authentication
- [ ] Exchange API keys
- [ ] Charts and visualization
- [ ] Event notifications

### 💡 Usage Recommendations

- **Time range:** Recommended no more than 30 days for optimal performance
- **Historical data:** Recommended to use data not older than 1 year for better API compatibility
- **API limitations:** Respect exchange API request limits

### 📞 Support

- **Email:** m.d.burobin@gmail.com
- **Telegram:** @bur_bone
- **GitHub Issues:** [Create Issue](https://github.com/burbone/exchange-to-excel/issues)

---

**Version:** 1.0.0  
**Release Date:** 28.06.2025
**Author:** burbone 
**License:** MIT 