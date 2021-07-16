package com.wuhan.tracedemo.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class CommentCode {
    private String userid;
    private String commentid;
    private boolean is_used;
}
