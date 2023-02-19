# ANNEX: Dockerize your application

## 1. Docker

* If you don't have Docker installed or if you've never used it let's read [this](./../../docker.md) first.

## 2. Dockerize your application

Dockerizing (or containerizing) an application is the process of making it able to run and deploy under Docker containers. Usually the final step of a containerization process is to push a Docker image containg the application to a Docker Registry such as Docker Hub. However, for the PTI lab you need to provide the sources necessaries to build the image, i.e. a Dockerfile and the application. It's not necessary that you push the image to Docker Hub as the teacher will build the image himself.

*NOTE: A Docker volume can be used to access an application in your filesystem from within a Docker container. This is a convenient way to proceed during the development of the application. However, when moving to production, having a Docker image with everything inside makes things easier (e.g. deployment automation). So, for the containerization we will not need a Docker volume. However, we could still need one for persisting application data (not required for the PTI lab).*

## 2.1 Write a Dockerfile

You need first to create a Dockerfile, a text file that contains all commands, in order, needed to build an image. A Dockerfile adheres to a specific format and set of instructions which you can find [here](https://docs.docker.com/engine/reference/builder/).

An example Dockerfile (for a basic Python application) would be:

	FROM python:3
	RUN mkdir -p /usr/src/app
	WORKDIR /usr/src/app
	COPY myapplication.py .
	RUN apt-get install somedependency
	RUN pip install somedependecy
	EXPOSE 8080
	CMD ["python", "./myapplication.py"]

Let's look at the different steps. We start with specifying our base image:

	FROM python:3

You an use, e.g., ubuntu:latest instead, but they you would need to install all the Python tools. The next recommended step is to create a working directory for your application:

	RUN mkdir -p /usr/src/app
	WORKDIR /usr/src/app

WORKDIR sets the working directory for any RUN, CMD, ENTRYPOINT, COPY and ADD instructions that follow it in the Dockerfile. 

Next step is to copy your application into the image. To understand that, you need to know that during the build you will send some files to the Docker daemon (we will see this later), the build context. The COPY command can copy files from the build context into the image. Assuming that you send your application at the root of the build context you can do:

	COPY myapplication.py .

That will copy the application into the WORKDIR. Next step is usually to install the dependencies that are not found within the base image: 

	RUN apt-get install somedependency
	RUN pip install somedependecy

Docker will save an intermediate image for each RUN command. You can avoid keeping too many images this way:

	RUN apt-get install somedependency \
	    pip install somedependecy

If your application listens to any port, the next step is to expose that port:

	EXPOSE 5000

The last step is to write the command that will be executed when anybody will run your image. This command will not be executed in build time.  

	CMD ["python", "./myapplication.py"]

If your application is just an executable file you can just do

	CMD ./myapplication

### 2.2 Building the image from the Dockerfile

From the folder containing your application (the build context) run:

	docker build -f Dockerfile -t imagetag .

The build commands requires a Dockerfile. By default it tries to find one in the current directory but you can specify a different location with the -f option (its usage in the example is unnecessary). The build command will first send the files specified by the PATH (the local directory . in this case) to the Docker daemon (as the Docker daemon is not necessarily running in your machine). These files will be the "build context". Then the Dockerfile will be processed line by line. 

### 2.3 Run a container

You can run the container this way:

	docker run --name containername -d -p 8080:8080 -p 8443:8443 imagetag

## 3 Delivering your application

As we mentioned before, usually the final step of a containerization process is to push a Docker image containg the application to a Docker Registry such as Docker Hub. However, for the PTI lab you need to provide the sources necessaries to build the image:
* The Dockerfile.
* The build context (that may be just one file).

	


