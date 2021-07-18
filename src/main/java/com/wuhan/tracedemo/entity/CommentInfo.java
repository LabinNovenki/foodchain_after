package com.wuhan.tracedemo.entity;


import java.math.BigInteger;

public class CommentInfo {
    public String commentid;
    public String comment;
    public BigInteger block;
    public String time;
    public int star;

    public CommentInfo(String _commentid, String _com, BigInteger _blo, String _time, int _star) {
        commentid = _commentid;
        comment = _com;
        block = _blo;
        time = _time;
        star = _star;
    }
    public void display() {
        System.out.println("评论id： " + commentid);
        System.out.println("评论： " + comment);
        System.out.println("区块号： " + block);
        System.out.println("时间： " + time);
        System.out.println("评分： " + star);
    }
}
