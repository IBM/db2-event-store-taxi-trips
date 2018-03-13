# Analyze event streams with taxi cab data

In this code pattern, we will stream data from a Java program and use a Jupyter notebook to
demonstrate charting of statistics based on historical and live events. IBM Db2 Event Store
is used as the event database.

IBM® Db2 Event Store (formerly IBM Project EventStore) is an in-memory database designed for massive structured data volumes and real-time analytics built on Apache SPARK and Apache Parquet Data Format. The solution is optimized for event-driven data processing and analysis. It can support emerging applications that are driven by events such as IoT solutions, payments, logistics and web commerce. It is flexible, scalable and can adapt quickly to your changing business needs over time. Available in a free developer edition and an enterprise edition that you can download now. The enterprise edition is free for pre-production and test.

Credit goes to Jacques Roy for the original Java code and Jupyter notebook.

When the reader has completed this code pattern, they will understand how to:
* Install IBM Db2 Event Store developer edition
* Interact with Db2 Event Store through a notebook
* Use a Java program to insert into IBM Db2 Event Store
* Query the database while inserts are in progress
* Use the asynchronous API to insert into a Db2 Event Store table

![](doc/source/images/architecture.png)

## Flow
1. User runs Jupyter notebook in DSX Local
2. Notebook connects to Db2 Event Store to analyze live event stream
3. External Java program sends live events

## Included components
* [IBM Db2 Event Store](https://www.ibm.com/us-en/marketplace/db2-event-store): In-memory database optimized for event-driven data processing and analysis.
* [IBM Data Science Experience](https://www.ibm.com/bs-en/marketplace/data-science-experience): Analyze data using RStudio, Jupyter, and Python in a configured, collaborative environment that includes IBM value-adds, such as managed Spark.
* [Jupyter Notebook](http://jupyter.org/): An open source web application that allows you to create and share documents that contain live code, equations, visualizations, and explanatory text.
* [Python](https://www.python.org/): Python is a programming language that lets you work more quickly and integrate your systems more effectively.
* [Java](https://java.com/): A secure, object-oriented programming language for creating applications.

## Featured technologies
* [Databases](https://en.wikipedia.org/wiki/IBM_Information_Management_System#.22Full_Function.22_databases): Repository for storing and managing collections of data.
* [Analytics](https://developer.ibm.com/watson/): Analytics delivers the value of data for the enterprise.
* [Data Science](https://medium.com/ibm-data-science-experience/): Systems and scientific methods to analyze structured and unstructured data in order to extract knowledge and insights.

# Steps

## Run locally

1. [Install IBM Db2 Event Store Developer Edition](#1-install-ibm-db2-event-store-developer-edition)
1. [Clone the repo](#2-clone-the-repo)
1. [Build the Java event loader](#3-build-the-java-event-loader)
1. [Create the Jupyter notebook in DSX Local](#4-create-the-jupyter-notebook-in-dsx-Local)
1. [Run the notebook and the loader](#5-run-the-notebook-and-the-loader)

### 1. Install IBM Db2 Event Store Developer Edition

Install IBM® Db2® Event Store Developer Edition on Mac, Linux, or Windows by following the instructions [here.](https://www.ibm.com/support/knowledgecenter/en/SSGNPV/eventstore/desktop/install.html)

> Note: This code pattern was developed with EventStore-DeveloperEdition 1.1.4

### 2. Clone the repo

Clone the `db2-event-store-taxi-trips` locally. In a terminal, run:

```
git clone https://github.com/IBM/db2-event-store-taxi-trips
```

### 3. Build the Java event loader

#### Pre-requisite

Maven >= 3.5 is used to build, test, and run. Check your maven version using the following command:

```
mvn -v
```

To download and install maven, refer to [maven.](https://maven.apache.org/download.cgi)

#### Build the event loader
Use maven to download dependencies and compile the Java code with the following commands:

```
cd db2-event-store-taxi-trips
mvn clean
mvn install
mvn compile
```

### 4. Create the Jupyter notebook

> Note: Db2 Event Store is built with Data Science Experience (DSX) Local

We access Db2 Event Store through a new notebook.
We setup the environment that will allow us to create a database and the appropriate table
for this code pattern.

#### Importing a Notebook
We need to import a notebook in our environment.
* From the drop down menu (three horizontal lines in the upper left corner), select My Notebooks.
* Click on "add notebook"
* Provide a name
* Select "From File" 
* Click “Choose File”, navigate to the lab directory and select the file Lab+Notebook.ipynb.
* Scroll down and click on “Create Notebook”
The new notebook is now open and ready for execution.

### 5. Run the notebook and the loader

> TODO:  Getting the IP Addr

#### Run the notebook with Run all Cells

> TODO: Screen shots and usual comments about running a notebook

#### Run the event loader
```
mvn exec:java -Dexec.mainClass=com.ibm.developer.code.patterns.db2eventstoretaxitrips.WriteES -Dexec.args="0.0.0.0 50 data/Taxi50k.json"
```

# Sample output

> TODO: screenshots, etc...

# Links
* 
*
*
*

# Learn more
* **Data Analytics Code Patterns**: Enjoyed this Code Pattern? Check out our other [Data Analytics Code Patterns](https://developer.ibm.com/code/technologies/data-science/)
* **AI and Data Code Pattern Playlist**: Bookmark our [playlist](https://www.youtube.com/playlist?list=PLzUbsvIyrNfknNewObx5N7uGZ5FKH0Fde) with all of our Code Pattern videos
* **Data Science Experience**: Master the art of data science with IBM's [Data Science Experience](https://datascience.ibm.com/)

# License
[Apache 2.0](LICENSE)
