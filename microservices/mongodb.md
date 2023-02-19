# Connecting to a MongoDB database within the same Pod

We are going to do something that makes no sense in a real scenario, connecting to a MongoDB running in the same Pod (so the data can be lost anytime). 

*NOTE: We will not create a PersistentVolume to persist our data. This will be also a problem anyway, because if the Pod gets replicated and two MongoDB instances try accessing the same data one of them will crash. Maybe a "better" alternative would be accessing a database external to Kubernetes, but this would involve some complexity that we want to avoid here. Nowadays, MongoDB provides help to run it in a proper way within Kubernetes, but that is out of the scope of the course.* 

First, let's replace the contents of our "server.js" with the following:
```
var http = require('http');
var MongoClient = require('mongodb').MongoClient;
var client = MongoClient.connect('mongodb://127.0.0.1:27017/test', function(err, client) {
    console.log("connected to the mongoDB !");
	var handleRequest = function(request, response) {
		response.writeHead(200, {'Content-Type': 'text/html'});
	    var db = client.db('test_database');
	    var myCollection = db.collection('test_collection')
	    var time = new Date().toISOString().match(/(\d{2}:){2}\d{2}/)[0]
	    myCollection.insert({name: "access", description: time}, function(err, result) {
		    var cursor = myCollection.find({name : "access"});
			cursor.each(function(err, doc) {
			    if(doc==null) {
			    	response.end();
			        return;
			    }
			    response.write("Accessed "+doc.description+"<br />");
			});
		});
	};
	var www = http.createServer(handleRequest);
	www.listen(8080);
});
```
And also change the Dockerfile this way:
```
vi Dockerfile

FROM node:6.14.2
RUN npm install mongodb
EXPOSE 8080
COPY server.js .
CMD node server.js

```

Let's rebuild the Docker image this way:

	eval $(minikube docker-env)
	docker build -f Dockerfile -t helloworld:2.0 .

Let's change the image in Kubernetes:

	kubectl set image deployment/helloworld helloworld=helloworld:2.0

You don't need to re-recreate the Service, but check if the NODE_PORT changed:

	kubectl describe service helloworld

To add a container with mongodb to your deployment edit a file named "deployment_with_mongodb.yaml" with these contents:

```
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: helloworld
spec:
  template:
    spec:
      containers:
      - name: helloworld
        image: helloworld:2.0
        ports:
           - containerPort: 8080
      - name: mongodb
        image: mongo
        ports:
           - containerPort: 27017
```

And apply the changes to the deployment this way:

	kubectl apply -f deployment_with_mongodb.yaml

Check if it worked with:

	curl $(sudo minikube ip):NODE_PORT 