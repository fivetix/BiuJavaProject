package test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import server.RequestParser;

public class MainTrain5 {
    private static void testParseRequest() {
        // Test data with content
        String request = "GET /api/resource?id=123&name=test HTTP/1.1\n" +
                "Host: example.com\n" +
                "Content-Length: 5\n" + 
                "\n" +
                "filename=\"hello_world.txt\"\n" +
                "\n" +
                "hello world!\n" +
                "\n";

        BufferedReader input = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request.getBytes())));
        try {
            RequestParser.RequestInfo requestInfo = RequestParser.parseRequest(input);

            // Test HTTP command
            if (!requestInfo.getHttpCommand().equals("GET")) {
                System.out.println("HTTP command test failed (-5)");
            }

            // Test URI
            if (!requestInfo.getUri().equals("/api/resource?id=123&name=test")) {
                System.out.println("URI test failed (-5)");
            }

            // Test URI segments
            String[] expectedUriSegments = {"api", "resource"};
            if (!Arrays.equals(requestInfo.getUriSegments(), expectedUriSegments)) {
                System.out.println("URI segments test failed (-5)");
                for (String s : requestInfo.getUriSegments()) {
                    System.out.println(s);
                }
            }

            // Test parameters
            Map<String, String> expectedParams = new HashMap<>();
            expectedParams.put("id", "123");
            expectedParams.put("name", "test");
            expectedParams.put("filename", "\"hello_world.txt\"");
            if (!requestInfo.getParameters().equals(expectedParams)) {
                System.out.println("Parameters test failed (-5)");
                System.out.println("Expected Parameters: " + expectedParams);
                System.out.println("Actual Parameters: " + requestInfo.getParameters());
            }

            // Test content
            byte[] expectedContent = "hello world!\n".getBytes();
            if (!Arrays.equals(requestInfo.getContent(), expectedContent)) {
                System.out.println("Content test failed (-5)");
                System.out.println("Expected Content: " + new String(expectedContent));
                System.out.println("Actual Content: " + new String(requestInfo.getContent()));
            }
            input.close();
        } catch (IOException e) {
            System.out.println("Exception occurred during parsing: " + e.getMessage() + " (-5)");
        }

        // Test data without content
        request = "GET /api/resource?id=123&name=test HTTP/1.1\n" +
                "Host: example.com\n" +
                "\n";

        input = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request.getBytes())));
        try {
            RequestParser.RequestInfo requestInfo = RequestParser.parseRequest(input);

            // Test HTTP command
            if (!requestInfo.getHttpCommand().equals("GET")) {
                System.out.println("HTTP command test failed (-5)");
            }

            // Test URI
            if (!requestInfo.getUri().equals("/api/resource?id=123&name=test")) {
                System.out.println("URI test failed (-5)");
            }

            // Test URI segments
            String[] expectedUriSegments = {"api", "resource"};
            if (!Arrays.equals(requestInfo.getUriSegments(), expectedUriSegments)) {
                System.out.println("URI segments test failed (-5)");
                for (String s : requestInfo.getUriSegments()) {
                    System.out.println(s);
                }
            }

            // Test parameters
            Map<String, String> expectedParamsWithoutContent = new HashMap<>();
            expectedParamsWithoutContent.put("id", "123");
            expectedParamsWithoutContent.put("name", "test");
            if (!requestInfo.getParameters().equals(expectedParamsWithoutContent)) {
                System.out.println("Parameters test failed (-5)");
                System.out.println("Expected Parameters: " + expectedParamsWithoutContent);
                System.out.println("Actual Parameters: " + requestInfo.getParameters());
            }

            // Test content
            byte[] expectedContentWithoutContent = new byte[0]; // No content expected
            if (requestInfo.getContent() != null && requestInfo.getContent().length != 0) {
                System.out.println("Content test failed (-5)");
                System.out.println("Expected Content: " + new String(expectedContentWithoutContent));
                System.out.println("Actual Content: " + new String(requestInfo.getContent()));
            }
            input.close();
        } catch (IOException e) {
            System.out.println("Exception occurred during parsing: " + e.getMessage() + " (-5)");
        }

        // Test data with content length 0
        request = "GET /api/resource?id=123&name=test HTTP/1.1\n" +
                "Host: example.com\n" +
                "Content-Length: 0\n" +
                "\n" +
                "filename=\"hello_world.txt\"\n";

        input = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request.getBytes())));
        try {
            RequestParser.RequestInfo requestInfo = RequestParser.parseRequest(input);

            // Test HTTP command
            if (!requestInfo.getHttpCommand().equals("GET")) {
                System.out.println("HTTP command test failed (-5)");
            }

            // Test URI
            if (!requestInfo.getUri().equals("/api/resource?id=123&name=test")) {
                System.out.println("URI test failed (-5)");
            }

            // Test URI segments
            String[] expectedUriSegments = {"api", "resource"};
            if (!Arrays.equals(requestInfo.getUriSegments(), expectedUriSegments)) {
                System.out.println("URI segments test failed (-5)");
                for (String s : requestInfo.getUriSegments()) {
                    System.out.println(s);
                }
            }

            // Test parameters
            Map<String, String> expectedParamsWithLengthZero = new HashMap<>();
            expectedParamsWithLengthZero.put("id", "123");
            expectedParamsWithLengthZero.put("name", "test");
            expectedParamsWithLengthZero.put("filename", "\"hello_world.txt\"");
            if (!requestInfo.getParameters().equals(expectedParamsWithLengthZero)) {
                System.out.println("Parameters test failed (-5)");
                System.out.println("Expected Parameters: " + expectedParamsWithLengthZero);
                System.out.println("Actual Parameters: " + requestInfo.getParameters());
            }

            // Test content
            byte[] expectedContentWithLengthZero = new byte[0]; // No content expected
            if (requestInfo.getContent() != null && requestInfo.getContent().length != 0) {
                System.out.println("Content test failed (-5)");
                System.out.println("Expected Content: " + new String(expectedContentWithLengthZero));
                System.out.println("Actual Content: " + new String(requestInfo.getContent()));
            }
            input.close();
        } catch (IOException e) {
            System.out.println("Exception occurred during parsing: " + e.getMessage() + " (-5)");
        }
    }

    public static void testServer() throws Exception {
        // implement your own tests!
    }

    public static void main(String[] args) {
        testParseRequest(); // 40 points
        try {
            testServer(); // 60
        } catch (Exception e) {
            System.out.println("your server throwed an exception (-60)");
        }
        System.out.println("done");
    }
}
