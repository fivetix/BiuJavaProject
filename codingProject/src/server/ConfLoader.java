package server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import configs.Graph;
import views.HtmlGraphWriter;
import configs.GenericConfig;
import server.RequestParser.RequestInfo;

public class ConfLoader implements Servlet {
    private final String configPath;

    public ConfLoader(String configPath) {
        this.configPath = configPath;
    }

    @Override
    public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
        System.out.println("ConfLoader handling request for URI: " + ri.getUri());

        String contentType = ri.getHeaders().get("Content-Type");
        if (contentType == null || !contentType.contains("multipart/form-data")) {
            System.out.println("Invalid content type: " + contentType);
            toClient.write("HTTP/1.1 400 Bad Request\r\n\r\nInvalid content type".getBytes());
            return;
        }

        String boundary = "--" + contentType.split("boundary=")[1];
        InputStream inputStream = new ByteArrayInputStream(ri.getContent());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        boolean isFileContent = false;
        StringBuilder fileContent = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            System.out.println(line); // Print each line for debugging purposes
            if (line.startsWith(boundary)) {
                if (isFileContent) break;
                isFileContent = true;
                reader.readLine(); // Skip Content-Disposition line
                reader.readLine(); // Skip Content-Type line
                reader.readLine(); // Skip empty line before the content
            } else if (isFileContent) {
                fileContent.append(line).append("\n");
            }
        }

        if (fileContent.length() == 0) {
            System.out.println("Empty file content, nothing to process.");
            return;
        }

        String filePath = configPath + "/uploaded.conf.txt";
        Files.write(Paths.get(filePath), fileContent.toString().getBytes());

        GenericConfig config = new GenericConfig();
        config.setConfFile(filePath);
        config.create();

        Graph graph = new Graph();
        graph.createFromTopics();

        List<String> htmlContent = HtmlGraphWriter.getGraphHTML(graph);
        String graphHtmlPath = configPath + "/graph.html";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(graphHtmlPath))) {
            for (String htmlLine : htmlContent) {
                writer.write(htmlLine);
                writer.newLine();
            }
        }

        System.out.println("Graph HTML content saved to file.");

        toClient.write("HTTP/1.1 303 See Other\r\n".getBytes());
        toClient.write(("Location: /graph.html\r\n").getBytes());
        toClient.write("\r\n".getBytes());

        System.out.println("Redirecting client to /graph.html");
    }

    @Override
    public void close() throws IOException {
    }
}
