# full-Fledged Mobile Banking System Back-End Template 
built using double entry accounting logic
### Tools Used
- Java 11 , Spring Boot 2.6.3, Tomcat embeded webserver
- Gradle build tool, **H2 in memory DB** , Restful API
- java SMTP email client , thymeleaf for templating engine
- git , github

### How to run this app on your local environment

- You will find a *.war file named **bank-x-app.war** the app source or shared via email.
you can run this file using command 
  - `java -jar bank-x-app.war`
- once the command finishes then open your browser and navigate to http://localhost:8080/swagger-ui/index.html
you should be able to see swagger api doc page 
  
- you can use swagger api doc to test api calls, but the recommended approach is use postman , for this purpose i have exported a postman collection 
you will find a collection named `Bank-x APi.postman_collection.json` in the app source also it will be shared via email
  please import this file onto your postman app and the api testing should be stright forward 

### Important points 
- Note that the system is developed following the standard double entry accounting , hence every financial transaction will have two journal entries each affecting (i.e debit / crediting) its own financial account .
- the first step in the testing process is to register a customer via http://localhost:8080/customer/create api and get **accountNumber**
- you can always use http://localhost:8080/transaction/getTransactionDetail?referenceNumber= api to see details about any financial transaction 
- you can also use http://localhost:8080/customer/getAccountBalance?customerAccountNumber= to verify change in customer's account balance  .

