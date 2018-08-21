curl http://localhost:8080/topjava/rest/meals

curl -X GET -H 'Content-Type: application/json' 'http://localhost:8080/topjava/rest/meals/filter?endDate=2015-05-30'

curl --header "Content-Type: application/json"  --request PUT  --data '{"dateTime":"2015-05-30T22:00:00","description": "curlUpdate","calories": 600,"user": null}'  http://localhost:8080/topjava/rest/meals/100006

curl -X "DELETE" http://localhost:8080/topjava/rest/meals/100003