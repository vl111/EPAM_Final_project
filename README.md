# Bus park system

<h2>Description</h2>
The task was to create a bus park system, which must have such entities: Driver, Administrator, Bus, Route.
There are buses in Bus park, administrator can asign bus to a route or a bus to a driver; and he aslo can make buses and drivers free.
Driver can confirm his designation.

<h2>How to install</h2>
In order this application to work MySQL must be installed. Database by the name "parking" should exist otherwise you must specify 
a different path in the globalConfig.properties file to a database which will contain buspark tables.
This application creates all required tables automatically, in case they dont exist.

Also com.mysql.jdbc.Driver must be imported; you can download it here [Download Connector/J](https://dev.mysql.com/downloads/connector/j/).
You can import it in File->Project structure in Intellij.
App runs from the main methon in the Main class.
