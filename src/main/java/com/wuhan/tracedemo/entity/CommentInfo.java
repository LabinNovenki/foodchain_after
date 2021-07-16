package com.wuhan.tracedemo.entity;


import java.math.BigInteger;

public class CommentInfo {
    public String comment;
    public BigInteger block;
    public String time;

    public CommentInfo(String _com, BigInteger _blo, String _time) {
        comment = _com;
        block = _blo;
        time = _time;
    }
    public void display() {
        System.out.println("评论： " + comment);
        System.out.println("区块号： " + block);
        System.out.println("时间： " + time);
    }
}
