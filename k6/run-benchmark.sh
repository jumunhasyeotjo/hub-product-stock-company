#!/bin/bash

# 성능 벤치마크 실행 스크립트
# 
# 사전 준비:
# 1. k6 설치: brew install k6
# 2. 앱 실행 (dynamic.enabled=true)
# 3. 테스트 데이터 준비 (HUB_ID, PRODUCT_ID)

BASE_URL="${BASE_URL:-http://localhost:8088}"
HUB_ID="${HUB_ID:-a73979b2-518d-495e-84be-de3c026acc94}"
PRODUCT_ID="${PRODUCT_ID:-test-product-uuid}"
OUTPUT_DIR="./results/$(date +%Y%m%d_%H%M%S)"

mkdir -p "$OUTPUT_DIR"

echo "============================================"
echo "  Performance Benchmark"
echo "  Base URL: $BASE_URL"
echo "  Output: $OUTPUT_DIR"
echo "============================================"

# ==================== Hub Cache Benchmark ====================
echo ""
echo ">>> Hub Cache Benchmark"
echo ""

# CAFFEINE
echo "[1/3] Testing CAFFEINE cache..."
curl -s -X PUT "$BASE_URL/internal/api/v1/dynamic/hub?type=CAFFEINE" | jq .
sleep 2
k6 run \
    --env BASE_URL="$BASE_URL" \
    --env CACHE_TYPE=CAFFEINE \
    --env HUB_ID="$HUB_ID" \
    --out json="$OUTPUT_DIR/hub-caffeine.json" \
    hub-cache-benchmark.js 2>&1 | tee "$OUTPUT_DIR/hub-caffeine.log"

echo "wait for next step"
sleep 20

# REDIS
echo "[2/3] Testing REDIS cache..."
curl -s -X PUT "$BASE_URL/internal/api/v1/dynamic/hub?type=REDIS" | jq .
sleep 2
k6 run \
    --env BASE_URL="$BASE_URL" \
    --env CACHE_TYPE=REDIS \
    --env HUB_ID="$HUB_ID" \
    --out json="$OUTPUT_DIR/hub-redis.json" \
    hub-cache-benchmark.js 2>&1 | tee "$OUTPUT_DIR/hub-redis.log"

echo "wait for next step"
sleep 20

# NONE
echo "[3/3] Testing NO cache..."
curl -s -X PUT "$BASE_URL/internal/api/v1/dynamic/hub?type=NONE" | jq .
sleep 2
k6 run \
    --env BASE_URL="$BASE_URL" \
    --env CACHE_TYPE=NONE \
    --env HUB_ID="$HUB_ID" \
    --out json="$OUTPUT_DIR/hub-none.json" \
    hub-cache-benchmark.js 2>&1 | tee "$OUTPUT_DIR/hub-none.log"

echo "wait for next step"
sleep 20

# 원복
curl -s -X PUT "$BASE_URL/internal/api/v1/dynamic/hub?type=CAFFEINE" > /dev/null

echo "wait for next test"
sleep 30
# ==================== Stock Lock Benchmark ====================
echo ""
echo ">>> Stock Lock Benchmark"
echo ""

# DEFAULT (Atomic Update)
echo "[1/2] Testing DEFAULT (atomic update)..."
curl -s -X PUT "$BASE_URL/internal/api/v1/dynamic/stock?type=DEFAULT" | jq .
sleep 2
k6 run \
    --env BASE_URL="$BASE_URL" \
    --env LOCK_TYPE=DEFAULT \
    --env PRODUCT_ID="$PRODUCT_ID" \
    --out json="$OUTPUT_DIR/stock-default.json" \
    stock-lock-benchmark.js 2>&1 | tee "$OUTPUT_DIR/stock-default.log"

echo "wait for next step"
sleep 20

# PESSIMISTIC_LOCK
echo "[2/2] Testing PESSIMISTIC_LOCK..."
curl -s -X PUT "$BASE_URL/internal/api/v1/dynamic/stock?type=PESSIMISTIC_LOCK" | jq .
sleep 2
k6 run \
    --env BASE_URL="$BASE_URL" \
    --env LOCK_TYPE=PESSIMISTIC_LOCK \
    --env PRODUCT_ID="$PRODUCT_ID" \
    --out json="$OUTPUT_DIR/stock-pessimistic.json" \
    stock-lock-benchmark.js 2>&1 | tee "$OUTPUT_DIR/stock-pessimistic.log"

# 원복
curl -s -X PUT "$BASE_URL/internal/api/v1/dynamic/stock?type=DEFAULT" > /dev/null

# ==================== 결과 요약 ====================
echo ""
echo "============================================"
echo "  Benchmark Complete!"
echo "  Results saved to: $OUTPUT_DIR"
echo "============================================"
echo ""
echo "Quick comparison:"
echo ""
echo "Hub Cache (p95 latency):"
grep "hub_read_latency.*p(95)" "$OUTPUT_DIR"/hub-*.log | head -3
echo ""
echo "Stock Lock (p95 latency):"
grep "stock_increment_latency.*p(95)" "$OUTPUT_DIR"/stock-*.log | head -2
