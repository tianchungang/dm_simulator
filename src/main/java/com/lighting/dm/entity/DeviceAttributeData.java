package com.lighting.dm.entity;

import lombok.Data;

import java.util.List;

@Data
public class DeviceAttributeData {
    private String iotId;
    private String deviceSecret;
    private String productKey;
    private Long utcTime;
    private String deviceType;
    private List<DeviceAttribute> items;

}
