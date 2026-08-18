@echo off
setlocal

REM -----------------------------
REM CONFIG
REM -----------------------------
set BROKER=kafka-broker
set BOOTSTRAP=localhost:9092
set KAFKA_PATH=/opt/kafka/bin/kafka-topics.sh

REM Topics to create
set TOPICS=cart.checkout order.created order.cancelled stock.rejected

echo ============================================
echo   KRaft Topic Creation Script
echo ============================================

echo Creating topics if they do not exist...
for %%t in (%TOPICS%) do (
    echo Creating topic: %%t
    docker exec %BROKER% %KAFKA_PATH% --create ^
        --topic %%t ^
        --bootstrap-server %BOOTSTRAP% ^
        --partitions 1 ^
        --replication-factor 1 ^
        --if-not-exists
)

echo.
echo Listing topics after creation...
docker exec %BROKER% %KAFKA_PATH% --list --bootstrap-server %BOOTSTRAP%

echo.
echo ============================================
echo   DONE
echo ============================================
pause
