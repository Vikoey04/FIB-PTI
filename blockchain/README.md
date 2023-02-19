# Blockchain


## 1 Introduction

Note: taken from: [Learn Blockchains by Building One](https://hackernoon.com/learn-blockchains-by-building-one-117428612f46). Check the corresponding [github repository](https://github.com/dvf/blockchain) for the installation of the Python environment.

In this part we will explore some concepts of a blockchain with a simplifed implementation in Python.

The blockchain is operated through a Web API. We need therefore an HTTP client to perform the operations. We will use curl (or alternatively you may use Postman).

## 2 Tasks

### 2.1 Install the environment

You will need python3.6 or higher. Check if it is installed and install it if needed.

    python3 --version

Depending on your environment, you may need to apply the following steps:

    sudo add-apt-repository ppa:deadsnakes/ppa
    sudo apt-get update
    sudo apt-get install python3.6
    sudo apt-get install python3-pip

And to install the packages
<!-- python3.6 -m pip install Flask==0.12.2 requests==2.18.4 -->

    python3.6 -m pip install Flask requests 

    python3.6 -m pip install Werkzeug==0.16

To run python programms: 
    
    python3.6 programa.py

Download the code.
Click on blockchain.tar.gz in the repositorie's folder or download the repository with

    git clone https://gitlab.fib.upc.edu/pti/pti.git

Open the blockchain.tar.gz and test if it runs correctly (or with python3.6 explicitely)

    python blockchain.py -p 5000   /    python3.6 blockchain.py -p 5000

### 2.2  Test the blockchain

In our experiment we will use two nodes (node 1 runs at port 5000 and node 2 runs at port 5001) to participate in the blockchain. 
With an HTTP client, we will make the operations on the two nodes. 
We will write transactions into the blockchain of the nodes and mine blocks. In the mining, the Proof-of-Work (PoW) is computed. 
We will then finalize the consensus between the two nodes so that they agree on one of the two chains. 

When these steps work correctly, we will finally add a few more funtions to the code.

(Note: Once you have tried the operations individually, you may run the following steps with a script)

    #Run the two nodes
    pkill python
    python blockchain.py -p 5000 &
    python blockchain.py -p 5001 &

    sleep 3

    #Write two transactions into node 1
    curl -X POST -H "Content-Type: application/json" -d '{"sender": "A","recipient": "B", "amount": 8, "order": 1  }' http://localhost:5000/transactions/new
    curl -X POST -H "Content-Type: application/json" -d '{"sender": "B","recipient": "C", "amount": 5, "order": 2  }' http://localhost:5000/transactions/new

    #Mine block at node 1 
    curl http://localhost:5000/mine

    #See chain at node 1 
    curl http://localhost:5000/chain

    #Mine another block at node 1 
    curl http://localhost:5000/mine

    #See chain at node 1 
    curl http://localhost:5000/chain

    #Mine another block at node 1 
    curl http://localhost:5000/mine

    #See chain at node 1 
    curl http://localhost:5000/chain

    # At this point, node 1 should have three blocks.
    # We will operate now on node 2
    #Mine block at node 2 
    curl http://localhost:5001/mine

    #Mine another block at node 2
    curl http://localhost:5001/mine

    #See chain at node 2 
    curl http://localhost:5001/chain

    # At this point, node 2 should have two blocks.
    # We will now register the nodes to each other: Node 1 will know node 2 and node 2 will know node 1.
    #Register nodes in node 1
    curl -X POST -H "Content-Type: application/json" -d '{"nodes":"http://localhost:5001"}' http://localhost:5000/nodes/register

    #Register nodes in node 2
    curl -X POST -H "Content-Type: application/json" -d '{"nodes":"http://localhost:5000"}' http://localhost:5001/nodes/register

    # At this point, we want to synchronize the two chains (of node 1 and node 2). 
    #The criteria is that the agreed common chain for both nodes is the one which is longer (i.e. has more blocks) and is valid (i.e. the chain of hashes is correct).
    #node 1 will synchronize its chain 
    curl http://localhost:5000/nodes/resolve

    #node 2 will synchronize its chain 
    curl http://localhost:5001/nodes/resolve

### 2.3  Add functions to the blockchain

After having done he above steps, we will add a few more functions to the Web API of the code:

    @app.route('/nodes/list', methods=['GET'])
This method should allow to see the other nodes that are registered at a node.
Test it e.g. with curl http://localhost:5001/nodes/list

    @app.route('/validate', methods=['GET'])
This method should allow to validate the chain of hashes of the blockchain stored in a node.
Test it e.g. with curl http://localhost:5001/validate

    @app.route('/nodes/manipulate', methods=['POST'])
This method should manipulate the chain of a node, so when validated, it will become incorrect.

Test the new operations with your client.

### 2.4 Additional tasks to be answered in the report

1) Think of an alternative to the used proof-of-work. 
What options have you found/considered?
Can this alternative be integrated in our code?
If possible, make the modification needed for this alternative and test it.

2) In our exercise, "everybody" can write a new transaction. Think about mechanisms to protect the execution of the transaction method (to answer in the report).

3) Does our excercise store a state in the blockchain, e.g the amounts A and B have after having done a transaction? If not, how would you add it to the code (to answer in the report).

### 2.5 Documenting

In your report, document the results and interpret the operations that you tested with the code.

