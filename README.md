# Insurance Example Project

## Project Set up

The project benefits from the following:

+ [H2](https://en.wikipedia.org/wiki/H2_(DBMS)) in memory database
+ [Lombok](https://projectlombok.org/) library

## In Memory Database Console
http://localhost:8080/h2-console
+ username: user
+ password: password

### DB Migration and Test Data
The application uses [Flyway](https://flywaydb.org/) for database migration and prepares initial test data. 

## Build and Run the Application
To build the application run the following:
+ `./mvnw clean install`

To run the application run the following, the application will start on port `8080`:
 + `java -jar ./target/insurance-example-0.0.1-SNAPSHOT.jar`

## Application Rest Endpoints
The application provides the following endpoints:

+ `http://localhost:8080/policies/create`
+ `http://localhost:8080/policies/modify`
+ `http://localhost:8080/policies/{policy-id}?requestDate={request-date}`

## Test
+ You may use the Postman collection that includes example requests in the following files:
+ `src/main/resources/test/policy-api.postman_collection.json`
