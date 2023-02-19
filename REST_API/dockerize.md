# ANNEX: Dockerize your Node.js web API

## 1. Docker and dockerization

* If you don't have Docker installed or if you've never used it let's read [this](./../../docker.md) first.

* If you've never dockerized an application let's read [this](./../../dockerization.md) first.

* More information about dockerizing a Node.js application [here](https://nodejs.org/en/docs/guides/nodejs-docker-webapp/).

## 2. Dockerize your Node.js web API

### 2.1 Write a Dockerfile

Let's move into the myapp folder (where the "server.js" and "package.json" are). Let's create a Dockerfile there with the following contents:

	FROM node:14

	# Create app directory
	WORKDIR /usr/src/app

	# Install app dependencies
	COPY package*.json ./

	RUN npm install
	
	# Bundle app source
	COPY . .
	COPY server.js .

	EXPOSE 8080
	CMD [ "node", "server.js" ]

### 2.2 Building the image from the Dockerfile

From the myapp folder (where "Dockerfile", "server.js" and "package.json" are) run:

	docker build -f Dockerfile -t carrental .

### 2.3 Run a container

Run the container this way:

	docker run --name carrental -d -p 8080:8080 -p 8443:8443 carrental

## 3 Deliver

Deliver a tarball containing the following files:

	Dockerfile
	server.js
	package.json





