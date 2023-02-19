from os import environ

DEFAULT_SERVER_URL = 'http://127.0.0.1:5000'

USE_TRAINED_MODEL = False # False: start training from scratch. True: use previously trained model

PRETRAINED_MODEL_PATH = '/keras_model_trained'
#PRETRAINED_MODEL_PATH = '<absolute_path>/keras_model'

GLOBAL_DATASETS = '/database' # dataset folder
#GLOBAL_DATASETS = '<absolute_path>/database'

if environ.get('CLIENT_URL') == 'http://localhost:5001':
    print("I'm client 1")
    GLOBAL_TMP_PATH = '/tmp1'
    INITIAL_MODEL_PATH = '/keras_model1'
    #INITIAL_MODEL_PATH = '<absolute_path>/keras_model1'    
elif environ.get('CLIENT_URL') == 'http://localhost:5002':
    print("I'm client 2")
    GLOBAL_TMP_PATH = '/tmp2'
    INITIAL_MODEL_PATH = '/keras_model2'
    #INITIAL_MODEL_PATH = '<absolute_path>/keras_model2' 
elif environ.get('CLIENT_URL') == 'http://localhost:5003':
    print("I'm client 3")
    GLOBAL_TMP_PATH = '/tmp3'
    INITIAL_MODEL_PATH = '/keras_model3'
    #INITIAL_MODEL_PATH = '<absolute_path>/keras_model3'
else:
    GLOBAL_DATASETS = '/database'
    USE_TRAINED_MODEL = False
    

