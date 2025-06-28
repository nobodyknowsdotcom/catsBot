package com.avegus.telegramconnector.broker.dto;

import lombok.Data;

@Data
public class Cat {
    private String id;
    private String name;
    private byte[] image;
    private String imageType;
    private int likeCount;
}
