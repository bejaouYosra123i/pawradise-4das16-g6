#!/usr/bin/env bash
# End-to-end test of every lost-and-found endpoint.
# Usage (Git Bash):  bash test-lost-and-found.sh            -> tests http://localhost:8081
#                    bash test-lost-and-found.sh http://localhost:8091
# Needs: curl and node (already installed). The service and its database must be running.

BASE="${1:-http://localhost:8081}"
TMP="$(mktemp)"
PASS=0; FAIL=0

# req METHOD PATH EXPECTED_CODE [JSON_BODY]   -> sets $CODE and $BODY
req() {
  local method="$1" path="$2" expected="$3" body="$4"
  if [ -n "$body" ]; then
    CODE=$(curl -s -o "$TMP" -w "%{http_code}" -X "$method" "$BASE$path" -H "Content-Type: application/json" -d "$body")
  else
    CODE=$(curl -s -o "$TMP" -w "%{http_code}" -X "$method" "$BASE$path")
  fi
  BODY="$(cat "$TMP")"
  if [ "$CODE" = "$expected" ]; then
    PASS=$((PASS+1)); printf "  PASS  %-6s %-48s %s\n" "$method" "$path" "$CODE"
  else
    FAIL=$((FAIL+1)); printf "  FAIL  %-6s %-48s got %s, expected %s\n        %s\n" "$method" "$path" "$CODE" "$expected" "${BODY:0:300}"
  fi
}

# json FIELD -> reads a top-level field of $BODY (or of the first element if $BODY is an array)
json() {
  printf '%s' "$BODY" | node -e 'let d="";process.stdin.on("data",c=>d+=c).on("end",()=>{try{let o=JSON.parse(d);if(Array.isArray(o))o=o[0];const v=o?.[process.argv[1]];console.log(v===undefined?"":v)}catch(e){console.log("")}})' "$1"
}

echo "Testing $BASE"
echo
echo "== Lost reports =="
req POST /api/lost-reports 201 '{"reporterId":"u1","petName":"Rex","lostDate":"2026-11-20","animalType":"Dog","breed":"Labrador","color":"Black","size":"Large","city":"Tunis","latitude":36.8065,"longitude":10.1815}'
LOST=$(json id)
req POST /api/lost-reports 400 '{"petName":"No type"}'
req POST /api/lost-reports 400 '{"animalType":"Dog","latitude":120,"longitude":10}'
req POST /api/lost-reports 400 '{"animalType":"Dog", bad json'
req GET  /api/lost-reports 200
req GET  "/api/lost-reports/$LOST" 200
req PUT  "/api/lost-reports/$LOST" 200 '{"reporterId":"u1","petName":"Rex","lostDate":"2026-11-20","animalType":"Dog","breed":"Labrador","color":"Black","size":"Large","city":"Tunis","latitude":36.8065,"longitude":10.1815,"description":"red collar"}'
req GET  /api/lost-reports/999999 404

echo
echo "== Found reports =="
req POST /api/found-reports 201 '{"reporterId":"u2","foundDate":"2026-11-22","injured":true,"animalType":"dog","breed":"labrador","color":"black","size":"Large","city":"Tunis","latitude":36.81,"longitude":10.18}'
FOUND=$(json id)
req POST /api/found-reports 201 '{"reporterId":"u3","foundDate":"2026-11-22","injured":false,"animalType":"Cat","color":"White","city":"Sousse","latitude":35.8256,"longitude":10.6084}'
FOUND_CAT=$(json id)
req GET  /api/found-reports 200
req GET  "/api/found-reports/$FOUND" 200
req POST "/api/found-reports/$FOUND/rescue" 200
req POST "/api/found-reports/$FOUND/rescue" 409
req POST "/api/found-reports/$FOUND_CAT/rescue" 409

echo
echo "== Reports: list / search / nearby =="
req GET /api/reports 200
req GET "/api/reports/$LOST" 200
req GET "/api/reports/search?type=LOST&animalType=dog&city=tun" 200
req GET "/api/reports/search?status=OPEN&color=black" 200
req GET "/api/reports/search?type=BIRD" 400
req GET "/api/reports/search?status=LOST_IN_SPACE" 400
req GET "/api/reports/nearby?lat=36.8065&lng=10.1815&radiusKm=5" 200
req GET "/api/reports/nearby?lat=36.8065&lng=10.1815&radiusKm=5&type=FOUND" 200
req GET "/api/reports/nearby?lat=36.8065" 400
req GET "/api/reports/nearby?lat=36.8&lng=10.1&radiusKm=0" 400
req GET "/api/reports/nearby?lat=abc&lng=10.1" 400

