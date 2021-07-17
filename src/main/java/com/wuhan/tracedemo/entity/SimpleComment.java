package com.wuhan.tracedemo.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SimpleComment {
    private String commentid;
    private String comment;
    private int star;
    public SimpleComment(String _comment, String _commentid, int _star) {
        commentid = _commentid;
        comment = _comment;
        star = _star;
    }
}
