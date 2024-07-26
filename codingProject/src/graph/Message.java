package graph;

import java.util.Date;

public class Message {
    public final byte[] data;
    public final String asText;
    public final double asDouble;
    public final Date date;

    // Constructor for byte array
    public Message(byte[] data) {
        this.data = data;
        this.asText = new String(data);    
        this.asDouble = parseDouble(asText);
        this.date = new Date();
    }

    // Constructor for String
    public Message(String text) {
        this(text.getBytes());
    }

    // Constructor for double
    public Message(double number) {
        this(Double.toString(number));
    }

    //Method to parse a string to a double
    private double parseDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
}

