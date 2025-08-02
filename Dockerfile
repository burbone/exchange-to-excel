# Используем официальный образ Java 17
FROM openjdk:17-jre-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл
COPY target/exchange-to-excel-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт
EXPOSE 8081

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"] 