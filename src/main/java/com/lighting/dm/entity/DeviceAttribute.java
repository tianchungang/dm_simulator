package com.lighting.dm.entity;

import lombok.Data;

@Data
public class DeviceAttribute {
    private String name;
    private String code;
    private Object value;
    private String unit;
    private Long time;
    private Object desiredValue;
    private Long desiredTime;
}
