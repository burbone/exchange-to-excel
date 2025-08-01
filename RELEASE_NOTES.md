# Release Notes - Exchange to Excel

## 🎉 v1.1.0 - 2025-06-29

### ✅ Новые возможности
- ✅ Проверка существования символов на биржах
- ✅ Централизованная обработка символов (DRY принцип)
- ✅ Исправлена обработка символов с пробелами
- ✅ Автоматическое открытие браузера при запуске

### 🔧 Исправления
- Исправлена ошибка `URISyntaxException` при проверке символов
- Убрано дублирование кода обработки символов
- Обновлен порт по умолчанию на 8081
- Улучшена архитектура проекта

### 📊 Поддерживаемые биржи
- **Bybit**: символы без пробелов (BTC USDT → BTCUSDT)
- **Kucoin**: символы с дефисами (BTC USDT → BTC-USDT)

### 🚀 Быстрый запуск
```bash
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar
# Откройте: http://localhost:8081
```

### 🔮 Планы на будущее
- [ ] Добавление новых бирж (Binance, OKX)
- [ ] Экспорт в другие форматы (CSV, JSON)
- [ ] Режим реального времени
- [ ] Мобильное приложение

### 📞 Поддержка
- Email: m.d.burobin@gmail.com
- Telegram: @bur_bone

---

**Версия:** 1.1.0  
**Дата:** 29.06.2025 