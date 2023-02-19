# ANNEX: Dockerize your Go web API

## 1. Docker and dockerization

* If you don't have Docker installed or if you've never used it let's read [this](./../../docker.md) first.

* If you've never dockerized an application let's read [this](./../../dockerization.md) first.

## 2. Dockerize your Go web API

### 2.1 Write a Dockerfile

Let's move into the $HOME/go/src/pti_golang/webserver folder. Let's create a Dockerfile with the following contents:

	FROM ubuntu:latest
	WORKDIR /
	RUN apt-get update
	RUN apt-get -y install curl
	RUN apt-get -y install wget
	RUN apt-get -y install git
	RUN wget https://gitlab.fib.upc.edu/pti/pti/raw/master/goREST/go1.7.1.linux-amd64.tar.gz
	RUN tar -C /usr/local -xzf go1.7.1.linux-amd64.tar.gz
	ARG PATH=$PATH:/usr/local/go/bin
	RUN mkdir /go
	ARG GOPATH=/go
	RUN mkdir /go/src
	RUN mkdir -p /go/src/pti_golang/webserver
	COPY webserver.go $HOME/go/src/pti_golang/webserver
	RUN /usr/local/go/bin/go get "github.com/gorilla/mux"
	RUN /usr/local/go/bin/go install pti_golang/webserver
	EXPOSE 8080
	CMD /go/bin/webserver

### 2.2 Building the image from the Dockerfile

From the $HOME/go/src/pti_golang/webserver folder run:

	docker build -f Dockerfile -t carrental .

### 2.3 Run a container

Run the container this way:

	docker run --name carrental -d -p 8080:8080 -p 8443:8443 carrental

## 3 Deliver

Deliver a tarball containing the following:

	Dockerfile
	webserver.go


