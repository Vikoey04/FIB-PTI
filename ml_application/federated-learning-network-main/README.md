# Training a machine learning model with federated learning

## 1 Introduction

In this part I of the lab we will see the training of a deep machine learning model by means of a distributed application which implements the technique of [federated learning](https://en.wikipedia.org/wiki/Federated_learning).

## 2 Preparing the environment

Python3.8 or higher is needed. Check if you have the right version or install it if needed.

    python3 --version

Download the code as a zip file, unzip it and you should obtain a base folder called `federated-learning-network-main` and two subfolders called `server` and `client`. Open a terminal.

    cd federated-learning-network-main

We will work with a virtual Python environment. Being in the folder `federated-learning-network-main`, create the virtual environment called `practfl` (it will create a subfolder with the same name in your local directory):

    python3 -m venv practfl 

Activate the virtual environment. 

    source practfl/bin/activate

After activating you will see (practfl) added to your current command line in your terminal.

The existing code needs several packages to be installed. One of them is Tensorflow, a machine learning framework. The packages are listed in the file requirements.txt. We will install the packages with pip3 (if pip3 is not available in your system, you will need to install it)

    pip3 install -r requirements.txt

## 3 Training the machine learning model 

The federated learning network consists of distributed nodes that communicate with each other messages related to the machine learning model training process. The implementation we use can run on any networked Linux-based devices. At the application level there are two types of nodes: 1) The aggregator or server node: this node orchestrates the training process. There is exactly one node of this type. 
2) The worker or client node: they train the machine learning model with local data. There can be one or more of this type of nodes.

The nodes will communicate with each other. In the implmentation we work with the communication is done over http.
Each node runs a Flask server. Furthermore, each node applies a http client in order to communicate with other nodes.

### 3.1 Dataset
The existing implementation works with two datasets for training: MNIST and Chest X-Ray. In this lab we will work only with the Chest X-Ray. You have to download the Chest X-Ray dataset.

Download the Chest X-Ray dataset from from https://data.mendeley.com/public-files/datasets/rscbjbr9sj/files/f12eaf6d-6023-432f-acc9-80c9d7393433/file_downloaded and uncompress it in a folder called `chest_xray`. 
The final structure of the dataset folders must be (other content in this folder will be ignored):

    chest_xray
         |----- train
         |        |----- NORMAL
         |        |----- PNEUMONIA
         |
         |----- test
                  |----- NORMAL
                  |----- PNEUMONIA

By default, the client node looks for it at GLOBAL_DATASETS/chest_xray. It is suggested to create **within the subfolder `client`** the folder `database` and there unzip the dataset. The path to one of the images subfolder can look like `federated-learning-network-main/client/database/chest_xray/train/NORMAL`. 
The variable GLOBAL_DATASETS is defined in the configuration file `client/config.py`.
Update or check the value of GLOBAL_DATASETS to where you installed the dataset. You may put the absolute path to the dataset if you prefer. 

### 3.2 Running the implementation 

As said before, federated learning runs with distributed nodes. However, within the scope of this lab we will run all nodes on a local machine and give a different port to each node.

We will train with one server and three clients. Therefore, open 4 terminals for running these four nodes.

From the base directory federated-learning-network-main, activate the virtual environment in each of the four terminals

    source practfl/bin/activate

#### 3.2.1 Starting the central node (server) 

In one of the terminals go to `federated-learning-network/server` and execute:

    cd server
    
    flask run

It will start a central node in `http://localhost:5000`. To see that the server runs, open a browser and go to that URL.
You'll see the dashboard of the network.

#### 3.2.2 Starting the clients 

Go to the client folder

    cd client

Check that there are the folders `tmp1`, `tmp2` and `tmp3` (where the three clients read their local dataset). If any of the three folders is missing, create it.    

In one of the other terminals go to `federated-learning-network/client` and execute:
    
    export CLIENT_URL='http://localhost:5001'
    flask run --port 5001
    
Do that for the other two clients in the remaining terminals, changing the listening port. 

    export CLIENT_URL='http://localhost:5002'
    flask run --port 5002

    export CLIENT_URL='http://localhost:5003'
    flask run --port 5003

