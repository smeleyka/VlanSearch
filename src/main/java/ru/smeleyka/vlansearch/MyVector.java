package ru.smeleyka.vlansearch;

import java.util.TreeSet;
import java.util.Vector;

public class MyVector extends Vector {
   private TreeSet<Switch> switchSet;

    public MyVector() {
        this.switchSet = new TreeSet<>();
        init();
    }

    public void init() {
        this.switchSet.add(new Switch("A"));
        this.switchSet.add(new Switch("D"));
        this.switchSet.add(new Switch("W"));
        this.switchSet.add(new Switch("O"));
    }

    public void print(){
        for (Switch s:switchSet){
            System.out.println(s);
        }
    }
}
