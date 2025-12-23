#!/bin/bash

# 빠른 단일 테스트용 스크립트

BASE_URL="${BASE_URL:-http://localhost:8088}"
HUB_ID="${HUB_ID:-a73979b2-518d-495e-84be-de3c026acc94}"
PRODUCT_ID="${PRODUCT_ID:-test-product-uuid}"

echo "Current config:"
curl -s "$BASE_URL/internal/api/v1/dynamic" | jq .

echo ""
echo "Select test:"
echo "1) Hub - CAFFEINE"
echo "2) Hub - REDIS"
echo "3) Hub - NONE"
echo "4) Stock - DEFAULT"
echo "5) Stock - PESSIMISTIC_LOCK"
read -p "Choice: " choice

case $choice in
    1)
        curl -s -X PUT "$BASE_URL/internal/api/v1/dynamic/hub?type=CAFFEINE" | jq .
        k6 run --env BASE_URL="$BASE_URL" --env CACHE_TYPE=CAFFEINE --env HUB_ID="$HUB_ID"  hub-cache-benchmark.js
        ;;
    2)
        curl -s -X PUT "$BASE_URL/internal/api/v1/dynamic/hub?type=REDIS" | jq .
        k6 run --env BASE_URL="$BASE_URL" --env CACHE_TYPE=REDIS --env HUB_ID="$HUB_ID"  hub-cache-benchmark.js
        ;;
    3)
        curl -s -X PUT "$BASE_URL/internal/api/v1/dynamic/hub?type=NONE" | jq .
        k6 run --env BASE_URL="$BASE_URL" --env CACHE_TYPE=NONE --env HUB_ID="$HUB_ID"  hub-cache-benchmark.js
        ;;
    4)
        curl -s -X PUT "$BASE_URL/internal/api/v1/dynamic/stock?type=DEFAULT" | jq .
        k6 run --env BASE_URL="$BASE_URL" --env LOCK_TYPE=DEFAULT --env PRODUCT_ID="$PRODUCT_ID"  stock-lock-benchmark.js
        ;;
    5)
        curl -s -X PUT "$BASE_URL/internal/api/v1/dynamic/stock?type=PESSIMISTIC_LOCK" | jq .
        k6 run --env BASE_URL="$BASE_URL" --env LOCK_TYPE=PESSIMISTIC_LOCK --env PRODUCT_ID="$PRODUCT_ID"  stock-lock-benchmark.js
        ;;
    *)
        echo "Invalid choice"
        ;;
esac
