USER Crud services

Requirements needed to run the application:

1. A running MongoDB on your local machine(localhost:27017) or remotely(must change the connection string in application.yml files)
2. Java 1.8
3. Open with IDE if  you do not have IDE available you can run the project with gradle; you don't need to deploy it to a server it comes with embedded Tomcat
 - just execute the command in the project directory: ./gradlew bootRun

Used technologies:
    - Spring Boot
    - MongoDB

Note:
    - On application startup initialize method is being invoked in order to fill the database with 2 sample users

Sample request messages:

GET -> http://localhost:8080/api/users/  -> fetches all Users from the database

GET -> http://localhost:8080/api/users/{email} -> finds the details about a user with given {email}

POST -> http://localhost:8080/api/users/ -> sends User object to out API and if its valid it is persisted in the database,
NB: the dateOfBirth property is in DateFormat : "yyyy-MM-dd"
sample request json:
    {
        "firstName" : "Peter",
        "lastName": "Beleganski",
        "email" : "email@email.com",
        "dateOfBirth" : "2010-10-10"
    }

DELETE  -> http://localhost:8080/api/users/{email} -> finds the user associated with given email and deletes it from the database

PUT -> http://localhost:8080/api/users/{email} -> send User object associated with email address, if its valid it is updated in the database