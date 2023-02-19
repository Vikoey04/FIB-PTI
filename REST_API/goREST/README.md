# Making a Web API in Go

## Introduction

A web service is a generic term for a software function that is accessible through HTTP. Traditional web services usually relied in support protocols for data exchange (e.g. SOAP) and service definition (WSDL). However, nowadays the paradigm has evolved to a simplified form, usually called web APIs. Web APIs normally rely only in plain HTTP (plus JSON for serializing the messages). Their design is usually influenced by the [REST architectural style](https://en.wikipedia.org/wiki/Representational_state_transfer), though the most part of existing web APIs do not really comply with REST principles. Nowadays, the most part of client-server systems (e.g. web applications and mobile apps) design their back end as a combination of web APIs.  

The goal of this session is to create a simple web API with the Go programming language and JSON. We will not bother to follow the REST principles, so it will not be a trully RESTful API.  

Each group will have to:

1. Tutorial: Follow a brief tutorial about how to create a simple web API with the Go programming language and JSON.
2. Assignment (basic part): Complete the lab assignment consisting on developing a simple car rental web API. 
3. Extensions: Optionally complete one of the suggested extensions.
4. Write a .pdf report describing the steps taken to complete the assignment, including screenshots of the application output.

## 1 Making a Web API in Go, a quick tutorial

### 1.1 Setup

[(help for those wanting to use their own computers (through Docker))](./../../docker.md)

#### 1.1.1 Booting the machine 

Conventional room: Select a 64-bit Linux image and login with your credentials.

Operating Systems room: Select the latest Ubuntu imatge with credentials user=alumne and pwd:=sistemes

#### 1.1.2 Prerequisites

Let's start by updating the Ubuntu's Package Index:

    sudo apt-get update

You need 'curl' installed (check it typing 'curl -V' in the terminal). If, for some strange reason, it's not, just do:

    sudo apt-get install curl

You need 'git' installed (in order to be able to run 'go get' commands):

    sudo apt-get install git

##### Troubleshooting

If you encounter lock errors with apt-get commands try killing the blocking process:

	ps aux | grep apt
	sudo kill PROCNUM

If that didn't solve the error try changing the default Ubuntu package repository in /etc/apt/sources.list. Replace http://es.archive.ubuntu.com/ubuntu/ by http://en.archive.ubuntu.com/ubuntu/. You can use this command:

	sudo sed -i 's|http://es.|http://en.|g' /etc/apt/sources.list

#### 1.1.3 Install Go

Download Go (if the link does not work go [here](https://golang.org/dl/))
    
	wget https://gitlab.fib.upc.edu/pti/pti/raw/master/goREST/go1.7.1.linux-amd64.tar.gz

(IMPORTANT: The executable will not work if you are working on a 32-bit Linux!)

Extract it into /usr/local 

    sudo tar -C /usr/local -xzf go1.7.1.linux-amd64.tar.gz

Change permissions:
    
    sudo chmod -R 777 /usr/local/go

Add /usr/local/go/bin to the PATH environment variable:

    export PATH=$PATH:/usr/local/go/bin

#### 1.1.4 Setup a directory hierarchy for your projects

(check [this](https://golang.org/doc/code.html) for more info in how to write Go code)

Create a directory to contain your golang workspace (e.g. $HOME/go): 

    cd
    mkdir $HOME/go

Set the GOPATH environment variable to point to that location

    export GOPATH=$HOME/go

Create a directory to contain source files: 

    mkdir $HOME/go/src

Now we will create a package directory (pti_golang/hello) where we will place our first program:

    mkdir -p $HOME/go/src/pti_golang/hello
    cd $HOME/go/src/pti_golang/hello

Now we download a source file:

    wget https://gitlab.fib.upc.edu/pti/pti/raw/master/goREST/src/hello/hello.go

Let's take a look to the content of hello.go:

```go
package main

import "fmt"

func main() {
    fmt.Printf("hello, world\n")
}
```

*A Go program consists on source files organized into a tree of directories called "packages". There's a root package, "pti_golang/hello" in the example, but there may be nested packages too. A package declaration is required at the start of every Go source file. Its main purpose is to determine the default identifier for that package (called the package name) when it is imported by another package. By convention, the package name is the same as the last element of the import path. However, there is an execption when a source file contains the entry point of an executable program such as the example hello.go. In that case, you declare the "package main", regardless of the packages' path, to signal that the package should be compiled as an executable program instead of a shared library.*

Then we run the "go install" command. This command (1) builds the package pti_golang/hello, producing an executable binary (using the last name of the package path as name of the command), and (2) copies it to the workspace's bin directory (it is created if it does not exists). 

    go install pti_golang/hello

Now we can execute the binary:

    $HOME/go/bin/hello
  
### 1.2 A simple web server
    
A web API is a specific type of web (HTTP-based) service. Let's start by programming a basic web server with Go:   

Create a directory for this program:

    mkdir $HOME/go/src/pti_golang/webserver

Edit $HOME/go/src/pti_golang/webserver/webserver.go
```go
package main

import (
    "fmt"
    "html"
    "log"
    "net/http"
)

func main() {
    http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
        fmt.Fprintf(w, "Hello, %q", html.EscapeString(r.URL.Path))
    })

    log.Fatal(http.ListenAndServe(":8080", nil))

}
```

*The package http is a standard Go package that provides HTTP client and server implementations. The function ListenAndServe starts an HTTP server with a given address and a HTTP request router, a ServeMux. A ServeMux is a multiplexor that compares incoming requests against a list of predefined URL paths, and calls the associated handler for the path whenever a match is found. When the ServeMux is nil, the DefaultServeMux it's used. HandleFunc adds handlers to DefaultServeMux. ListenAndServe opens the server port and blocks forever waiting for clients. If it fails to open the port, the log.Fatal call will report the problem and exit the program.*

Let's build it (will create an executable within $HOME/go/bin/webserver):

    go install pti_golang/webserver

Run:

    $HOME/go/bin/webserver &

test in browser: http://localhost:8080

Kill the process before going through the next steps.
    
### 1.3 URL routing
    
An web API exposes different functionalities. These functionalities are accessed through different URL routes or endpoints. We need a mechanism that let us map URL routes into calls to different functions in our code. The standard golang library offers a [too complex routing mechanism](https://husobee.github.io/golang/url-router/2015/06/15/why-do-all-golang-url-routers-suck.html), so we will use an external library for that (mux router from the Gorilla Web Toolkit):

    go get "github.com/gorilla/mux"

(check that a new package object has been created within $HOME/go/pkg).

Let's modify our webserver.go to add some routes:
```go
package main

import (
    "fmt"
    "log"
    "net/http"
    "github.com/gorilla/mux"
)

func main() {

router := mux.NewRouter().StrictSlash(true)
router.HandleFunc("/", Index)
router.HandleFunc("/endpoint/{param}", endpointFunc)

log.Fatal(http.ListenAndServe(":8080", router))
}

func Index(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Service OK")
}

func endpointFunc(w http.ResponseWriter, r *http.Request) {
    vars := mux.Vars(r)
    param := vars["param"]
    fmt.Fprintln(w, "You are calling with param:", param)
}
```
Rebuild, run and open http://localhost:8080/endpoint/1234 in your browser.

**WARNING: The Go compiler does not report warnings, only errors that prevent compilation (e.g. for unused variables or package imports). If you don't fix them the binaries will not be updated.**
   
### 1.4. JSON 

Typically an endpoint has to deal with more complex input and output parameters. This is usually solved by formatting the parameters (input and/or output) with JSON. 

#### 1.4.1 A JSON response

Let's modify our webserver.go to include a JSON response.

```go
package main

import (
    "fmt"
    "log"
    "net/http"
    "github.com/gorilla/mux"
    "encoding/json"
)

type ResponseMessage struct {
    Field1 string
    Field2 string
}

func main() {

router := mux.NewRouter().StrictSlash(true)
router.HandleFunc("/", Index)
router.HandleFunc("/endpoint/{param}", endpointFunc)

log.Fatal(http.ListenAndServe(":8080", router))
}

func Index(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Service OK")
}

func endpointFunc(w http.ResponseWriter, r *http.Request) {
    vars := mux.Vars(r)
    param := vars["param"]
    res := ResponseMessage{Field1: "Text1", Field2: param}
    json.NewEncoder(w).Encode(res)
}
```
Rebuild, run and open http://localhost:8080/endpoint/1234 in your browser.

Let's try also to call the server with curl:

	curl -H "Content-Type: application/json" http://localhost:8080/endpoint/1234

#### 1.4.2 A JSON request

Let's now add a new endpoint that accepts JSON as input. First of all add the following struct:
```go
type RequestMessage struct {
    Field1 string
    Field2 string
}
```
And "io" and "io/ioutil" to the imports:
```go
import (
    	"fmt"
    	"log"
    	"net/http"
    	"github.com/gorilla/mux"
    	"encoding/json"
	"io"
	"io/ioutil"
	) 
```
Then add a new route:
```go
router.HandleFunc("/endpoint2/{param}", endpointFunc2JSONInput)
```
And its related code:
```go
func endpointFunc2JSONInput(w http.ResponseWriter, r *http.Request) {
    var requestMessage RequestMessage
    body, err := ioutil.ReadAll(io.LimitReader(r.Body, 1048576))
    if err != nil {
        panic(err)
    }
    if err := r.Body.Close(); err != nil {
        panic(err)
    }
    if err := json.Unmarshal(body, &requestMessage); err != nil {
        w.Header().Set("Content-Type", "application/json; charset=UTF-8")
        w.WriteHeader(422) // unprocessable entity
        if err := json.NewEncoder(w).Encode(err); err != nil {
            panic(err)
        }
    } else {
        fmt.Fprintln(w, "Successfully received request with Field1 =", requestMessage.Field1)
        fmt.Println(r.FormValue("queryparam1"))
    }
}
```

*This code involves some tricky error handling. [Go’s approach to error handling](https://blog.golang.org/error-handling-and-go) is one of its most controversial features. Go functions support multiple return values, and by convention, this ability is commonly used to return the function’s result along with an error variable. By convention, returning an error signals the caller there was a problem, and returning nil represents no error. The panic built-in function stops the execution of the function and, in a cascading way, of the chain of caller functions, thus stopping the program.*

Rebuild and run. In order to submit a JSON request we will use curl instead of the browser. Open a new terminal and type:

    curl -H "Content-Type: application/json" -d '{"Field1":"Value1", "Field2":"Value2"}' http://localhost:8080/endpoint2/1234?queryparam1=Value3

(while curl is enough for this session, for your project you could take a look at [POSTMAN](https://www.getpostman.com/))

**WARNING: The fields of the request and response structs MUST START WITH A CAPITAL LETTER.**

**WARNING: The fields of the request and response structs MUST ONLY INCLUDE ALPHANUMERIC CHARACTERS (AVOID UNDERSCORES, ETC.).**


## 2 Lab assignment 

### 2.1 Creating your own car rental web API (9 points)

#### 2.1.1 Description

As an example web API you will create a simple car rental web API. It will consist in two functionalities:

- Request a new rental: An endpoint to register a new rental order. Input fields will include the car maker, car model, number of days and number of units. The total price of the rental will be returned to the user along with the data of the requested rental.
 
- Request the list of all rentals: An endpoint that will return the list of all saved rental orders (in JSON format). 

In order to keep the rentals data (to be able to list them) you will need to save the data to the disk. A single text file where each line represents a rental will be enough (though not in a real scenario). ANNEX 1 and ANNEX 2 provide help for witing and reading comma-separated values to/from a CSV file.


### 2.2 Extension (1 point)

In order to obtain the maximum grade you can complete any of the following extensions: 

* Add to the report a 1-page explanation of the pros and cons of Go compared to another alternative (e.g. C++, Java, Rust, etc.).
* Dockerize your web application. Some help [here](./dockerize.md).

## 3.  Submission

You need to upload the following files to your BSCW's lab group folder before the next lab session:

* A tarball containing the source files.
* A .pdf with a report describing the steps taken to complete the assignment, including screenshots of the application output.   


## ANNEX 1. Writing comma-separated values to a CSV file

An easy way to save the list of rentals could be a text file with lines containing comma-separated values (CSV). One rental per line. This way you can save a new rental just adding a line at the end of the file.

	(need to add "encoding/csv" and "os" to imports)
```go
func writeToFile(w http.ResponseWriter, values []string) {
    file, err := os.OpenFile("rentals.csv", os.O_APPEND|os.O_WRONLY|os.O_CREATE, 0600)
    if err!=nil {
        json.NewEncoder(w).Encode(err)
        return
        }
    writer := csv.NewWriter(file)
    writer.Write(values)
    writer.Flush()
    file.Close()
}
```

Yoy may call your function from endpointFunc2JSONInput this way:

```go
writeToFile(w, []string{requestMessage.Field1, requestMessage.Field2})
```

If you don't specify a file path the file will be saved in the directory from which you launch the command. 

## ANNEX 2. Reading a CSV file

In order to read all the lines from a CSV file and to put them within a JSON response you can do:

```go
var rentalsArray []ResponseMessage
file, err := os.Open("rentals.csv", )
if err != nil {
    log.Fatal(err)
} 
reader := csv.NewReader(file)
for {
    line, err := reader.Read()
    if err != nil {
        if err == io.EOF {
            break
        }
        log.Fatal(err)
    }
    rentalsArray = append(rentalsArray, ResponseMessage{Field1: line[0], Field2: line[1]})
}
json.NewEncoder(w).Encode(rentalsArray)

```
     
