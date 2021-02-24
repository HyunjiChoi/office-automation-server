package com.caddie.voice.y1.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class Sales {

    private String MARKET_CD;        // 쇼핑물 식별코드
    private Date CAL_DT;             // 연월
    private double CAL_MONTH;        // 월
    private double CAL_DAY;          // 일
    private String SALES_TYPE;       // 구분
    private String AP;               // 소구
    private String NO_ORDER;         // 주문번호
    private String LN_PARTNER;       // 업체명
    private String NM_SPITEM;        // 품목
    private double CM;               // 판매단가
    private String COLOR;            // 색상
    private double QT_SO;            // 수량
    private double CA;               // 판매금액
    private double VAT;              // 수수료
    private double PRICE;            // 금액
    private double SUM;              // 합계
    private String ETC;              // 비고
    private String NM_CUST;          // 구매자
    private String CAL_WH;           // 정산여부

}
