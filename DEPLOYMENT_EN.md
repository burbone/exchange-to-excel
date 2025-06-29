# Deployment Guide

## 🚀 Quick Start

### 1. Ready JAR
```bash
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar
# Open: http://localhost:8081
```

### 2. From Source
```bash
git clone https://github.com/burbone/exchange-to-excel.git
cd exchange-to-excel
./mvnw.cmd clean package -DskipTests  # Windows
java -jar target/exchange-to-excel-0.0.1-SNAPSHOT.jar
```

## 🔧 Configuration

### Change Port
```bash
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar --server.port=8082
```

### Background Run (Linux/Mac)
```bash
nohup java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

### JVM Parameters
```bash
java -Xmx2g -Xms1g -jar exchange-to-excel-0.0.1-SNAPSHOT.jar
```

## 🐳 Docker

### Build and Run
```bash
./mvnw clean package -DskipTests
docker build -t exchange-to-excel .
docker run -p 8081:8081 exchange-to-excel
```

## 📋 Requirements
- **Java:** 17+
- **RAM:** 512 MB (recommended 2 GB)
- **Disk:** 100 MB
- **Network:** internet access

## 🔍 Verification
```bash
# Check Java
java -version

# Check application
curl http://localhost:8081
```

## 🐛 Issues
- **Java not found:** install Java 17+
- **Port in use:** `--server.port=8082`
- **No data:** check symbol and time

## 📞 Support
- Email: m.d.burobin@gmail.com
- Telegram: @bur_bone 