package com.jumunhasyeo.product.application.command;

public record CreateProductCommand (
        String name,
        Integer price,
        String description,
        Long userId
){}
