# Machine learning-based application

## Introduction

Scenario: In this lab we want to test a prototype of a machine learning-based application that helps a medical doctor to assess the x-ray image of a patient for determining whether she has peneumonia or not. 

The laboratory consists of two parts: 
1) Run the training of a machine learning model. We will use an existing implementation which trains a deep machine learning model with [federated learning](https://en.wikipedia.org/wiki/Federated_learning). 
2) Run the prototype application.

Note: The first part is based on the federated learning network in this [github repository](https://github.com/eyp/federated-learning-network).
The second part is based on this [github repository](https://github.com/imfing/keras-flask-deploy-webapp).

For doing part I, go to the subfolder `federated-learning-network-main`.

For doing part II, go to the subfolder `keras-flask-deploy-webapp-class_pneu`.

The connection between the first and the second part of the lab consists in the fact that in the second part we will use the machine learning model trained in the first part. However, for the second part default machine learning models are provided, so both parts can be done independently.

The result of the lab is the acquisition of an initial experience on how machine learning training can be performed by a distributed application and how a machine learning model can be used in a prototype application. 


## Lab assignment 

1. It is expected to do part I and part II. The successful execution of the code has to be demonstrated with screenshots complemented by the reporting of the experience. See the details on exactly what screenshots are expected in the README in the subfolders for part I and part II.

2. Extension: Choose on aspect/topic of the lab (either of part I or part II, as you prefer) to look into in more detail.
The topic/aspect is _at your choice_. 
It can be "theoretical" or "practical". Theoretical, e.g. by analyzing more deeply something of the system like the protocol of interactions ..., or practical, e.g. by implementing a small extension. 
If you add any extension (either to the code of the federated learning network (part I) or medical doctor application (part II), you can choose what extension you want to develop but for your report explain or demonstrate (e.g. with screenshot) the result of the implementation and argue the benefit of the extension you implemented.



