package com.example.instanthelp;

public class Stories {

    String category, body;
    //String id;

    //added this empty constructor because it was not
    //letting the line on retrieve class 46 display

    // public Stories() {}

    public Stories(String category, String body) {
        this.category = category;
        this.body = body;
     //   this.id = id;
    }




    public String getStory() {
        return body;
    }


//    public String getId() {
//        return id;
//    }


}
