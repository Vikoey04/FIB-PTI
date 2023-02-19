# Kubernetes' PersistentVolume

In a production cluster, the cluster administrator would provision a network resource to store files like a Google Compute Engine persistent disk, NFS or similar. For our Minikube cluster, we can use a "hostPath" PersistentVolume, that lets use create a PersistenVolume over a directory of the filesystem of the only node in the cluster. 

Let's edit the following configuration in a file named pv-volume.yaml:
```
kind: PersistentVolume
apiVersion: v1
metadata:
  name: task-pv-volume
  labels:
    type: local
spec:
  storageClassName: manual
  capacity:
    storage: 10Gi
  accessModes:
    - ReadWriteOnce
  hostPath:
    path: "/mnt/data" 
```

Let's now create the PV:

	kubectl apply -f pv-volume.yaml

View information about the PersistentVolume:

	kubectl get pv task-pv-volume

In order to let our Pods to access the PV we need to create a PersistentVolumeClaim (PVC).

Let's edit the following configuration in a file named pv-claim.yaml:

```
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: task-pv-claim
spec:
  storageClassName: manual
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 3Gi
```

Create the PersistentVolumeClaim:

	kubectl apply -f pv-claim.yaml

Look again at the PersistentVolume to see that the PVC has been bined to it (STATUS = Bound):

	kubectl get pv task-pv-volume

Now edit a file deployment_persistentvolume.yaml with the following content:

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
        image: helloworld
        volumeMounts:
        - mountPath: /mnt/data
          name: task-pv-volume
      volumes:
      - name: task-pv-volume
        persistentVolumeClaim:
          claimName: task-pv-claim
```

We can replace the Deployment configuration of our microservice with this one doing the following:

		kubectl apply -f deployment_persistentvolume.yaml