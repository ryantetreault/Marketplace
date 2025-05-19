
Instructions to Run:
1.	 Download program from the GitHub repo.
2.	Navigate to the following folder: marketplace/marketplace_backend/src/main/java/com.cboard.marketplace_backend/SQLFiles
3.	Copy and run both the create and inserts script in MySql to create the database and sample data (or use the attached SQL files in the plato submission).
4.	Navigate to the following file: marketplace/marketplace_backend/src/main/java/resources/application.properties
a.	Change spring.datasource.username=   to your my sql username
b.	Change spring.datasource.password=   to your my sql password
5.	Through the terminal, navigate to the project location (it should be called marketplace).
6.	Run the command: mvn clean install  to build the project.
7.	Navigate to marketplace_backend (within the marketplace directory) and run the command: mvn spring-boot:run  to start the backend. Wait for this to begin running before running the frontend.
8.	On a new terminal window, navigate to marketplace_frontend (within the marketplace directory) and run the command: mvn javafx:run  to start the frontend.

NOTE: This project was designed targeting jdk 21, and some issues were found when using a different version. We used zulu-21 from the below website which solved our issues.

https://www.azul.com/downloads/?package=jdk#zulu
