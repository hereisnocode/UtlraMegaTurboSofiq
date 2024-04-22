package org.example;

import java.io.IOException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Listener sofiqAlwaysListens = new Listener();
        try{sofiqAlwaysListens.start(80);}
        catch (Exception e) {e.printStackTrace();}
        }
    }
