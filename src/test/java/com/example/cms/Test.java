package com.example.cms;

public class Test {
    public void testResource(){
        System.out.println(this.getClass().getResourceAsStream("face").toString());
    }
}