You'll see some log traces telling the client has started and has registered in the network:

    Registering in server: http://127.0.0.1:5000
    Doing request http://127.0.0.1:5000/client
    Response received from registration: <Response [201]>
    Client registered successfully

If you refresh the central node's dashboard in `http://localhost:5000` you can see the three clients registered in the network.

<!--![FL_dashboard](/uploads/9bc441b8fdba94ca55449f74e3a29155/FL_dashboard.png) -->

<p align="center">
<img src="/uploads/8e974ce9c5771b9aa6eb2c4f25550b00/FL_dashboard.png" width="800">
</p>


## Lab assignment part Ia

In order to demonstrate the previous steps:
1. Take a screenshot of the server console after the three clients have registed (before having run a training round).
ing done a training round. 
2. Take a screenshot of any of the client console after having registered at the server (before having run a training round).

#### 3.2.3 Training sessions

Now we want to run a training round. Once we have the central node and clients running properly and registered, open the server dashboard and click on the _Launch training_ button. Click on CHEST_X_RAY training (do NOT click on MNIST). This will tell the server to start a training round with the CHEST_X_RAY training data.

The server will launch a training round between all the registered clients. You can see the progress of the training in each client's console. For example, for CHEST_X_RAY when the client receives the POST request to start training you will see something like this in the client node console:
    
    Request POST /training for training type: CHEST_X_RAY_PNEUMONIA
    No weights found in the request
    Federated Learning config:
    --Learning Rate: 0.0001
    --Epochs: 1
    --Batch size: 2

    Initializing ChestXRayModelTrainer...
    Training started...
    ....
    Sending calculated model weights to central node
    Response received from updating central model params: <Response [200]>
    Model params updated on central successfully
    Training finished...

In the client console the accuracy of the model during the training is shown, something like. 

    10/10 - 5s - loss: 0.8821 - accuracy: 0.8500 - val_loss: 1.9336 - val_accuracy: 0.8000 - 5s/epoch - 501ms/step
    ...

You can do more training rounds afterwards by just clicking again on _Launch training_ and CHEST_X_RAY and see how or if the model improves (increasing the accuracy). 


Note: if any error appears in the clients console, abort them with Control+C and do a complete re-start of both _server and clients_ to try again.


## Lab assignment part Ib 

In order to demonstrate the previous steps:
3. Take a screenshot of the server console (showning several lines) after having done a training round.
4. Take a screenshot of any of the client console (showning several lines) after having having done a training round.

#### 3.2.4 Saving the trained model

After doing each training round, the code saves the trained model. This is important for our purpose since we want to use the trained model in the medical doctor application (part II of this lab) to classify x-ray images. The implementation already saves the model (in the file `chest_x_ray_model_trainer.py`)  with `model.save(INITIAL_MODEL_PATH)`. However, we should check the value of INITIAL_MODEL_PATH in the configuration file `client/config.py` to make sure that the folder exists and is found by the code.

The `client/config.py` determines the folders where the application finds the training data and models. 
Also, two different situations can be created. 
`USE_TRAINED_MODEL = False`  makes that the application will train a new model from scratch. While if set True it will continue training an already existing model.

## 4 Using the trained model in the medical doctor application

After having saved the trained model, we can use it in part II in the application prototype. Go to the folder `keras-flask-deploy-webapp-class_pneu`. You will integrate there your trained model in the prototype application. Stop the server running at port 5000 since the same port is used in part II by the application.

#### Additional explanations about implementation details 

The machine learning model is defined in the file `chest_x_ray_model_trainer.py` within the `client` folder. Is applies the Sequential class of the Tensorflow/Keras framework to define a deep neural network model.
Some of the training parameters are defined in the `server` folder in the file `federated_learning_config.py` and the values are used for doing the training of the model in `chest_x_ray_model_trainer.py`.
The instance of a client with the Flask server is created in the file `app.py` within the `client` folder.
The instance of the server with the Flask server is created in the file `__init__.py` within the `server` folder.




