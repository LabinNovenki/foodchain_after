package com.wuhan.tracedemo.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ComplexComment {
    private float score;
    private SimpleComment[] infos;
    public  ComplexComment(float _score, SimpleComment[] _infos) {
        score = _score;
        infos = _infos;
    }
}