echo
echo "== Sightings =="
req POST "/api/reports/$LOST/sightings" 201 '{"observerId":"u5","latitude":36.80,"longitude":10.17,"comment":"near the market","evidences":[{"type":"PHOTO","fileUrl":"http://img/1.jpg","score":0.8}]}'
SIGHT=$(json id)
req POST "/api/reports/$LOST/sightings" 400 '{"comment":"no observer"}'
req POST "/api/reports/$LOST/sightings" 400 '{"observerId":"u5","seenAt":"2099-01-01T10:00:00"}'
req POST /api/reports/999999/sightings 404 '{"observerId":"u5"}'
req GET  "/api/reports/$LOST/sightings" 200
req GET  "/api/sightings/$SIGHT" 200
req PUT  "/api/sightings/$SIGHT" 200 '{"observerId":"u5","latitude":36.80,"longitude":10.17,"comment":"near the central market"}'

echo
echo "== Evidences on a sighting =="
req POST "/api/sightings/$SIGHT/evidences" 201 '{"type":"VIDEO","fileUrl":"http://vid/1.mp4"}'
EV_S=$(json id)
req POST "/api/sightings/$SIGHT/evidences" 400 '{"type":"PHOTO"}'
req POST "/api/sightings/$SIGHT/evidences" 400 '{"type":"PHOTO","fileUrl":"x","score":5}'
req GET  "/api/sightings/$SIGHT/evidences" 200
req GET  "/api/evidences/$EV_S" 200
req PUT  "/api/evidences/$EV_S" 200 '{"type":"VIDEO","fileUrl":"http://vid/1-hd.mp4","score":0.9}'
req DELETE "/api/evidences/$EV_S" 204
req GET  "/api/evidences/$EV_S" 404

echo
echo "== Ownership claims (on the FOUND report) =="
req POST "/api/reports/$FOUND/claims" 201 '{"claimantId":"u1","message":"This is my dog Rex","evidences":[{"type":"VET_RECORD","fileUrl":"http://doc/vet.pdf"}]}'
CLAIM=$(json id)
req POST "/api/reports/$FOUND/claims" 409 '{"claimantId":"u1","evidences":[{"type":"PHOTO","fileUrl":"http://img/2.jpg"}]}'
req POST "/api/reports/$FOUND/claims" 400 '{"claimantId":"u9"}'
req POST "/api/reports/$LOST/claims" 409 '{"claimantId":"u9","evidences":[{"type":"PHOTO","fileUrl":"http://img/3.jpg"}]}'
req POST "/api/reports/$FOUND/claims" 201 '{"claimantId":"u7","evidences":[{"type":"PHOTO","fileUrl":"http://img/4.jpg"}]}'
CLAIM2=$(json id)
req GET  "/api/reports/$FOUND/claims" 200
req GET  "/api/claims/$CLAIM" 200
req PUT  "/api/claims/$CLAIM" 200 '{"message":"This is my dog Rex, he has a microchip"}'
req POST "/api/claims/$CLAIM/evidences" 201 '{"type":"MICROCHIP","fileUrl":"http://doc/chip.pdf"}'
req GET  "/api/claims/$CLAIM/evidences" 200
CLAIM2_EV=$(curl -s "$BASE/api/claims/$CLAIM2/evidences" | node -e 'let d="";process.stdin.on("data",c=>d+=c).on("end",()=>console.log(JSON.parse(d)[0].id))')
req DELETE "/api/evidences/$CLAIM2_EV" 409

echo
echo "== Matches =="
req GET "/api/matches/$LOST" 200
MATCH=$(json id)
echo "        best score: $(json score)"
req GET "/api/matches/$FOUND_CAT" 204
req GET /api/matches/999999 404
req PUT "/api/matches/$MATCH/confirm" 200
req PUT "/api/matches/$MATCH/confirm" 409

echo
echo "== Accept / reject claims =="
req PUT "/api/claims/$CLAIM/accept" 200
req GET "/api/claims/$CLAIM2" 200
echo "        other claim is now: $(json status)  (expected REJECTED)"
req PUT "/api/claims/$CLAIM2/accept" 409
req DELETE "/api/claims/$CLAIM" 409
req DELETE "/api/claims/$CLAIM2" 204

echo
echo "== Status =="
req PUT "/api/reports/$FOUND/status" 400 '{"status":"FLYING"}'
req PUT "/api/reports/$FOUND/status" 400 '{}'
req PUT "/api/reports/$FOUND/status" 200 '{"status":"RECOVERED"}'
req PUT "/api/reports/$FOUND/status" 409 '{"status":"RECOVERED"}'
req PUT /api/reports/999999/status 404 '{"status":"CLOSED"}'
req POST "/api/reports/$FOUND/claims" 409 '{"claimantId":"u8","evidences":[{"type":"PHOTO","fileUrl":"http://img/5.jpg"}]}'

echo
echo "== Deletes (children are removed with the report) =="
req DELETE "/api/sightings/$SIGHT" 204
req GET    "/api/sightings/$SIGHT" 404
req DELETE "/api/found-reports/$FOUND" 204
req GET    "/api/found-reports/$FOUND" 404
req DELETE "/api/lost-reports/$LOST" 204
req DELETE "/api/found-reports/$FOUND_CAT" 204

rm -f "$TMP"
echo
echo "Result: $PASS passed, $FAIL failed"
