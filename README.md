# Space flight spring boot project
Final learning project with spring technologies including back-end part and client

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
This training project represents space system which is operated by users with different roles and permissions.
It consists of two modules:
* **flight-api** - back end module
* **flight-web** - client with primitive front end
> Base admin user with essential data is already exists in migrations

## Technologies
The following technologies were involved in this project :
* Spring boot 2.7.5
  * Web
  * Validation
  * Security
  * JWT
* Flyway migration
* PostgreSQL

## Setup
>Setup is oriented for Windows

#### Requirements
JRE 19+<br>
Maven 3+<br>
(Optional) Docker<br>

1. Create executable jar file
    * you can use maven panel
    * or input next command in each module:
   ```
   mvn install -f pom.xml
   ```
2. Launch docker-compose file for running as containers via Docker
3. Open http://localhost:8081/login and/or
http://localhost:8080/swagger-ui/index.html#/auth-controller/login to see all realized api
