# Stockmanager

 <p> Stock Manager is a simple backend application provides Uer to manage stocks. This application has API that allows user to create, update, read and delete the stocks. </p>
  
<br>Below are the API endpoints:<br/>
GET <b>/api/stocks</b> (get a list of stocks with Pagination)<br/>
GET <b>/api/stocks/all</b> (gets all the stocks without Pagination. This API is just a placeholder just incase if there are any other Services that consumes this application)<br/>
POST <b>/api/stocks</b> (create a stock)<br/>
GET <b>/api/stocks/{stockId}</b> (gets stock information for a given stock Id)<br/>
PATCH <b>/api/stocks/{stockId}</b> (update the price/currency code of a single stock)<br/>
DELETE <b>/api/stocks/{stockId}</b> (delete a single stock)<br/>
GET <b>/api/stocks/{stockId}/history</b> (gets stock price history information for a given stock Id)<br/>
<br/><br/>
# Considerations:
<p>
As this is Production ready MVP, few important points have been considered </p>
<b>1) Security:</b> Though the application is not fully secured with mechanisms like DB Auth/oAuth etc, it provides a http basic authentication with predefined username/password provided in the configuration.<br/>
Also, there is a hook to check for sql injections and proper validations are also provided<br/>
<b>2) Stock History</b>: It would be a basic need to know the fluctuations of the Stock Prices that happened in the Past. Considering this is a basic need, history of a Stock is also considered. This includes the deleted stocks as well. This information can be used as a feed to AI/ML algorithms to predict the price trend<br/>
<b>3) Logger:</b> Log statements are used whenever possible to get insights of the execution<br/>
<b>4) Swagger: </b>Implemented swagger inorder to get comrehensive details about the exposed APIs<br/>
<b>5) Currency Conversion:</b> This application assumes that the deafult currency is EUR. However, provided a way to convert the user preferred currency to default currency. Taken care that, with few changes/additions new currency conversion can be implemented<br/>
<b>6) Initial Stocks:</b> Externalized the configuration to drive the creation of Stocks<br/>

<br/><br/>

# Technical Decisions:
 </br> </br>
<b>1) DataBase:</b> Stock related inforamtion looks to be very relational. For this MVP, relational DB would be a nice go and there is no need for NoSQL DBs. Hence, considered postgres as the targeted DB and utilised the Dockerized postgres instancs that runs locally. In case of cloud application, Backing services can be used as backend store. </br>
<b>2) Liquibase:</b> As a version control for database, liquibase is the best choice considering the features, support that it provides for free version compared to Flyway. </br>
<b>3) Spring Boot:</b> Spring boot provides a great features and flixibility to integrate services like JPA, REST etc. So utilised these features. Also enabled, actuator inorder to check the health of the services and application.</br>
<b>4) Initial Scripts</b>: Liquibase provides multiple ways to persist prerequisite data like DML statements, reading from a CSV etc. However, for this MVP, used DML statements to create few Stocks and have written java code that creates stocks dynamically for the rest of the stocks. This is driven through a configuration
</br></br>

# Post MVP considerations:(Good to have):
<b>1) Initial scripts:</b> As of now the stocks creation happen during the startup of the application which increases the start up time. This can be moved to separate thread that runs in parallel.</br>
<b>2) Currency Conversion: </b>Actual invocation of third Party APIs is not implemented. This could be enhanced to make third party APIs </br>
<b>3) Cache:</b> Considering the requests and the stocks load, we can introduce cache to serve the requests faster. Redis could be a better choice here</br>
<b>4) Docker:</b> A Docker image can be created for this application to run</br>
<b>5) Rabbit:</b> If any logic should be performed like sending a notification or sending details to third pary systems, we can make use messaging queues</br>

# Running the Stock manager application :
<b>1) Setting up Postgres:</b> Below docker command pulls the postgres image and creates a database with name stocks_db with the provided username and password </br>
 docker run -p 5433:5432 --name pg -e POSTGRES_PASSWORD="password" -e POSTGRES_DB="stocks_db" postgres </br>
<b>2) Checkout teh code and Build</b>: Once the code is checked out, run below command for creating the build. Ensure command is run from the directory where pom file is located. For example: /git/stockmanger/stock-manager</br>
 &nbsp;&nbsp;&nbsp;<b>mvn clean install</b></br>
 
 <b>3) Running the application: </b></br>
 The above command creates a war file with name "stock-manager-0.0.1-SNAPSHOT.war". This can be run using the maven spring plugin from command line. Change directory to the war file locationa and run below command </br>
  &nbsp;&nbsp;&nbsp;<b> mvn spring-boot:run </b> </br>
This starts the application on port 8080 and the APIs can be consumed through  </br>
http:localhost:8080
</br>
APIs can also be accessed through swagger at below location </br>
http://localhost:8080/swagger-ui/index.html
</br></br>
# Order of execution:</br>
1) Get /api/stocks: Fetches information about all the Stocks</br>
2) POST /api/stocks: Can create new stock by providing required details</br>
3) PATCH /api/stocks: For an available stock the price can be updated multiple times</br>
4) Get /api/stocks/{stockId}/history: Fetch the information of all price changes for the stock</br>
5) Delete /api/stock/{stockId} : Delete the stock thats created by providing valid ID</br>
6) Get /api/stocks/{stockId}/history: Fetch the information of all price changes for the stock. "Delete" history should also be present</br>
