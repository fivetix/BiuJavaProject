package server;

import java.io.*;
import java.util.*;
import server.RequestParser.RequestInfo;

public class HtmlLoader implements Servlet {
    private final String htmlFilesPath;

    public HtmlLoader(String htmlFilesPath) {
        this.htmlFilesPath = htmlFilesPath;
    }

    @Override
    public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
        System.out.println("HtmlLoader handling request for URI: " + ri.getUri());

        String uri = ri.getUri();
        if (uri.equals("/")) {
            uri = "/index.html";
        }

        File file = new File(htmlFilesPath, uri);
        if (!file.exists() || file.isDirectory()) {
            sendNotFound(toClient);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            toClient.write("HTTP/1.1 200 OK\r\n".getBytes());
            toClient.write("Content-Type: text/html\r\n\r\n".getBytes());
            while ((line = br.readLine()) != null) {
                toClient.write(line.getBytes());
                toClient.write("\n".getBytes()); // Ensure new lines are sent
            }
        }
    }

    private void sendNotFound(OutputStream toClient) throws IOException {
        toClient.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
        toClient.write("<h1>404 Not Found</h1>".getBytes());
    }

    @Override
    public void close() throws IOException {
    }
}
