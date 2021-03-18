package com.caddie.voice.y1.dto;

import lombok.Data;

@Data
public class VcData extends ErpData{

    private String noOrder;  // 주문번호
    private String nmCust;   // 주문자명
    private String recipt;  // 수취자
    private int Sum;         // 판매금액 합계
    private int cpn;         // 쿠폰
    private int shipF;       // 배송비
    private int dpsitTotal;  // 입금 합계

}
