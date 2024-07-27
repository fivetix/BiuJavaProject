# Configuration File Upload and Graph Generation Server

## Background

In many data-driven applications, visualizing configuration data through graphs can provide valuable insights. This project aims to create a simple yet effective tool for uploading configuration files, processing them to generate graphs, and displaying these graphs as HTML. The server is implemented in Java.

## Task Summary

1. **Goal**: To build an HTTP server that can accept a configuration file, load the data from the file, create a graph (Graph), and display it in the browser.
2. **Packages and Classes**:
    - **configs**: Classes related to configuration and graph definitions.
    - **graph**: Classes related to agents and topics handling.
    - **server**: Classes related to the HTTP server, including servlets handling requests.
    - **views**: Classes related to graph presentation in the browser.
    - **html_files**: Static HTML files used by the system.

## Package Descriptions

### 1. configs
- **BinOpAgent, IncAgent, PlusAgent**: Agents that perform different operations on data received from topics.
- **Config**: Interface for configuration.
- **GenericConfig**: Class that loads a configuration file and creates agents based on the data in the file.
- **Graph**: Class representing a graph of agents and topics.
- **Node**: Class representing a node in the graph.

### 2. graph
- **Agent**: Interface for agents.
- **Message**: Class representing messages sent between agents.
- **ParallelAgent**: Agent running in parallel (in a separate thread).
- **Topic**: Class representing a topic subscribed to by various agents.
- **TopicManagerSingleton**: Class managing all topics in the system.

### 3. server
- **ConfLoader**: Servlet that accepts a configuration file, loads it, and creates a graph.
- **HtmlLoader**: Servlet handling requests for static HTML files.
- **HTTPServer, MyHTTPServer**: Interface and class implementing the HTTP server.
- **RequestParser**: Class parsing HTTP requests.
- **Servlet**: Interface for servlets.
- **TopicDisplayer**: Servlet handling requests for publishing messages to topics.

### 4. views
- **HtmlGraphWriter**: Class generating HTML for graph display.

### 5. html_files
- **form.html**: HTML page allowing configuration file upload and message sending to topics.
- **index.html**: Main HTML page containing iframes for loading other pages.
- **temp.html**: Temporary HTML page.
- **graph.html**: HTML template for graph display.

## Design

The project is designed with modularity and simplicity in mind. Here’s an overview of the design components:

1. **Server Setup**:
   - The core of the project is an HTTP server (`MyHTTPServer`) that listens for incoming requests and dispatches them to appropriate handlers (servlets).

2. **Servlets**:
   - **`HtmlLoader`**: Handles GET requests to serve static HTML files from the `html_files` directory.
   - **`ConfLoader`**: Handles POST requests for file uploads. It processes the uploaded configuration file, generates a graph, and creates an HTML representation of the graph.
   - **`TopicDisplayer`**: An additional servlet that can be extended to display specific topics or additional data.

3. **Configuration Processing**:
   - **`GenericConfig`**: Reads and parses the uploaded configuration file.
   - **`Graph`**: Constructs a graph based on the parsed configuration data.
   - **`HtmlGraphWriter`**: Converts the graph into an HTML format for visualization.

4. **File Storage**:
   - Uploaded configuration files are stored in the `config_files` directory.
   - Generated HTML files for graphs are also stored in the `config_files` directory.

## Installation

To set up and run this project, follow the steps below:

1. **Clone the repository**:
    ```sh
    git clone <repository-url>
    cd <repository-directory>
    ```

2. **Set up the required libraries**:
    - Ensure you have the following JAR files in your classpath:
        - `javax.servlet-api-4.0.1.jar`
        - `commons-io-2.11.0.jar`
        - `commons-fileupload-1.4.jar`
    - If you are using an IDE like Eclipse:
        - Right-click on your project and select `Properties`.
        - Go to `Java Build Path` -> `Libraries`.
        - Click `Add External JARs` and select the above JAR files.

3. **Directory structure**:
    - Create the following directories in the root of your project if they do not already exist:
        - `html_files`: For storing the HTML files served by the server.
        - `config_files`: For storing uploaded configuration files and generated graph HTML files.

## Running the Project

To run the server, follow these steps:

1. **Compile the project**:
    - Ensure all Java files are compiled. If you are using an IDE like Eclipse, this should be done automatically.

2. **Start the server**:
    - Run the `Main` class to start the server.


- **Upload a configuration file**:
    - Open a browser and navigate to `http://localhost:8080`.
    - You should see an upload form. Select a configuration file and click `Upload`.

- **View the generated graph**:
    - After uploading a configuration file, you should be redirected to the generated graph HTML.
    - Alternatively, navigate to `http://localhost:8080/graph.html` to view the latest generated graph.

## File Descriptions

- **`test/Main.java`**: The main entry point for the server.
- **`server/MyHTTPServer.java`**: The custom HTTP server implementation.
- **`server/ConfLoader.java`**: The servlet handling file uploads and processing configuration files.
- **`server/HtmlLoader.java`**: The servlet serving static HTML files.
- **`server/TopicDisplayer.java`**: The servlet handling additional display logic (if any).
- **`configs/GenericConfig.java`**: Class for handling configuration file parsing.
- **`configs/Graph.java`**: Class for creating a graph from the configuration.
- **`views/HtmlGraphWriter.java`**: Class for generating HTML from the graph data.
- **`configs/BinOpAgent.java, IncAgent.java, PlusAgent.java`**: Agents that perform different operations on data received from topics.
- **`configs/Config.java`**: Interface for configuration.
- **`configs/Node.java`**: Class representing a node in the graph.
- **`graph/Agent.java`**: Interface for agents.
- **`graph/Message.java`**: Class representing messages sent between agents.
- **`graph/ParallelAgent.java`**: Agent running in parallel (in a separate thread).
- **`graph/Topic.java`**: Class representing a topic subscribed to by various agents.
- **`graph/TopicManagerSingleton.java`**: Class managing all topics in the system.

By following the instructions in this README, you should be able to set up, run, and interact with the server to upload configuration files and view the generated graphs.
