package com.wuhan.tracedemo.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Good {
    private Long id;
    private String name;
    private int weight;
    private int price;
}
