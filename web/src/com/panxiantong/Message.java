package com.panxiantong;


public class Message {

    private long id;
    private String message;


    public Message(long id, String message) {
        super();
        this.id = id;
        this.message = message;
    }


    public long getId() {
        return id;
    }


    public String getMessage() {
        return message;
    }

}
