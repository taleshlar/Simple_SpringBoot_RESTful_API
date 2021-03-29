# embl-assignment

Java version:11<br>

To run:

1: In application.properties Set logback.logging-dir to desired path <br>
2: In IntelliJ IDEA Set 'Use classpath of module' to bootstrap<br>
3: Open http://localhost:8083/swagger-ui.html in a browser to read API documentation and Try APIs.<br>
4: At first you must register a user with desired username and password and get related token<br>  
<pre>curl -X POST "http://localhost:8083/public/users/register?password=testuser&username=testpass" -H "accept: */*"</pre>
5: Then use your token as a Bearer Token to call APIs.<br>
<pre>curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInppcCI6Ikd..." http://localhost:8083/api/people/allpeople</pre>
Note: You can use attached Postman export file to test all APIs.<br><br>
Project structure is based on Java 9 Modules:<br><br>
<h4>A Module is a self-describing collection of Code, Data, and some Resources. It is a set of related Packages, Types (classes, abstract classes, interfaces etc) with Code & Data and Resources.<br>

Each Module contains only a set of related code and data to support Single Responsibility (Functionality) Principle (SRP).</h4>
