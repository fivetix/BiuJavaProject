package test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import server.ConfLoader;
import server.HtmlLoader;
import server.MyHTTPServer;
import server.TopicDisplayer;


public class Main {
    public static void main(String[] args) {
        System.out.println("Starting server...");
        MyHTTPServer server = new MyHTTPServer(8080, 10);

        server.addServlet("GET", "/", new HtmlLoader("html_files"));
        server.addServlet("POST", "/upload", new ConfLoader("config_files"));
        server.addServlet("GET", "/publish", new TopicDisplayer());
        server.addServlet("GET", "/graph.html", new HtmlLoader("config_files"));

        server.start();

        // Wait for user input to stop the server
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Press Enter to stop the server...");
            br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.close();
        System.out.println("Server stopped.");
    }
}




