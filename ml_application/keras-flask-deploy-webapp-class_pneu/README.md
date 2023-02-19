# Application prototype

## 1 Introduction

In this part II of the lab we will see the integration of a machine learning model in a Web-based application prototype. 

## 2 Preparing the environment

Go to the subfolder 

    cd keras-flask-deploy-webapp-class_pneu/

Python3.8 or higher is needed. Check if you have the right version or install it if needed.

    python3 --version

We will work with a virtual Python environment. Being in the folder `keras-flask-deploy-webapp-class_pneu`, create the virtual environment called practfl (it will create a subfolder with this name in your local directory):

    python3 -m venv practfl 

Activate the virtual environment. 

    source practfl/bin/activate

The existing code needs several packages to be installed. The packages are listed in the file requirements.txt. We will install the packages with pip3 (if pip3 is not available in your system, you will need to install it)

    pip3 install -r requirements.txt


## 3 Running the medical doctor application prototype

As commented before, we apply the code from this [github repository](https://github.com/imfing/keras-flask-deploy-webapp), however we have made a few changes in order to be able to work with our trained machine learning model and the x-ray dataset. 

The application can use the machine learning model of the type we trained in part I of the lab.

The file `app_pneu.py` is the main applications. In `app_pneu.py` the path where the model is found has to be configured. The application can be tested with a _trained_ or _untrained_ model. The expectation is that the trained model mostly classifies correctly the images while the untrained does not. Example models are provided for both cases. One of the paths must be uncommented.

`MODEL_PATH = './keras_model_trained`. Uncomment this path when you want to test with the trained model. 

`MODEL_PATH ='./keras_model_untrained`. To test the untrained model uncomment or can change that to: MODEL_PATH ='./keras_model_untrained.

You can add a new folder location MODEL_PATH ='<path_to_your_trained_model>' for the model _you_ trained in part I of the lab to see how it performs.

Run the application:

    python3 app_pneu.py

Open http://localhost:5000.  

You can see a Web interface that let's you drop images. For trying you can take images from the folder `DATASET_EXTRACT` or take images from the training or test folder of the CHEST_X_RAY dataset that you used in part I. Go to the NORMAL and PNEUMONIA folders and check if the applications classifies the x-ray images correctly. 

<!--!![classification](/uploads/80ce379ffbdd25883c5c7342c798974f/classification.png)-->

<p align="center">
<img src="/uploads/5da1a1350fe154a16697428d026a1d24/classification.png" width="600">
</p>


## Lab assignment part II 

1. In order to demonstrate the previous steps:
Take a screenshot of the console where you run `app_pneu.py` after having classified an image.


## Lab assignment global
See the README of the lab folder.
