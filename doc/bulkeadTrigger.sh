#!/bin/bash
count=0
while (( count++ <= 100 )); do
 curl -i \
 -H "Content-Type: application/json" \
 -X GET \
 -d "2.03" \
 http://localhost:8080/resilience/call-bulkhead-method
 echo
done