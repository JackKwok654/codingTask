# Light version Warehouse Inventory System
The simple Warehouse Inventory System using Spring-boot framework for the back-end and ReactJs for the front-end

# Features
  - able to store product data via csv file consumption
  - able to store quantities of such products in different locations via csv file consumption
  - UI to show inventory level of given product code
  - able to transfer inventory from one location to another given amount of quantity and product code via UI

# Setup
In this application, I use the MySQL server as the database, so you may have to follow the instruction as down below first:

 - First thing to do is open and login the mysql in your cmd, and the port number of the server is 3306.
 - Then run the following commands at the mysql prompt:

```sh
mysql> create database db_example; -- Creates the new database for the application
mysql> create database db_example_test; -- Creates the new database for test the application
mysql> create user 'springuser'@'%' identified by 'ThePassword'; -- Creates the user
mysql> grant all on db_example.* to 'springuser'@'%';
mysql> grant all on db_example_test.* to 'springuser'@'%'; -- Gives all privileges to the new user on the newly created database
```

If you do not want to use the default setting, you can go to "codingTask\src\main\resources\application.properties" and "codingTask\src\test\resources\application.properties" change by yourself.

# Test
I write simple test case on "codingTask\src\test\java\com\jack\codingTask", you can run them by cd to the "codingTask" file, and then run the following command:
```sh
mvn test
```

# Complie & Run
To compile this applcation, you need to use the webpack, if you do not have it, you can install the webpack as the following command: (you can skip this part and directly go to run part if you want!!!)
```sh
npm install webpack
```

If you having the webpack, you can cd to the "codingTask" file, and then run the following command:
```sh
webpack
```

After it finish, you can run the following command to run the application:
```sh
mvnw spring-boot:run
```
After compile it successfully, you can browse [localhost:8080] on browser to use the applcation

# Do not refresh the page
I use the react as the front end, if there have some data changed, it will refresh automatically. But if you refresh by careless, you can browse [localhost:8080] again.

# Navbar
The navbar will help you to redirect to different components, you can simply just click on them.

# Products List

### Upload CSV
It you compile it successfully and browse [localhost:8080] on browser, you will able to see the Products List. The Products List should be empty, but I provided the sample data in "\codingTask\dataset", you can upload the products.csv to this page, it will refresh automatically and you will able to see the products.

- CSV format: code,name,weight

### Search Bar
You can search products by name, eg. input "Apple" you will see the AP-HKTV02 Apple Pie.

### Product
You can click on the product and you will able to see the product's info on the right hand side. And there have a yellow button "Edit", you can click it and edit the product's name and weight or you even can delete it. If you want to go back the products list page, you can click the Products on the navbar.

### Download CSV
You can click on the green button "Download products.csv" to download the products.csv which contains all the products data.

### Remove All
You can click on the red button "Remove All" to remove all the products' data on the database.

# Add Product
You can click the Add Product on the navbar and it will redirect you to the form, you can fill in the form to create the new product's data and add on the database.

# Stock Record List

### Upload CSV
Same as the Products List, the Stocks list will be empty as first, you can upload the stocks.csv which is in "\codingTask\dataset". After that, it will refresh automatically and you will able to see the stock records.
- CSV format: location,code,quantity

### Search Bar
You can search stock records by product code, eg. input "AP" you will see the AP-HKTV02 in TKO and CWB ,each have 200 quantity.

### Stock Record
You can click on the stock record and you will able to see the stock record's info on the right hand side. There have the yellow button "Update" and green button "Transfer". 

##### Yellow Button "Update"
The Update button allow you to change the quantity of the stock record, you can just type in the new quantity and click update, and you can see the change on Stocks list. Also you can click the detele button to delete the record.

##### Green Button "Transfer"
Once you click on the Transfer Button, you will able to see the stock record on the top, and down below you can see the Transfer To and Transfer Quantity. You can input the location you want to transfer to in "Transfer To" (can be a new place and it will create the new record), the quantity you want to transfer in "Transfer Quantity", and then click the green button "transfer" and it will show the result on down below or you can go the Stocks list check it.

### Download CSV
You can click on the green button "Download stocks.csv" to download the products.csv which contains all the stock records' data.

### Remove All
You can click on the red button "Remove All" to remove all the  stock records' data on the database.

# Add Stock Record
You can click the Add Stock Record on the navbar and it will redirect you to the form, you can fill in the form to create the new stock record's data and add on the database.

# Daily Log Book

### 4/6
I got the email on this day, so I start the task on this day. I choose the spring boot as the back-end framwork and start to think about how to setup the database. The products database is simple, just use the code as the primary key, but the stock record database need to use the loaction and product code as the primary key, also the product code is the foreign key. I use the many-to-one to link up stock record and products. Then I finished the model and repository part at this day.

### 5/6
In this day, start code the controller part and finish the simple get, put and post data part, then i use the postman to test it. After that, I codes some simple test for two controllers in "codingTask\src\test\java\com\jack\codingTask" to make sure everything is ok. Then I use the axios to help call the apis in controller part and wirte a simple reactjs front-end to test those api. The product controller apis is work perfectly but the stock controller is not work when I try to turn the json to Stock class. The problem is on the product code foreign key, because the product code is the Product type and json can not turn it to Product type when it inside the Stock class. I spend a lot fo time on it but still can not fix the problem. So I just unlink the products database and stock record database and rewrite the stock record part. After one more test, everything is really ok, then I start to write the front-end part.

### 6/6
On last two days, I finished the simple part on the applcation, I can create, delete and edit the data on the  applcation. But I still need to write the CSV part. I spent some time goole on it, then I wirte the csv_helper part to help me read and write data to csv, the service part to help me put and get data from database and the message part to help me debug. Then I update the controller part to add some apis and start wite the front-end for csv upload and download. The download is easy, just directly call the api, but the upload have some problem. I spent some time on google and find out that MultipartFile.getContentType() cannot recognize my upload file is csv. I spent a lot of time on it, but it still can not recognize my file, so I turn down my check is csv or not function and all functions are work. And then spent some time to make the UI more user friendly.

I tried my best to build the application and learn from it, I know that I still have long way to go and I will keep learning to imporve myself, thank you for giving me this chance for learning.

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


   [localhost:8080]: <http://localhost:8080/>