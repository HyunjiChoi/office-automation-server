package com.caddie.voice.y1.exception.common;

import lombok.Data;

@Data
public class ErrorMessage {
    public String message = "";
    public ErrorMessage(String message){
        this.message = message;
    }
}
