# Deployment Guide

## 🚀 Quick Start

### 1. Download and run ready JAR

1. **Download JAR file** from Releases section
2. **Open command line/terminal**
3. **Navigate to JAR file folder**
4. **Run:**
   ```bash
   java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar
   ```
5. **Open browser:** `http://localhost:8080`

### 2. Build from source code

#### Windows
```bash
# Clone repository
git clone https://github.com/burbone/exchange-to-excel.git
cd exchange-to-excel

# Build project
./mvnw.cmd clean package -DskipTests

# Run
java -jar target/exchange-to-excel-0.0.1-SNAPSHOT.jar
```

#### Linux/Mac
```bash
# Clone repository
git clone https://github.com/burbone/exchange-to-excel.git
cd exchange-to-excel

# Make script executable
chmod +x mvnw
chmod +x start.sh

# Build project
./mvnw clean package -DskipTests

# Run
./start.sh
# or
java -jar target/exchange-to-excel-0.0.1-SNAPSHOT.jar
```

## 🔧 Configuration

### Change port
```bash
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar --server.port=8081
```

### Run in background (Linux/Mac)
```bash
nohup java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

### Run with additional JVM parameters
```bash
java -Xmx2g -Xms1g -jar exchange-to-excel-0.0.1-SNAPSHOT.jar
```

## 🐳 Docker deployment

### Create Dockerfile
```dockerfile
FROM openjdk:17-jre-slim
WORKDIR /app
COPY target/exchange-to-excel-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build and run Docker image
```bash
# Build JAR
./mvnw clean package -DskipTests

# Build Docker image
docker build -t exchange-to-excel .

# Run container
docker run -p 8080:8080 exchange-to-excel
```

## 📋 System Requirements

### Minimum requirements
- **Java:** 17 or higher
- **RAM:** 512 MB
- **Disk:** 100 MB free space
- **Network:** Internet access for exchange APIs

### Recommended requirements
- **Java:** 17 or higher
- **RAM:** 2 GB
- **Disk:** 1 GB free space
- **CPU:** 2 cores

## 🔍 Installation verification

### Check Java
```bash
java -version
```
Should show version 17 or higher.

### Check application
1. Run application
2. Open `http://localhost:8080`
3. Web page with form should load

### Check API
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

## 🐛 Troubleshooting

### Error: "Java not found"
**Solution:**
1. Install Java 17+ from [official site](https://adoptium.net/)
2. Add Java to PATH
3. Restart command line

### Error: "Port 8080 is already in use"
**Solution:**
```bash
# Change port
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar --server.port=8081

# Or free port
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Error: "Permission denied" (Linux/Mac)
**Solution:**
```bash
chmod +x mvnw
chmod +x start.sh
```

### Error: "No data in Excel"
**Solution:**
1. Check symbol correctness (e.g., "BTC USDT")
2. Check time range
3. Ensure selected exchanges are available

## 📊 Monitoring

### Application logs
Logs are output to console. For production, external logging is recommended.

### Status check
```bash
curl http://localhost:8080/actuator/health
```

## 🔒 Security

### Production recommendations
1. Use HTTPS
2. Configure firewall
3. Limit API access
4. Set up monitoring
5. Regularly update Java

### HTTPS configuration
```bash
java -jar exchange-to-excel-0.0.1-SNAPSHOT.jar \
  --server.ssl.key-store=keystore.p12 \
  --server.ssl.key-store-password=password \
  --server.ssl.key-store-type=PKCS12
```

## 📞 Support

If problems occur:
1. Check console logs
2. Ensure requirements compliance
3. Refer to "Troubleshooting" section
4. Create GitHub Issue

**Contacts:**
- Email: m.d.burobin@gmail.com
- Telegram: @bur_bone 