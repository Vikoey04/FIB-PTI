# ANNEX: Dockerize your servlet-based web application

## 1. Docker and dockerization

* If you don't have Docker installed or if you've never used it let's read [this](./../../docker.md) first.

* If you've never dockerized an application let's read [this](./../../dockerization.md) first.

## 2. Dockerize your servlet-based web application

### 2.1 Write a Dockerfile

First go into the folder that contains the "my_webapp" subfolder. If you followed the recommended steps, this folder will be the Tomcat's webapps folder:

    cd $USER_HOME/apache-tomcat-9.0.37/webapps

Then edit there a file named "Dockerfile" with the following contents:

	vi Dockerfile

    FROM tomcat:jdk14-openjdk-buster
	COPY my_webapp /my_webapp
	WORKDIR /
	RUN cp -r my_webapp /usr/local/tomcat/webapps

### 2.2 Building the image from the Dockerfile

From the folder containing the "my_webapp" subfolder (e.g. $USER_HOME/apache-tomcat-9.0.37/webapps) run:

	docker build -f Dockerfile -t carrental .

### 2.3 Run a container

Run the container this way:

	docker run --name carrental -d -p 8080:8080 -p 8443:8443 carrental

Now check with your browser if the application is running: 

http://localhost:8080/my_webapp/index.html

The teacher will get your Dockerfile and your my_webapp folder and will run the previous docker build and docker run commands.

## 3 Deliver

Deliver a tarball containing the following:

	Dockerfile
	my_webapp


