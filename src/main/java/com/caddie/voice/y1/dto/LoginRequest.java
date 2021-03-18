package com.caddie.voice.y1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class LoginRequest {
    @NonNull
    @ApiModelProperty(value = "사용자 아이디")
    private String id;

    @NonNull
    @ApiModelProperty(value = "사용자 비밀번호")
    private String pw;

}
