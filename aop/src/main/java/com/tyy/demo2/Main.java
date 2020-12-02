package com.tyy.demo2;

/**
 * @author:tyy
 * @date:2020/12/1
 */
public class Main {
    public String str="6";
    public static void main(String[] args) {
        Main sv=new Main();
        sv.change(sv.str);
        System.out.println(sv.str);
    }
    public void change(String str) {
        str="10";
    }
}
