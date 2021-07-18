package com.wuhan.tracedemo.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ComplexComment {
    private String score;
    private CommentInfo[] infos;
    public  ComplexComment(String _score, CommentInfo[] _infos) {
        score = _score;
        infos = _infos;
    }
}
