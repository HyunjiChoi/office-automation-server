package com.caddie.voice.y1.dto;

import lombok.Data;

@Data
public class CardData {

    private String noOrder; // 주문번호
    private String nmCust;  // 구매자
    private int Price;   // 거래금액
}
