@echo off
setlocal

REM -----------------------------
REM CONFIG
REM -----------------------------
set BROKER=kafka-broker
set BOOTSTRAP=localhost:9092
set KAFKA_PATH=/opt/kafka/bin/kafka-topics.sh

echo ============================================
echo   KRaft Topic Listing Script
echo ============================================

echo Listing Kafka topics...
docker exec %BROKER% %KAFKA_PATH% --list --bootstrap-server %BOOTSTRAP%

echo.
echo ============================================
echo   DONE
echo ============================================
pause
