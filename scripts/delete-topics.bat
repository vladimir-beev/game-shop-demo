@echo off
setlocal

REM -----------------------------
REM CONFIG
REM -----------------------------
set BROKER=kafka-broker
set BOOTSTRAP=localhost:9092
set KAFKA_PATH=/opt/kafka/bin/kafka-topics.sh
set LOG_DIR=/tmp/kraft-combined-logs

REM Topics to delete
set TOPICS=cart.checkout order.created order.cancelled stock.rejected

echo ============================================
echo   KRaft Topic Deletion Script
echo ============================================

echo Checking if topic deletion is enabled...
docker exec %BROKER% printenv KAFKA_DELETE_TOPIC_ENABLE

echo.
echo Attempting normal Kafka deletion...
for %%t in (%TOPICS%) do (
    echo Deleting topic: %%t
    docker exec %BROKER% %KAFKA_PATH% --delete --topic %%t --bootstrap-server %BOOTSTRAP%
)

echo.
echo Listing topics after delete attempt...
docker exec %BROKER% %KAFKA_PATH% --list --bootstrap-server %BOOTSTRAP%

echo.
echo ============================================
echo   FORCE DELETE (KRaft metadata cleanup)
echo ============================================

echo Removing topic directories from metadata log...
for %%t in (%TOPICS%) do (
    echo Removing metadata for: %%t
    docker exec %BROKER% bash -c "rm -rf %LOG_DIR%/%%t*"
)

echo.
echo Listing topics after forced deletion...
docker exec %BROKER% %KAFKA_PATH% --list --bootstrap-server %BOOTSTRAP%

echo.
echo ============================================
echo   DONE
echo ============================================
pause
