JAXRS 2.0 Simple Starter Project
================================
This project demonstrates how to implement a very simple web service and is intended to be used as a starter project for new RESTful APIs.

System Requirements:
-------------------------
- Maven 2.0.9 or higher

Building the project:
-------------------------
1. In root directoy

mvn jetty:run

This will build a WAR and run it with embedded Jetty

Then open browser and go to:

http://localhost:8080

Submit form and follow links or just go to the following URL to get Bob Villa's address:

http://localhost:8080/api/customers/1.json

Tests:
--------
To run tests:

mvn clean install
