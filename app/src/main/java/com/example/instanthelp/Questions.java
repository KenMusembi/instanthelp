package com.example.instanthelp;

public class Questions {

    String question;
    String answer;

    //added this empty constructor because it was not
    //letting the line on retrieve class 46 display
    public Questions() {}

    public Questions(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
    public String getQuestion() {
        return question;
    }
    public String getAnswer() {
        return answer;
    }

}
