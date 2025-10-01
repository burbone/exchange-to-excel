# Crypto Exchange Arbitrage Platform

[🇷🇺 Русская версия](README.md)

## 📋 Project Description

Web application for analyzing arbitrage opportunities on cryptocurrency exchanges and exporting historical data of crypto pairs. Supports work with 5 major exchanges: **Bybit**, **Bitget**, **KuCoin**, **OKX**, **HTX**.

### Key Features

- **📊 Real-time Arbitrage Analysis** - finding the best opportunities for arbitrage trading
- **🔄 Price Comparison Matrix** - comparing buy/sell prices between all exchanges for selected pairs
- **📈 Historical Data Export** - exporting historical candles (OHLC) to Excel
- **✅ Symbol Validation** - validating trading pairs on exchanges
- **⚡ WebSocket Connection** - tracking user activity

---

## 🛠 Technology Stack

### Backend
- **Java 21**
- **Spring Boot 3.5.5**
    - Spring Web
    - Spring WebFlux
    - Spring WebSocket
    - Spring DevTools
- **Lombok** - code simplification
- **Jackson** - JSON processing
- **Apache POI 5.4.1** - Excel file generation
- **Java HTTP Client** - API requests to exchanges
- **Maven** - project build

### Frontend
- HTML5, CSS3, JavaScript (Vanilla JS)
- WebSocket for real-time updates

### API Integrations
- Bybit API v5
- Bitget API v2
- KuCoin API v1/v2
- OKX API v5
- HTX (Huobi) Market API

---

## 🚀 Quick Start

### Requirements
- Java 21
- Maven 3.6+
- 2GB free RAM

### Installation and Launch
#### Option 1: Run from Source

1. **Clone the repository:**
```bash
  git clone <repository-url>
  cd Exchange
```

2. **Build the project:**
```bash
  mvn clean install
```

3. **Run the application:**
```bash
  mvn spring-boot:run
```

4. **Open browser:**
    - The application will automatically open browser at `http://localhost:8081`
    - If it doesn't open automatically - navigate manually

#### Option 2: Run via JAR File

1. Download the latest release:
    - Go to Releases section
    - Download Exchange-0.0.1-SNAPSHOT.jar file
2. Run the application:
```bash
  java -jar Exchange-0.0.1-SNAPSHOT.jar
```
#### After Launch:
 - The application will automatically open browser at http://localhost:8081
 - If it doesn't open automatically - navigate manually
   #### Building JAR file manually:
```bash
    mvn clean package
    # JAR file will be in target/Exchange-0.0.1-SNAPSHOT.jar
```

---

## 📖 Usage

### 1. Online Arbitrage (online.html)

**Top-5 Arbitrage Opportunities:**
- Automatic updates every second
- Shows the best pairs for arbitrage
- Displays exchanges for buying/selling and profit percentage

**Arbitrage Matrix:**
- Enter trading pair symbol (e.g., `BTC USDT`)
- Matrix will show price differences between all exchanges

### 2. Historical Data Export (data.html)

**Step 1:** Symbol Validation
- Select exchange
- Enter symbol (e.g., `BTC USDT`, `ETH BTC`)
- Click "Check"

**Step 2:** Configure Parameters
- Specify period (start and end)
- Select candle interval (1m, 5m, 15m, 1h, 4h, etc.)

**Step 3:** Data Selection
- Add columns ("+" button)
- For each column select:
    - Data type: Open, High, Low, Close
    - Exchange: Bybit, Bitget, KuCoin, OKX

**Step 4:** Download
- Click "Download file"
- Get Excel file with historical data

---

## 🏗 Project Architecture

### Package Structure

