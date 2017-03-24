package com.example.may.msocial.models;


public class Phrase {
    private String section;
    private int actor;
    private int question;
    private String phrase;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Phrase(String id, int actor, String phrase) {
        this.id = id;
        this.actor = actor;
        this.phrase = phrase;
    }

    public Phrase() {

    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getActor() {
        return actor;
    }

    public void setActor(int actor) {
        this.actor = actor;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
}
