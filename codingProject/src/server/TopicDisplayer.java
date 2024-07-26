package server;

import java.io.*;
import java.util.*;
import graph.TopicManagerSingleton;
import server.RequestParser.RequestInfo;

public class TopicDisplayer implements Servlet {
    @Override
    public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
        String topicName = ri.getParameters().get("topic");
        String message = ri.getParameters().get("message");

        if (topicName != null && message != null) {
            TopicManagerSingleton.get().getTopic(topicName).publish(new graph.Message(message));

            // Generate the HTML for the topic table
            StringBuilder sb = new StringBuilder();
            sb.append("<table border='1'>");
            sb.append("<tr><th>Topic</th><th>Last Message</th></tr>");
            sb.append("<tr><td>").append(topicName).append("</td><td>").append(message).append("</td></tr>");
            sb.append("</table>");

            toClient.write("HTTP/1.1 200 OK\r\n".getBytes());
            toClient.write("Content-Type: text/html\r\n\r\n".getBytes());
            toClient.write(sb.toString().getBytes());
        } else {
            toClient.write("HTTP/1.1 400 Bad Request\r\n\r\n".getBytes());
            toClient.write("<h1>400 Bad Request</h1>".getBytes());
        }
    }

    @Override
    public void close() throws IOException {
    }
}
