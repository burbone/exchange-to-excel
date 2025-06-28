# Инструкция по развертыванию

## 🚀 Быстрый запуск

### 1. Скачивание и запуск готового JAR

1. **Скачайте JAR-файл** из раздела Releases
2. **Откройте командную строку/терминал**
3. **Перейдите в папку с JAR-файлом**
4. **Запустите:**
   ```bash
   java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar
   ```
5. **Откройте браузер:** `http://localhost:8080`

### 2. Сборка из исходного кода

#### Windows
```bash
# Клонируйте репозиторий
git clone https://github.com/burbone/exchange-to-excel.git
cd exchange-to-excel

# Соберите проект
./mvnw.cmd clean package -DskipTests

# Запустите
java -jar target/exchange-to-excel-0.0.1-SNAPSHOT.jar
```

#### Linux/Mac
```bash
# Клонируйте репозиторий
git clone https://github.com/burbone/exchange-to-excel.git
cd exchange-to-excel

# Сделайте скрипт исполняемым
chmod +x mvnw
chmod +x start.sh

# Соберите проект
./mvnw clean package -DskipTests

# Запустите
./start.sh
# или
java -jar target/exchange-to-excel-0.0.1-SNAPSHOT.jar
```

## 🔧 Настройка

### Изменение порта
```bash
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar --server.port=8081
```

### Запуск в фоновом режиме (Linux/Mac)
```bash
nohup java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

### Запуск с дополнительными параметрами JVM
```bash
java -Xmx2g -Xms1g -jar exchange-to-excel-0.0.1-SNAPSHOT.jar
```

## 🐳 Docker развертывание

### Создание Dockerfile
```dockerfile
FROM openjdk:17-jre-slim
WORKDIR /app
COPY target/exchange-to-excel-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Сборка и запуск Docker образа
```bash
# Соберите JAR
./mvnw clean package -DskipTests

# Соберите Docker образ
docker build -t exchange-to-excel .

# Запустите контейнер
docker run -p 8080:8080 exchange-to-excel
```

## 📋 Требования к системе

### Минимальные требования
- **Java:** 17 или выше
- **RAM:** 512 MB
- **Диск:** 100 MB свободного места
- **Сеть:** Доступ к интернету для API бирж

### Рекомендуемые требования
- **Java:** 17 или выше
- **RAM:** 2 GB
- **Диск:** 1 GB свободного места
- **CPU:** 2 ядра

## 🔍 Проверка установки

### Проверка Java
```bash
java -version
```
Должно показать версию 17 или выше.

### Проверка работы приложения
1. Запустите приложение
2. Откройте `http://localhost:8080`
3. Должна загрузиться веб-страница с формой

### Проверка API
```bash
curl -X POST http://localhost:8080/api/export-table \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "BTC USDT",
    "interval": "12h",
    "startTime": "2025-06-02 20:54",
    "endTime": "2025-06-09 20:54",
    "columns": [
      {"type": "open", "exchange": "bybit"},
      {"type": "close", "exchange": "bybit"},
      {"type": "high", "exchange": "bybit"},
      {"type": "low", "exchange": "bybit"}
    ]
  }'
```

## 🐛 Устранение неполадок

### Ошибка: "Java not found"
**Решение:**
1. Установите Java 17+ с [официального сайта](https://adoptium.net/)
2. Добавьте Java в PATH
3. Перезапустите командную строку

### Ошибка: "Port 8080 is already in use"
**Решение:**
```bash
# Измените порт
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar --server.port=8081

# Или освободите порт
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Ошибка: "Permission denied" (Linux/Mac)
**Решение:**
```bash
chmod +x mvnw
chmod +x start.sh
```

### Ошибка: "No data in Excel"
**Решение:**
1. Проверьте правильность символа (например, "BTC USDT")
2. Проверьте временной диапазон
3. Убедитесь, что выбранные биржи доступны

## 📊 Мониторинг

### Логи приложения
Логи выводятся в консоль. Для продакшена рекомендуется настроить внешнее логирование.

### Проверка статуса
```bash
curl http://localhost:8080/actuator/health
```

## 🔒 Безопасность

### Рекомендации для продакшена
1. Используйте HTTPS
2. Настройте файрвол
3. Ограничьте доступ к API
4. Настройте мониторинг
5. Регулярно обновляйте Java

### Настройка HTTPS
```bash
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar \
  --server.ssl.key-store=keystore.p12 \
  --server.ssl.key-store-password=password \
  --server.ssl.key-store-type=PKCS12
```

## 📞 Поддержка

При возникновении проблем:
1. Проверьте логи в консоли
2. Убедитесь в соответствии требованиям
3. Обратитесь к разделу "Устранение неполадок"
4. Создайте Issue в GitHub

**Контакты:**
- Email: m.d.burobin@gmail.com
- Telegram: @bur_bone 