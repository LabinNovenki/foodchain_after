package com.wuhan.tracedemo.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SimpleComment {
    private String commentid;
    private String comment;
    public SimpleComment(String _comment, String _commentid) {
        commentid = _commentid;
        comment = _comment;
    }
}
