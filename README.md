# Holidays Api
Author: Mikołaj Król

Api is limited to last year's historical data only.

## Requirements
- Java 11
- Maven 3.2.5

## Run
Run command in the folder with pom.xml

```sh
mvn spring-boot:run
```

## Swagger

http://localhost:8080/swagger-ui.html#/

## Usage

### Example

GET Request 
```
http://localhost:8080/api/holidays?country1=PL&country2=DE&date=2020-02-10
```
Response 
```
{
  "date": "2020-03-20",
  "name1": "Earth Day",
  "name2": "March Equinox"
}
```
