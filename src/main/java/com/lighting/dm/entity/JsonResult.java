package com.lighting.dm.entity;

import lombok.Data;

import java.io.Serializable;
@Data
public class JsonResult<T> implements Serializable {
    private int code = 1;
    private String message;
    private boolean success = true;
    private T data;
}
