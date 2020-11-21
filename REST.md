# Test MealRestController with curl

## Get all meals
```sh
curl --location --request GET 'http://localhost:8080/topjava/rest/meals'
```

## Create new meal
```sh
curl --location --request POST 'http://localhost:8080/topjava/rest/meals' \
--header 'Content-Type: application/json' \
--data-raw '{
    "calories": 550,
    "dateTime": "2020-02-01T20:00:00",
    "description": "Полдник"
}'
```

## Get meal by id
```sh
curl --location --request GET 'http://localhost:8080/topjava/rest/meals/100011'
```

## Update meal by id
```sh
curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100011' \
--header 'Content-Type: application/json' \
--data-raw '{
    "calories": 600,
    "dateTime": "2020-02-01T20:00:00",
    "description": "Ужин",
    "id": 100011
}'
```

## Delete meal by id
```sh
curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/100011'
```

## Get filtered
```sh
curl --location --request GET 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-30&startTime=10:01&endTime=14:00'
```
