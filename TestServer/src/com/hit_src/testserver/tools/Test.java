package com.hit_src.testserver.tools;

public class Test {
    public static void main(String[] args) {
        boolean[][] test1=new boolean[][]{{true,true,true,true,true,true,true,true},{false,false,false,false,false,false,false,false}};
        boolean[][] test2=new boolean[][]{{false, false, false, false, false, false, false, false},{true,true,true,true,true,true,true,true}};
        boolean[][] x=BitOperation.XORS(test1,test2);
        System.out.println(x);
    }
}
