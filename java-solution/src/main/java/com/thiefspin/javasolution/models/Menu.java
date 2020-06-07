package com.thiefspin.javasolution.models;

public enum Menu {
    MENU1(1, "Welcome to Mama Money! Where would you like to send money today? #placeholder"),
    MENU2(2, "How much money (in Rands) would you like to send to #placeholder?"),
    MENU3(3, "Your person you are sending to will receive: #placeholder \n1) OK"),
    MENU4(4, "Thank you for using Mama Money!");

    private int id;
    private String message;

    Menu(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
