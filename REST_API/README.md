# Making a Web API with Node.js


## Introduction

<!--*NOTE: This lab replaces a previous version which relied on using Go instead of Node.js. If, for some reason, you prefer to develop your web API with Go instead of Node.js, you are allowed to do it. The documentation is [here](./goREST/README.md).*-->
A web service is a generic term for a software function that is accessible through HTTP. Traditional web services usually relied in support protocols for data exchange (e.g. SOAP) and service definition (WSDL). However, nowadays the paradigm has evolved to a simplified form, usually called web APIs. Web APIs normally rely only in plain HTTP (plus JSON for serializing the messages). Their design is usually influenced by the [REST architectural style](https://en.wikipedia.org/wiki/Representational_state_transfer), though the most part of existing web APIs do not really comply with REST principles. Nowadays, the most part of client-server systems (e.g. web applications and mobile apps) design their back end as a combination of web APIs.  

The goal of this session is to create a simple web API with the Node.js JavaScript runtime environment. We will not bother to follow the REST principles, so it will not be a trully RESTful API.  

Each group will have to:

1. Tutorial: Follow a brief tutorial about how to create a simple web API with Node.js.
2. Assignment (basic part): Complete the lab assignment consisting on developing a simple car rental web API. 
3. Extensions: Optionally complete one of the suggested extensions.
4. Write a .pdf report describing the steps taken to complete the assignment, including screenshots of the application output.

*NOTE: The tutorial can be followed even if you don't have previous knowledge about JavaScript/Node.js.*

*NOTE: The tutorial was tested with Node.js 14.10.1 and Express 4.17.*


<!--https://www.smashingmagazine.com/2018/01/understanding-using-rest-api/-->

## Some useful links 

- [JavaScript tutorials and reference](https://developer.mozilla.org/en-US/docs/Web/JavaScript)
- [Node.js guides](https://nodejs.org/en/docs/guides/) 
- [Express guide](https://expressjs.com/en/guide/routing.html)


## 1 Making a Web API with Node.js, a quick tutorial

### 1.1 Setup

[Read this if you will do this lab on your own computer](./../yourmachine.md).

#### 1.1.1 Booting the machine (Operating Systems room)

Select the latest Ubuntu imatge with credentials user=alumne and pwd:=sistemes

#### 1.1.2 Prerequisites

Let's start by updating the Ubuntu's Package Index:

    sudo apt-get update

You need cURL installed (check it typing 'curl -V' in the terminal). If, for some strange reason, it's not, just do:

    sudo apt-get install -y curl

##### Troubleshooting

If you encounter lock errors with apt-get commands try killing the blocking process:

	ps aux | grep apt
	sudo kill PROCNUM

If that didn't solve the error try changing the default Ubuntu package repository in /etc/apt/sources.list. Replace http://es.archive.ubuntu.com/ubuntu/ by http://en.archive.ubuntu.com/ubuntu/. You can use this command:

	sudo sed -i 's|http://es.|http://en.|g' /etc/apt/sources.list

#### 1.1.3 Install Node.js

Run the following command:

	curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -

Install Node.js:
	
	sudo apt-get install -y nodejs


#### 1.1.4 Setup a directory for your application and install the Express package

Create a directory to contain your application: 

    cd
    mkdir myapp
    cd myapp

Create a package.json file that will register the dependencies of your application (more information [here](https://docs.npmjs.com/files/package.json)). Run the following and press RETURN to accept all default values:

	npm init

Now install Express in the myapp directory and save it in the dependencies list:

	npm install express --save

The save flag will add Express as a dependency within the package.json file.

We can check which version of Express we installed by typing:

    npm list 

### 1.2 A simple web server
    
A web API is a specific type of web (HTTP-based) service. Let's start by programming a basic web server with Node.js:   

Edit a new file named "server.js" within $HOME/myapp with this content:

```js
const express = require('express')
const app = express()
const port = 8080

app.get('/', (req, res) => {
  res.send('Hello World!')
})

app.listen(port, () => {
  console.log(`PTI HTTP Server listening at http://localhost:${port}`)
})
```

This example:

1. Creates an Express "application" with the express() function. 
2. With a call to the app.get method specifies that HTTP GET requests to the '/' path will be handled by a [callback function](https://developer.mozilla.org/en-US/docs/Glossary/Callback_function) that sends 'Hello World!' back to the client who issued the request (e.g. a browser).
3. Starts a UNIX socket and listens for connections on the port 8080. 

*NOTE: In JavaScript "=>" is more concise way for defining a function (more details [here](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Functions/Arrow_functions)).*

Let's now launch the server:

    node server.js 

Test in browser: http://localhost:8080

For this initial test is practical to run the command this way, in the foreground, and to stop the server with a CTRL+C. Later you may prefer to run the server in the background ("node server.js &").
    
### 1.3 URL routing

#### Endpoints

A web API exposes different functionalities. These functionalities are accessed through different HTTP methods (POST, GET, PUT, PATCH or DELETE) and different URL route paths (e.g. '/'). A route path in combination with a request method, define an **endpoint** at which requests can be made. We need a mechanism that let us map endpoints to different functions in our code. Express method "app.METHOD" (e.g. "app.get") allows to specify which function will handle each request depending on the method and the URL route. We already have an endpoint, HTTP GET requests to the '/' path.

Let's modify our server.js to add a new endpoint. Add the following below the app.get('\'...

```js
app.get("/students", (req, res, next) => {
 res.send('Received request at /students')
});
```
Open http://localhost:8080/students in your browser.

#### Route paths

Route paths (such as "/students") can be strings, string patterns (e.g. "/stud*"), or even regular expressions. We are not going to address that in detail, if necessary you can find more information [here](https://expressjs.com/en/guide/routing.html).

#### Route parameters

Sometimes, some segments of the URL contain values that are relevant to process the request (e.g. "/students/21142250q"). To address these situations you can specify the route parameters in the path of the route as shown in this example:

```js
app.get('/students/:studentId', function (req, res) {
    res.send('Received request at /students with param studentId='+req.params.studentId)
})
```
Change our previous endpoint within "server.js" accordingly, relaunch the server and open http://localhost:8080/students/21142250q in your browser.

In a trully RESTful API, route parameters are very important. However, in this lab session we will not use them. We will focus on the parameters passed with JSON.

Well, this is enough about routing for now. Express includes more sophisticated routing tools such as the app.route() method or the express.Router class but we are not going to address them here.
   
### 1.4. JSON 

Typically, an endpoint has to deal with more complex input and output parameters. This is usually solved by formatting the parameters (input and/or output) with JSON. 

#### 1.4.1 A JSON response

Let's modify our endpoint this way to include a JSON response.

```js
app.get("/students", (req, res, next) => {
    res.json({
        responseId: 1234,
        students: [
            {name: "Jordi", studentId: '12345678a'},
            {name: "Marta", studentId: '12345678b'}
    ]});
});
```
You can see that returning a JSON document as a response is as simple as passing it as a parameter to the "res.json" method.

Relaunch the server and open http://localhost:8080/students in your browser.

Let's try also to call the server with cURL:

```
	curl -H "Content-Type: application/json" http://localhost:8080/students
```

#### 1.4.2 A JSON request

Handling JSON requests can be done with the help of the new "express.json()" built-in function (Express 4.16.0 onwards). 

Let's now add a new endpoint (POST method and "/newstudent" route path) that accepts JSON as input. 

*WARNING: Do not forget to add "app.use(express.json());" or it will not parse the incoming JSON payload.* 

```js
const express = require('express')
const app = express()
const port = 8080

app.use(express.json());

app.post('/newstudent', (req, res, next) => {
    console.log(req.body.name);
    res.end(); 
}) 

app.listen(port, () => {
  console.log(`PTI HTTP Server listening at http://localhost:${port}`)
})
```

Relaunch the server. In order to submit a JSON request we will use cURL instead of the browser. Open a new terminal and type:

```
    curl -H "Content-Type: application/json" -d '{"name":"Fatima", "studentId":"234123412f"}' http://localhost:8080/newstudent
```

As a result the terminal (the server one) should show "Fatima". 

*NOTE: You can see what the server is receiving by replacing "console.log(req.body.name);" by "console.log(req);" or "console.log(req.body);".* 

This time we did not return any HTTP message body. By default, the res.end() will return a response message with an HTTP 200 OK success status response code. You can check the returned HTTP header adding the "-i" option to wour cURL command:

```
    curl -i -H "Content-Type: application/json" -d '{"name":"Fatima", "studentId":"234123412f"}' http://localhost:8080/newstudent
```

Let's try a more complex example:

```js
app.post('/newstudent', (req, res, next) => {
    for(var i in req.body.students){
        console.log(req.body.students[i].name+'\n');
    }
    res.end(); 
}) 
```
In the example you can see how to deal with a JSON request including an array. Relaunch the server. Open a new terminal and type:

```
    curl -i -H "Content-Type: application/json" -d '{"students": [{"name": "Fatima", "studentId": "234123412f"}, {"name": "Maria", "studentId":"16553412g"}]}' http://localhost:8080/newstudent
```

But normally a POST request will have some effect over the state of the server (e.g. adding something to a DB). In those cases, it's conveneint to return back a 201 HTTP status response code (which means "created"). You can do that with the "res.status" function:

```js
    ...
    res.status(201);
    res.end();
    ...
```
Try it and check that the status response code is 201.

*NOTE: while cURL is enough for this session, for your project you could take a look at [POSTMAN](https://www.getpostman.com/)*

<!--
### 1.5. Making our web API "RESTful" 

When developing a web API we may want to follow the [REST architectural style](https://en.wikipedia.org/wiki/Representational_state_transfer). Many people use the term "REST API" or "RESTful" API incorrectly, just to refer to any web API. A RESTful API follows a set of patterns when designing the API endpoints (HTTP method, HTTP status response code, etc.). The benefits of REST are controversial (many relevant web APIs are not RESTful), but it's convenient to know it to be able to understand any API specification. 

#### Using the proper HTTP method

The endpoints of a REST API must use an HTTP method consistent with the type of actions that they perform. People from databases tend to talk about four types of actions: Create, Read, Update and Delete (CRUD). The proper HTTP methods to apply to these actions are POST, GET, PUT (or PATCH) and DELETE, respectively. 


-->

## 2 Lab assignment 

### 2.1 Creating your own car rental web API (8 points)

#### 2.1.1 Description

As an example web API you will create a simple car rental web API. It will consist in two functionalities:

- Request a new rental: An endpoint to register a new rental order. Input fields will include the car maker, car model, number of days and number of units (in JSON format). The endpoint should just return an HTTP status response code 201. 
 
- Request the list of all rentals: An endpoint that will return the list of all saved rental orders (in JSON format). 

In order to keep the rentals data (to be able to list them) you will need to save the data to the disk. A single JSON file will be enough (though not in a real scenario). ANNEX 1 provides help for manipulating JSON files with JavaScript.


### 2.2 Extension 1 (1 point)

Dockerize your web application. Some help [here](./dockerize.md).

### 2.3 Extension 2 (1 point)

Include a CI/CD practice as explained [here](./cicd/CICD.md).

## 3.  Submission

You need to upload the following files to your BSCW's lab group folder before the next lab session:

* A tarball containing the source files.
* A .pdf with a report describing the steps taken to complete the assignment, including screenshots of the application output.   


## ANNEX 1. Reading, manipulating and writing a JSON file in JavaScript

An easy way to save the list of rentals could be a JSON file (not a good idea for a real web API but enough for this session). The following code is an example of how to read/write a JSON document from/to a file and how to modify and display it once in memory.  

```js
const fs = require('fs');

//Create an empty JSON document in memory and save it to a file (students.json)
studentsJSON = {"students": []};
fs.writeFileSync("students.json", JSON.stringify(studentsJSON));

//Read a JSON document from a file
studentsFileRawData = fs.readFileSync('students.json');
studentsJSON = JSON.parse(studentsFileRawData);

//Add something to the (in memory) JSON document
studentsJSON['students'].push({"name": "Marc", "studentId": "12341231h"});

//Display the contents of the array within a JSON document
for(var i in studentsJSON['students']){
    console.log(studentsJSON['students'][i].name+'\n');
}

//Write the (modified) JSON document into a file
fs.writeFileSync("students.json", JSON.stringify(studentsJSON));
```

     
