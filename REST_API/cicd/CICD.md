# CI/CD (lab extension)

*NOTE: This is an extension to the [REST_API lab](../REST_API/README.md). The extension requires that the application has been dockerized.*

## 1. Introduction

CI/CD stands for Continuous Integration, Continuous Delivery and Continuous Deployment. It is a set of practices that aims to automate the building, testing and deployment of software applications. CI/CD is the backbone of a DevOps methodology, bringing developers and IT operations teams together to deploy software. **Continuous Integration** refers to the process of automatically building, testing, and integrating code changes into a shared repository. **Continuous Delivery** refers to making code changes available for release (e.g. by uploading the application to a container registry). **Continuous Deployment** refers to automatically releasing code changes to production (e.g. deploying a new application version to a Kubernetes cluster). 

<p align="center">
  <img src="ci-cd-flow-desktop.webp" width="400">
</p>

There are multiple tools to implement these practices (e.g. Jenkins). Here, for convenience, we will only use some of GitLab's CI/CD functionality.

## 2. Tasks

We are going to carry out a small test to get an idea of ​​the type of tasks involved in CI/CD. In summary, we will perform the following tasks:

1) Create a repository for the carrental REST API in GitLab.
2) Install a GitLab Runner in your machine, register it and run it. 
3) Create a .gitlab-ci.yml file at the root of your repository. This file is where you define the CI/CD jobs. 
4) Modify the .gitlab-ci.yml file to achieve that every time there is a commit, the Docker image is (1) rebuilt and (2) uploaded to a container registry.

## 3. Help step by step

*NOTE: You cand find more information about GitLab CI/CD [here](https://docs.gitlab.com/ee/ci/)*

### 3.1 Create a repository for the carrental REST API in GitLab

*NOTE: A minimal knowledge about git is assumed here. Ask for help otherwise.* 

1) Sign in into repo.fib.upc.es and create a repository for the carrental REST API. 

2) Copy the files from the REST API lab into the root of the repository (including the Dockerfile). The repo should have the following structure:

```
	myapp
		Dockerfile
		server.js
		package.json
```
3) Add a .gitignore at the root of the repo with this content to avoid uploading to the repo the application dependencies:
```
	node_modules/
```
4) Commit and push the changes. 

### 3.2 Installing, registering and running a GitLab Runner

GitLab CI/CD tasks are executed by an application called [GitLab Runner](https://docs.gitlab.com/runner/). The runner can be hosted in GitLab servers but here, for convenience, you will run it your machine. 

1) Install a GitLab Runner in your machine following the instructions in: 

	Settings > CI / CD > Runners > Show runner installation instructions

2) Register the runner for your project (select "shell" as executor):

	gitlab-runner register --url https://repo.fib.upc.es/ --registration-token $REGISTRATION_TOKEN

*NOTE: Obtain the $REGISTRATION_TOKEN from Settings > CI / CD > Runners*

3) Run the runner in a separate Terminal:

	gitlab-runner run

4) Check that the runner status in Settings > CI / CD > Runners

### 3.3 Define a CI/CD pipeline

CI/CD tasks are often grouped around the concept of a [pipeline](https://docs.gitlab.com/ee/ci/pipelines/index.html). A pipeline is a specification of CI/CD tasks (jobs) structured in stages. You do that by including a .gitlab-ci.yml file in the root of your project repo. 

1) Create a test .gitlab-ci.yml file with the following content:

```
	test:
	  script:
	    - echo "Hello, $GITLAB_USER_LOGIN!" 
```

Commit and push the change to the repo. This will trigger the pipeline. Check the result of the pipeline in CI / CD > Pipelines. Click the pipeline number, then click over the "test" job. You should see the output. Check the result of the pipeline in CI / CD > Pipelines. 

2) Modify .gitlab-ci.yml to automate the building of the Docker image:

```
	build:
	  script:
	      - docker build  -t carrental myapp
	test:
	  script:
	    - docker run --name carrental -d -p 8080:8080 -p 8443:8443 carrental
	    - sleep 1
	    - curl --request GET  "localhost:8080"
	    - docker stop carrental
	    - docker rm carrental
```

Commit and push the change to the repo. Check the result of the different jobs of the pipeline. 

*NOTE: If the pipeline fails, you would need to stop and remove the container manually before testing it again.*

3) Push the Docker image to a registry

Ideally you would push the image to the GitLab Container Registry but it's not enabled for the FIB's GitLab. Let's try it by launching a local registry. In the terminal type:

	docker run -d -p 5000:5000 --restart=always --name registry registry:2

*NOTE: See [this](https://docs.docker.com/registry/deploying/) for more information about launching a registry server.*

Now change .gitlab-ci.yml this way:

```
	build:
  script:
      - docker build  -t carrental myapp
	test:
	  script:
	    - docker run --name carrental -d -p 8080:8080 -p 8443:8443 carrental
	    - sleep 1
	    - curl --request GET  "localhost:8080"
	    - docker stop carrental
	    - docker rm carrental

	release-image:
	  script:
	      - docker tag carrental:latest localhost:5000/carrental 
	      - docker push localhost:5000/carrental
	      - docker image remove localhost:5000/carrental
	      - docker pull localhost:5000/carrental
```
Commit, push and check the output.

[Tagging](https://docs.docker.com/engine/reference/commandline/tag/) the image as localhost:5000/carrental is required to be able to push it to your local private registry. 
