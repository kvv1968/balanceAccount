Тестовое задание: Баланс счета

1. Сборка проекта `mvn clean install -U`
2. Запуск проекта `BalanceAccountApplication.java`
3. Swagger `http://localhost:8080/swagger-ui/index.html`

Пост запрос `/api/balance/add-transaction` добавить транзакцию
`{  
    "nameBalance": "main", // уникальное имя транзакции, если его нет то создатся баланс с таким именем
    "type": "WITHDRAWAL", // тип операции может быть только WITHDRAWAL или DEPOSIT,
    "amount": 2, //сумма транзакции
    "currency": "EUR" //валюта транзакции, может быть только USD, EUR, BYN, RUB
}`

Гет запрос `/api/balance/sum-amount-balance/{name}` посмотреть баланс, если он есть, передается имя баланса

Гет запрос `/api/balance/all_transaction/{name}` посмотреть все транзакции по балансу, передается имя баланса