# 基於 OpenJDK 17
FROM openjdk:17-jdk-slim
LABEL authors="silvia chang"

# 設定工作目錄
WORKDIR /app

# 複製 JAR 到容器
COPY target/BlogWebsiteServer-1.0-SNAPSHOT.jar app.jar

# 設置環境變數 PORT
ENV PORT=10000

# 啟動命令
CMD ["java", "-jar", "app.jar"]

ENTRYPOINT ["top", "-b"]