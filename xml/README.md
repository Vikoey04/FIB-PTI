# XML Processing with Java

## 1. Introduction

While nowadays JSON is replacing XML in many data-interchange scenarios, XML is still a broadly used language, with unique features that make it the first choice in some situations. The purpose of this assignment is to learn an easy way to process XML documents with Java. We will use JDOM, a library for enabling rapid development of XML applications. Though it's not necessary, you can take a look to [this article](http://unpetitaccident.com/pub/compeng/languages/JAVA/Tutorials/JDOM/simplify%20XML%20programming%20with%20jdom.pdf) to get more information about JDOM. 

<!-- *NOTE: You can use a different programming language (e.g. Python) and/or different libraries (there are [many for Java](https://en.wikipedia.org/wiki/Java_XML)). In that case, just jump to [Section 3](#3-lab-assignment).*
-->
## 2. Setup

### 2.1 Booting the machine

Select the latest Ubuntu imatge (e.g. Ubuntu 14)

    user: alumne
    pwd: sistemes


### 2.2 Download the example and the libraries

Install git if necessary:

    sudo apt-get install git

Download the sources (if you already have the pti repository, just do a git pull to update it):

    cd $HOME       
    git clone https://gitlab.fib.upc.edu/pti/pti.git

    cd pti/xml
    ls

### 2.3 Set the Java classpath and run the example

Check if java is installed (if not you will have to install it):

    java -version

If not installed do the following:

    apt-get install default-jdk

Set the Java classpath this way:

    export CLASSPATH=./xalan.jar:./xercesImpl.jar:./jdom.jar:.

Now build the example:

    javac Example.java

And run it:

    java Example

## 3 Lab assignment 

You have to program a console application that keeps information about car rentals in an XML file named carrental.xml. The application will accept just one parameter that will specify the action to be performed. After executing one action the application will finish. These are the actions that the application must provide:


### 3.1 reset

Command:

    java CarRental reset

The application will create a new XML document (in memory) with the following structure:
    
    <?xml version="1.0" encoding="UTF-8"?>
    <carrental>
    </carrental>

Or it's equivalent serialization:

    <?xml version="1.0" encoding="UTF-8"?>
    <carrental/>

Once created, the application will save it to a file carrental.xml. If the file already exists, its previous contents will be lost.

### 3.2 new

Command:

    java CarRental new

The application will 1) Ask the user (through the console) the data of a new rental (car model, etc.); 2) Read the carrental.xml XML document into memory; 3) Add a new "rental" element to the document with the following structure :
    
    <?xml version="1.0" encoding="UTF-8"?>
    <carrental>
        <rental id="anyID">
          <model>Basic</model>
          <engine>Electric</engine>
          <nofdays>3</nofdays>
          <nofunits>1</nofunits>
          <discount>15</discount>
        </rental>
    </carrental>

And 4) the application will save new document including the new rental into carrental.xml.

NOTE: ANNEX A contains an example for reading input from the console in Java 

### 3.3 list

Command:

    java CarRental list

The application will read the carrental.xml XML document into memory and print it to the console.

### 3.4 xslt

Command:

    java CarRental xslt

The application will read the carrental.xml XML document into memory, transform it into HTML with an XSLT stylesheet and print it to the console. You can reuse the stylesheet from the example (example.xslt), but you will need to change it.

### 3.5 validate

Command:

    java CarRental validate

The application will read the carrental.xml XML document into memory and validate it againt carrental.xsd.
We will consider the case where the XML document (carrental.xml) contains a reference to the XSD schema. For this we will add the XSD schema reference to the root element of carrental.xml. 
The carrental.xsd file (see annex C) needs to be present in the same folder

    <?xml version="1.0" encoding="UTF-8"?>
    <carrental xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="carrental.xsd">
    </carrental>

We create a SAXBuilder with the arguments XMLReaders.XSDVALIDATING. With the SAXBuilder#build() method, carrental.xml is validated against the internal XSD schema.
Successful validation should show the root element, while if the carrental.xml is not valid, an exception is thrown. 

    SAXBuilder builder = new SAXBuilder(XMLReaders.XSDVALIDATING);
    Document anotherDocument = builder.build(new File("carrental.xml"));
    System.out.println("Root: " + anotherDocument.getRootElement().getName());

### 3.6 xml to json

After you have added the data of a few rentals to your carrental.xml, transform your xml into json.

For this you can leverage the support of an online tool that you may find in Internet to obtain the json for your xml.

In the report that you write about your lab exercise, include the json you obtain, e.g. by taking a sceenshot, and indicate the tool you used (if any).


## ANNEX A. Reading input from console in Java

	public static Element askData() {
		Element carElement = new Element("rental");
		System.out.print("Model:");
		String input = System.console().readLine();
		Element model = new Element("model");
		make.addContent(input);
		carElement.addContent(model);
		...
		return carElement;
   	 }	

## ANNEX B. XSLT apply-templates

In order to create an XSLT stylesheet that processes N subelements you can use the apply-templates function: 
	
	<xsl:template match="/carrental">
		<html>
			<head><title>RENTALS</title></head>
			<body>
				<xsl:apply-templates select="rental"/>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="rental">
		<table border="0">
			<h1>MAKE=<xsl:value-of select="model"/></h1><br />
			...
		</table>		
	</xsl:template>


## ANNEX C. XSD schema

An XSD Schema can be used to validate an XML document. The XSD describes the elements, attributes and types, which are allowed in the XML and in which order.
	
	<?xml version="1.0" encoding="utf-8"?>
    <xs:schema xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" attributeFormDefault="unqualified" elementFormDefault="qualified" xmlns:xs="http://www.w3.org/2001/XMLSchema">
    <xs:element name="carrental">
        <xs:complexType>
            <xs:sequence>
                <xs:element maxOccurs="unbounded" name="rental">
                    <xs:complexType>
                        <xs:sequence>
                            <xs:element name="model" type="xs:string" />
                            ...
                            <xs:element name="nofdays" type="xs:unsignedByte" />
                            ...
                        </xs:sequence>
                        <xs:attribute name="id" type="xs:unsignedInt" use="required" />
                    </xs:complexType>
                </xs:element>
            </xs:sequence>
         </xs:complexType>
        </xs:element>
    </xs:schema>
  

## ANNEX D. Work from a Docker container

Install Docker 

    sudo apt install docker.io

Launch the container:

    sudo docker run --name=pti -it ubuntu:18.04

Within the container:

    apt-get update
    apt-get install git
    apt-get install default-jdk




