package com.example.instanthelp;

public class Questions {

    String question;
    //String id;

    //added this empty constructor because it was not
    //letting the line on retrieve class 46 display
    public Questions() {}

    public Questions(String question) {
        this.question = question;
     //   this.id = id;
    }




    public String getQuestion() {
        return question;
    }


//    public String getId() {
//        return id;
//    }


}