```
com.Exchange.Exchange
├── api/                    # Exchange API clients
│   ├── bitget/
│   ├── bybit/
│   ├── htx/
│   ├── kucoin/
│   └── OKX/
├── config/                 # Spring configuration
│   ├── ActivityChecker     # Activity monitoring
│   ├── SchedulerConfig     # Scheduler setup
│   └── WebSocketConfig     # WebSocket configuration
├── controller/             # REST controllers
│   ├── data/              # Data export
│   └── tickers/           # Tickers and arbitrage
├── dto/                   # Data Transfer Objects
│   ├── CustomCollections/ # Tickers, matrices
│   ├── Request/          # Requests
│   └── Response/         # Responses
├── handler/              # WebSocket handlers
├── service/              # Business logic
│   ├── data/            # Symbol validation
│   ├── exel/            # Excel generation
│   │   └── Connecter/   # Exchange connectors
│   └── tickers/         # Price operations
└── ExchangeApplication   # Main class
```
---

## 🔧 API Endpoints

### Data Export

**POST** `/api/exportfile`
```json
{
  "symbol": "BTC USDT",
  "startTime": "2024-01-01T00:00",
  "endTime": "2024-01-02T00:00",
  "interval": "1h",
  "columns": [
    {"type": "open", "exchange": "bybit"},
    {"type": "close", "exchange": "bitget"}
  ]
}
```
Returns Excel file with historical data.

### Symbol Validation

**POST** `/api/symbolcheck`
```json
{
  "exchange": "Bybit",
  "symbol": "BTC USDT"
}
```
Returns: `{"ok": true/false}`

### Arbitrage

**GET** `/api/top5`

Returns top-5 arbitrage opportunities:
```json
[
  {
    "symbol": "BTCUSDT",
    "buyExchange": "Bybit",
    "sellExchange": "OKX",
    "buyPrice": 43500.0,
    "sellPrice": 43650.0,
    "profitPercent": 0.345
  }
]
```

**GET** `/api/matrix/{symbol}`

Returns arbitrage matrix for specified symbol.

---

## ⚙️ Configuration

### Main Settings (`application.properties`)

```properties
server.port=8081
spring.application.name=Exchange
logging.level.com.Exchange.Exchange=INFO
```

### Parallelism Configuration

In connectors (`Kline*Connect`):
```java
maxThreads = Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() - 1, 8));
maxConcurrentRequests = 6;
```

### Update Intervals

- **PriceRepo**: price updates every 1 second
- **ActivityChecker**: activity check every 5 seconds
- **Auto-shutdown**: closes after 2 minutes of inactivity

---

### Rate Limiting
- Request batching (950-1500 candles per request)
- Pause between batches (200ms)
- Concurrent request limit (6)

### Price Caching
- ConcurrentHashMap for thread-safety
- Automatic updates every second
- In-memory storage without database

### Auto-shutdown
Application automatically closes when:
- Last browser tab is closed
- 2 minutes of inactivity
- Request to `/health?action=close`

---

## 🐛 Known Limitations

1. **No authentication** - all requests are public
2. **Spot market only** - futures are not supported
3. **Exchange API limitations**:
    - Bybit: 1000 candles per request
    - Bitget: 1000 candles per request
    - KuCoin: 1500 candles per request
    - OKX: 300 candles per request
4. **No persistence** - all data in memory
5. **Single instance** - cannot run multiple copies simultaneously (port 8081)

---

## 📞 Contact

**Author:** Maxim Burobin

- **Telegram:** [@bur_bone](https://t.me/bur_bone)
- **Email:** m.d.burobin@gmail.com

---

## 📝 License

Project created for educational purposes. Use at your own risk.

⚠️ **Disclaimer:** This is not financial advice. Arbitrage trading carries risks. Always verify data before making trading decisions.

---

## 🔮 Possible Improvements

- [ ] Add more exchanges (Binance, Gate.io, BitMart)
- [ ] Implement WebSocket subscriptions for real-time updates
- [ ] Add database for arbitrage history
- [ ] Account for fees when calculating profit
- [ ] Authentication and user dashboard
- [ ] Notifications about profitable opportunities (Telegram bot)
- [ ] Charts and data visualization
- [ ] API for integration with trading bots
- [ ] Docker containerization
- [ ] Unit and integration tests

---

**Thanks for using! 🚀**