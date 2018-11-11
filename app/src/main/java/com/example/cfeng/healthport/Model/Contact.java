package com.example.cfeng.healthport.Model;

public class Contact {

    private String name;
    private String number;
    private String position;

    public Contact(String name, String number,String position) {
        this.name = name;
        this.number = number;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getPosition(){return position;}

}
