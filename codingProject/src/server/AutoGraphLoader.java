package server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import configs.GenericConfig;
import configs.Graph;
import views.HtmlGraphWriter;

public class AutoGraphLoader implements Servlet {

    @Override
    public void handle(RequestParser.RequestInfo ri, OutputStream toClient) throws IOException {
        String filePath = "C:\\Users\\User\\eclipse-workspace\\codingProject\\config_files\\simple.conf.txt";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            toClient.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
            toClient.write("File not found.".getBytes());
            return;
        }

        try {
            GenericConfig config = new GenericConfig();
            config.setConfFile(file.getPath());
            config.create();
            Graph graph = new Graph();
            graph.createFromTopics();

            // Generate HTML for graph display
            List<String> graphHtml = HtmlGraphWriter.getGraphHTML(graph);

            toClient.write("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n".getBytes());
            for (String line : graphHtml) {
                toClient.write(line.getBytes());
                toClient.write("\n".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            toClient.write("HTTP/1.1 500 Internal Server Error\r\n\r\n".getBytes());
            toClient.write("Internal Server Error: ".getBytes());
            toClient.write(e.getMessage().getBytes());
        }
    }

    @Override
    public void close() throws IOException {
        // Implement any necessary cleanup here
    }
}
