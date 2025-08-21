# Exchange to Excel

Get data from cryptocurrency exchanges (Bybit, Kucoin) and export to Excel with time synchronization.

**📖 [Русский](README.md)**

## 🚀 Quick Start

### Option 1: Ready JAR (recommended)
1. Download `exchange-to-excel-0.0.1-SNAPSHOT.jar` from [Releases](https://github.com/burbone/exchange-to-excel/releases)
2. Run: `java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar`
3. Open: `http://localhost:8081`

### Option 2: From source
```bash
git clone https://github.com/burbone/exchange-to-excel.git
cd exchange-to-excel
./mvnw.cmd clean package -DskipTests  # Windows
java -jar target/exchange-to-excel-0.0.1-SNASPHOT.jar
```

## 📋 Requirements
- Java 21+
- Browser with JavaScript

## 🎯 Features
- ✅ Support for Bybit and Kucoin
- ✅ Symbol existence validation
- ✅ Time-synchronized data
- ✅ Excel export with customizable columns
- ✅ Web interface
- ✅ Download online prices of identical pairs from exchanges

## 📊 How to use
1. Open `http://localhost:8081`
2. Select exchange and enter symbol (e.g., "BTC USDT")
3. Click "Check" to validate symbol
4. Set time range and interval
5. Configure export columns
6. Download Excel file

## 🔧 API
- `POST /api/check-symbol` - symbol validation
- `POST /api/export-table` - Excel export
- `POST /api/download-prices` - export online prices to Excel

## 🚀 Future plans
- [ ] Adding new exchanges

## 🐛 Troubleshooting
- **Port in use**: `java -jar app.jar --server.port=8082`
- **No data**: check symbol and time range
- **Java not found**: install Java 21+

## 📞 Contacts
- **Email:** m.d.burobin@gmail.com
- **Telegram:** @bur_bone

---

**⭐ Star the repository if you like the project!** 