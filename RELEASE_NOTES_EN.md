# Release Notes - Exchange to Excel

## 🎉 v1.1.0 - 2025-06-29

### ✅ New Features
- ✅ Symbol existence validation on exchanges
- ✅ Centralized symbol processing (DRY principle)
- ✅ Fixed symbol processing with spaces
- ✅ Automatic browser opening on startup

### 🔧 Fixes
- Fixed `URISyntaxException` error when checking symbols
- Removed code duplication for symbol processing
- Updated default port to 8081
- Improved project architecture

### 📊 Supported Exchanges
- **Bybit**: symbols without spaces (BTC USDT → BTCUSDT)
- **Kucoin**: symbols with hyphens (BTC USDT → BTC-USDT)

### 🚀 Quick Start
```bash
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar
# Open: http://localhost:8081
```

### 🔮 Future Plans
- [ ] Adding new exchanges (Binance, OKX)
- [ ] Export to other formats (CSV, JSON)
- [ ] Real-time mode
- [ ] Mobile application

### 📞 Support
- Email: m.d.burobin@gmail.com
- Telegram: @bur_bone

---

**Version:** 1.1.0  
**Date:** 2025-06-29 