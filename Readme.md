Task Application: Java, Spring Boot, Spring Web, Spring Data, MongoDB.

Application fetches last prices of BTC/USD, ETH/USD and XRP/USD from CEX.IO

API is represented with four endpoints:

GET /cryptocurrencies/api/v1/minprice?name=[currency_name] - return record with the lowest price of selected cryptocurrency.

GET /cryptocurrencies/api/v1/maxprice?name=[currency_name] - return record with the highest price of selected cryptocurrency. 

GET /cryptocurrencies/api/v1/?name=[currency_name]&page=[page_number]&size=[page_size] - return a selected page with selected number of elements, default sorting by price.

GET /cryptocurrencies/api/v1/csv - return csv report that contains the following fields: Cryptocurrency Name, Min Price, Max Price.

[currency_name] possible values: BTC, ETH or XRP. If some other value is provided then appropriate error message should be thrown.