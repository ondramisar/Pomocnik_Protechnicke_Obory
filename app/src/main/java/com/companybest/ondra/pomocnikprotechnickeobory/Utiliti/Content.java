package com.companybest.ondra.pomocnikprotechnickeobory.Utiliti;



public class Content {

    public String name;
    public String content;
    public Boolean able;

    public Content(){

    }

    public Content(Boolean able, String content, String name) {
        this.able = able;
        this.content = content;
        this.name = name;
    }

    public boolean isAble() {
        return able;
    }

    public void setAble(boolean able) {
        this.able = able;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Content{" +
                "able=" + able +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
