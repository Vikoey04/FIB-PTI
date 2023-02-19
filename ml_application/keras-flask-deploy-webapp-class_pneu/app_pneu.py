import os
import sys

# Flask
from flask import Flask, redirect, url_for, request, render_template, Response, jsonify, redirect
from werkzeug.utils import secure_filename
from gevent.pywsgi import WSGIServer

# TensorFlow and tf.keras
import tensorflow as tf
from tensorflow import keras

import tensorflow_datasets as tfds

from tensorflow.keras.applications.imagenet_utils import preprocess_input, decode_predictions
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image

# Some utilites
import numpy as np
from util import base64_to_pil


# Declare a flask app
app = Flask(__name__)


# You can use pretrained model from Keras
# Check https://keras.io/applications/
# or https://www.tensorflow.org/api_docs/python/tf/keras/applications


#from tensorflow.keras.applications.mobilenet_v2 import MobileNetV2
#model = MobileNetV2(weights='imagenet')

#print('Model loaded. Check http://127.0.0.1:5000/')


# Model saved with Keras model.save()
#MODEL_PATH = 'models/your_model.h5'
MODEL_PATH = './keras_model_trained'
#MODEL_PATH ='./keras_model_untrained'
#MODEL_PATH = '<absolute_path_to_your_model>'

# Load your own trained model
# model = load_model(MODEL_PATH)
# model._make_predict_function()          # Necessary
# print('Model loaded. Start serving...')

#model = keras.models.load_model('path/to/location')
model = keras.models.load_model(MODEL_PATH)

print('Model loaded. Start serving...')




@app.route('/', methods=['GET'])
def index():
    # Main page
    return render_template('index.html')


@app.route('/predict', methods=['GET', 'POST'])
def predict():
    if request.method == 'POST':
        # Get the image from post request
        image = base64_to_pil(request.json)

        print('Input image shape:')
        input_arr = tf.keras.preprocessing.image.img_to_array(image)
        print(input_arr.shape)

        # Save the image to ./uploads
        image.save("./uploads/image.png")
        print('Image saved')

        img = tf.io.read_file("./uploads/image.png")
 
        tensor = tf.io.decode_image(img, channels=3, dtype=tf.dtypes.float32)
        tensor = tf.image.resize(tensor, [224, 224])
        tensor = np.array([tensor])  # Convert single image to a batch.
        
        model.summary()
        
        # Make prediction
         
        preds = model.predict(tensor, batch_size=1, verbose=2)
        
        #print(preds.shape)
        print(preds)   # probablity for the two classes
        print(preds[0,0])
        print(preds[0,1])
        
        # Process your result for human
        pred_proba = "{:.3f}".format(np.amax(preds))    # Max probability
        print("max. probability " + pred_proba)
        
        if preds[0,0] > preds[0,1]:
        	result="pneumonia"
        else:
        	result="normal"
        print('Classification result: ' + result)
        
        # Serialize the result, you can add additional fields
        return jsonify(result=result, probability=pred_proba)

    return None


if __name__ == '__main__':
    # app.run(port=5002, threaded=False)

    # Serve the app with gevent
    http_server = WSGIServer(('0.0.0.0', 5000), app)
    http_server.serve_forever()
