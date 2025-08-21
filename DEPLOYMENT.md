# Инструкция по развертыванию

## 🚀 Быстрый запуск

### 1. Готовый JAR
```bash
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar
# Откройте: http://localhost:8081
```

### 2. Из исходников
```bash
git clone https://github.com/burbone/exchange-to-excel.git
cd exchange-to-excel
./mvnw.cmd clean package -DskipTests  # Windows
java -jar target/exchange-to-excel-0.0.1-SNAPSHOT.jar
```

## 🔧 Настройка

### Изменение порта
```bash
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar --server.port=8082
```

### Запуск в фоне (Linux/Mac)
```bash
nohup java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

### Параметры JVM
```bash
java -Xmx2g -Xms1g -jar exchange-to-excel-0.0.1-SNAPSHOT.jar
```

## 🐳 Docker

### Сборка и запуск
```bash
./mvnw clean package -DskipTests
docker build -t exchange-to-excel .
docker run -p 8081:8081 exchange-to-excel
```

## 📋 Требования
- **Java:** 21+
- **RAM:** 512 MB (рекомендуется 2 GB)
- **Диск:** 100 MB
- **Сеть:** доступ к интернету

## 🔍 Проверка
```bash
# Проверка Java
java -version

# Проверка приложения
curl http://localhost:8081
```

## 🐛 Проблемы
- **Java не найдена:** установите Java 17+
- **Порт занят:** `--server.port=8082`
- **Нет данных:** проверьте символ и время

## 📞 Поддержка
- Email: m.d.burobin@gmail.com
- Telegram: @bur_bone 