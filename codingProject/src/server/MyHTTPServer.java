package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.*;

public class MyHTTPServer extends Thread implements HTTPServer {
    private final int port;
    private final ExecutorService threadPool;
    private final Map<String, Map<String, Servlet>> servlets;
    private volatile boolean running;
    private ServerSocket serverSocket;

    public MyHTTPServer(int port, int maxThreads) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(maxThreads);
        this.servlets = new ConcurrentHashMap<>();
        this.running = false;
    }

    @Override
    public void addServlet(String httpCommand, String uri, Servlet s) {
        System.out.println("Adding servlet for command: " + httpCommand + ", uri: " + uri);
        servlets.computeIfAbsent(httpCommand.toUpperCase(), k -> new ConcurrentHashMap<>()).put(uri, s);
    }

    @Override
    public void removeServlet(String httpCommand, String uri) {
        Map<String, Servlet> commandMap = servlets.get(httpCommand.toUpperCase());
        if (commandMap != null) {
            commandMap.remove(uri);
        }
    }

    @Override
    public void run() {
        running = true;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            while (running) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            if (!running) {
                System.out.println("Server stopped.");
            } else {
                e.printStackTrace();
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            RequestParser.RequestInfo requestInfo = RequestParser.parseRequest(in);
            String httpCommand = requestInfo.getHttpCommand().toUpperCase();
            String uri = requestInfo.getUri();

            System.out.println("Received request: " + httpCommand + " " + uri);

            Servlet servlet = findServlet(httpCommand, uri);
            if (servlet != null) {
                servlet.handle(requestInfo, out);
            } else {
                out.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Servlet findServlet(String httpCommand, String uri) {
        Map<String, Servlet> commandMap = servlets.get(httpCommand);
        if (commandMap == null) return null;

        String matchingUri = "";
        for (String registeredUri : commandMap.keySet()) {
            if (uri.startsWith(registeredUri) && registeredUri.length() > matchingUri.length()) {
                matchingUri = registeredUri;
            }
        }
        return commandMap.get(matchingUri);
    }

    @Override
    public void start() {
        if (!running) {
            super.start();
        }
    }

    @Override
    public void close() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(1, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }
    }
}
