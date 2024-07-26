package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import server.MyHTTPServer;
import server.RequestParser;
import server.Servlet;

public class MainTrain5b {
    public static void main(String[] args) {
        // יצירת שרת HTTP על פורט 8081 ומספר מקסימלי של ת'רדים 10
        MyHTTPServer server = new MyHTTPServer(8081, 10);

        // יצירת Servlet לדוגמה
        Servlet exampleServlet = new Servlet() {
            @Override
            public void handle(RequestParser.RequestInfo ri, OutputStream toClient) throws IOException {
                // חילוץ פרמטרים וביצוע חישוב פשוט
                String name = ri.getParameters().getOrDefault("name", "world");
                String message = "Hello, " + name + "!";

                // כתיבה בחזרה ללקוח
                String response = "HTTP/1.1 200 OK\r\nContent-Length: " + message.length() + "\r\n\r\n" + message;
                toClient.write(response.getBytes());
                toClient.flush();
            }

            @Override
            public void close() throws IOException {
                // פעולות סגירה אם יש צורך
            }
        };

        // הרשמת ה-Servlet לכתובת uri מסוימת בשרת
        server.addServlet("GET", "/greet", exampleServlet);

        // בדיקת מספר הת'רדים הפעילים לפני ואחרי הפעלת השרת
        int activeThreadsBefore = Thread.activeCount();
        System.out.println("Active threads before starting server: " + activeThreadsBefore);
        
        // הפעלת השרת
        server.start();

        // וידוא שהפעלת השרת פתחה ת'רד אחד נוסף
        int activeThreadsAfter = Thread.activeCount();
        System.out.println("Active threads after starting server: " + activeThreadsAfter);

        if (activeThreadsAfter != activeThreadsBefore + 1) {
            System.out.println("Failed to start the server correctly (-10 points).");
            return;
        } else {
            System.out.println("Server started correctly.");
        }

        try {
            // יצירת לקוח המתחבר לשרת על localhost ובפורט 8081
            Socket clientSocket = new Socket("localhost", 8081);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // שליחת בקשה שמתאימה להפעלת ה-Servlet
            out.println("GET /greet?name=Test HTTP/1.1");
            out.println("Host: localhost");
            out.println();
            out.flush();

            // קריאת התשובה מהשרת
            String responseLine;
            StringBuilder response = new StringBuilder();
            while ((responseLine = in.readLine()) != null) {
                response.append(responseLine).append("\n");
                if (responseLine.isEmpty()) break;
            }

            // קריאת הגוף של התשובה מהשרת
            while ((responseLine = in.readLine()) != null) {
                response.append(responseLine).append("\n");
            }

            // הדפסת התשובה מהשרת
            System.out.println("Response from server: " + response);

            // וידוא שקיבלתם את התוצאה הרצויה
            if (!response.toString().contains("Hello, Test!")) {
                System.out.println("Test failed: Unexpected response from server (-10 points).");
            } else {
                System.out.println("Test passed: Correct response from server.");
            }

            // סגירת כל המשאבים הפתוחים
            out.close();
            in.close();
            clientSocket.close();
            server.close();

            // המתנה לסגירת כל הת'רדים
            Thread.sleep(2000);

            // וידוא שכל הת'רדים נסגרו
            int activeThreadsAfterClose = Thread.activeCount();
            System.out.println("Active threads after closing server: " + activeThreadsAfterClose);
            if (activeThreadsAfterClose != activeThreadsBefore) {
                System.out.println("Failed to close all threads (-10 points).");
            } else {
                System.out.println("All threads closed successfully.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
