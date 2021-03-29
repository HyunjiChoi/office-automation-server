package com.caddie.voice.y1.service;

import com.caddie.voice.y1.domain.SalesDetail;
import com.caddie.voice.y1.dto.*;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;


@AllArgsConstructor
@Service
public class VcMallService {

    private final int JUMUN = 0;
    private final int ERP = 1;
    private final int CARD = 2;
    private final int MONEY = 3;

    HashMap<String, VcData> vcList = new HashMap<>();
    HashMap<String, ErpData> erpList = new HashMap<>();
    HashMap<String, CardData> cardList = new HashMap<>();
    HashMap<String, MoneyData> moneyList = new HashMap<>();

    HashMap<String, ErpData> IncNoOrder = new HashMap<>();   // 주문번호 불일치
    HashMap<String, VcData> IncNmCust = new HashMap<>();    // 구매자 불일치
    HashMap<String, VcData> IncPrice = new HashMap<>();     // 금액 불일치
    HashMap<String, VcData> Refund = new HashMap<>();       // 환불
    HashMap<String, VcData> Dupli = new HashMap<>();        // 중복
    HashMap<String, VcData> Same = new HashMap<>();         // 완전 일치

    HashMap<String, CardData> CardIncNoOrder = new HashMap();   // 주문번호 불일치
    HashMap<String, VcData> CardIncNmCust = new HashMap<>();    // 구매자 불일치
    HashMap<String, VcData> CardIncPrice = new HashMap<>();     // 금액 불일치
    HashMap<String, VcData> CardRefund = new HashMap<>();       // 환불
    HashMap<String, VcData> CardDupli = new HashMap<>();        // 중복
    HashMap<String, VcData> CardSame = new HashMap<>();         // 완전 일치

    HashMap<String, VcData> MoneyIncNmCust = new HashMap<>(); // 구매자 불일치
    HashMap<String, VcData> MoneyIncPrice = new HashMap<>();     // 금액 불일치
    HashMap<String, VcData> MoneyRefund = new HashMap<>();       // 환불
    HashMap<String, VcData> MoneyDupli = new HashMap<>();        // 중복
    HashMap<String, VcData> MoneySame = new HashMap<>();         // 완전 일치


    public void insertExcelData(MultipartFile frontVideoFile) throws IOException {

        try {
            String extension = FilenameUtils.getExtension(frontVideoFile.getOriginalFilename());

            Workbook workbook = null;

            if (extension.equals("xlsx")) {
                workbook = new XSSFWorkbook(frontVideoFile.getInputStream());
            } else if (extension.equals("xls")) {
                workbook = new XSSFWorkbook(frontVideoFile.getInputStream());
            }

            Sheet jumun = workbook.getSheetAt(JUMUN);
            Sheet erp = workbook.getSheetAt(ERP);

            Sheet card = workbook.getSheetAt(CARD);
            Sheet money = workbook.getSheetAt(MONEY);

            InsertVcMallExcelData(jumun);
            InsertErpExcelData(erp);
            InsertCardExcelData(card);
            InsertMoneyExcelData(money);

        }catch (Exception e){ e.printStackTrace(); }

    }


    public void InsertVcMallExcelData(Sheet worksheet) throws IOException {

        try {

            System.out.println("worksheet.getPhysicalNumberOfRows() : " + worksheet.getPhysicalNumberOfRows());

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

                Row row = worksheet.getRow(i);

                VcData vcData = new VcData();


                vcData.setNoOrder(row.getCell(0).getStringCellValue());            // 주문번호
                vcData.setRecipt(row.getCell(5).getStringCellValue());             // 수취인
                vcData.setNmCust(row.getCell(9).getStringCellValue());             // 구매자
                vcData.setSum((int) row.getCell(16).getNumericCellValue());        // 판매금액 합계
                vcData.setCpn((int) row.getCell(17).getNumericCellValue());        // 쿠폰
                vcData.setShipF((int) row.getCell(22).getNumericCellValue());      //배송비
                vcData.setDpsitTotal((int) row.getCell(23).getNumericCellValue()); // 입금합계

                vcList.put(vcData.getNoOrder(), vcData);

                //System.out.println("vcMall : " + vcData);
                //System.out.println("vcMallList : " + vcList);
                }

            System.out.println("Hash 개수 : " + vcList.size());

        } catch (Exception e) { e.getStackTrace(); }

    }


    public void InsertErpExcelData(Sheet worksheet) {

        try {
            System.out.println("worksheet.getPhysicalNumberOfRows() : " + worksheet.getPhysicalNumberOfRows());
            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

                Row row = worksheet.getRow(i);

                ErpData erpData = new ErpData();

                erpData.setS(row.getCell(0).getStringCellValue());              // 선택
                erpData.setNoSo(row.getCell(1).getStringCellValue());           // 접수번호
                erpData.setDtSo(row.getCell(2).getStringCellValue());           // 접수일
                erpData.setNmShop(row.getCell(3).getStringCellValue());         // 접수유형
                erpData.setLnPartner(row.getCell(4).getStringCellValue());      // 매출처
                erpData.setNoOrder(row.getCell(5).getStringCellValue());        // NO_ORDER
                erpData.setCdSpitem(row.getCell(6).getStringCellValue());       // 상품코드
                erpData.setNmSpitem(row.getCell(7).getStringCellValue());       // 사움명
                erpData.setCdOpt(row.getCell(8).getStringCellValue());          // 옵션코드
                erpData.setNmOpt(row.getCell(9).getStringCellValue());          // 옵션명
                erpData.setNmCust(row.getCell(10).getStringCellValue());        // 주문자
                erpData.setQtSo((int) row.getCell(11).getNumericCellValue());   // 수량
                erpData.setUmVat((int) row.getCell(12).getNumericCellValue());  // 판매단가(VAT)
                erpData.setAmVat((int) row.getCell(13).getNumericCellValue());  // 판매금액(VAT)
                erpData.setCmVat((int) row.getCell(14).getNumericCellValue());  // 마감단가(VAT)
                erpData.setCaVat((int) row.getCell(15).getNumericCellValue());  // 마감금액(VAT)
                erpData.setCm((int) row.getCell(16).getNumericCellValue());     // 마감단가
                erpData.setCa((int) row.getCell(17).getNumericCellValue());     // 마감금액
                erpData.setVat((int) row.getCell(18).getNumericCellValue());    // VAT
                erpData.setAmRateCharge((int) row.getCell(19).getNumericCellValue()); // 정률수수료
                erpData.setNoGir(row.getCell(20).getStringCellValue());         // 의뢰번호
                erpData.setPayType(row.getCell(21).getStringCellValue());       // 결제형태
                erpData.setNoPo(row.getCell(22).getStringCellValue());          // 발주 번호
                erpData.setNoGirMin(row.getCell(23).getStringCellValue());      //최초 의뢰번호

                erpList.put(erpData.getNoOrder(), erpData);
                //VcData vcData = vcList.get(erpData.getNoOrder());

                if(vcList.containsKey(erpData.getNoOrder())){   // 주문번호 여부
                    // 주문번호 일치
                    VcData vcData = vcList.get(erpData.getNoOrder());

                    if(vcData.getRecipt().equals(erpData.getNmCust())
                            || vcData.getNmCust().equals(erpData.getNmCust())){ // 수취인 = 주문자 or 주문자 = 주문자 일치 여부
                        // 수취인 = 주문자 일치
                        vcList.get(erpData.getNmCust());

                        if(vcData.getSum() == erpData.getAmVat()){   // 입금액 = 금액합계(VAT) + 택배비 - 쿠폰 일치 여부
                            // 입금액 = 금액합계(VAT) + 택배비 - 쿠폰 일치
                            vcList.get(erpData.getAmVat());
                            Same.put(erpData.getNoOrder(), vcData);

                            System.out.println("Same : " + Same);
                        }else{  // 입금액 = 금액합계(VAT) + 택배비 - 쿠폰 불일치
                            //vcdata 주문 : 1, erp : 여러개 품목
                            /*if(IncPrice.containsKey(erpData.getNoOrder())){
                                 VcData vcdata = IncPrice.get(erpData.getNoOrder());
                                 //신발에 티셔츠 추가하기

                            }else{

                            }*/
                            IncPrice.put(erpData.getNoOrder(), vcData);//

                            System.out.println("IncPrice : " + IncPrice);
                        }

                    } else { // 수취인 = 주문자 불일치
                        IncNmCust.put(erpData.getNoOrder(), vcData);
                        System.out.println("IncNmCust : " + IncNmCust);
                    }

                } else {  // 주문번호 불일치 주문에는 없고, erp는 있고
                    System.out.println("주문번호 불일치");
                    ErpData erpData2 = erpList.get(erpData.getNoOrder());
                    //IncNoOrder.put(erpData.getNoOrder(), erpData);  // erp 데이터 기준 출력
                    IncNoOrder.put(erpData.getNoOrder(), erpData2);   // 주문서 데이터 기준 출력 (null로 출력)

                    System.out.println("IncNoOrder : " + IncNoOrder);
                }

/*
                vcmallobj -setNoGir(erpData.getNoGir());
                vcList.get("").setNoGir(erpData.getNoGir());
                vcList.get("").setNoGir(erpData.getNoGir());*/

                //System.out.println("erpData : " + erpData);
                //System.out.println("erpList : " + erpList);
            }
            System.out.println("#####################################################");
            System.out.println("결론 Same >> " + Same);
            System.out.println("결론 InNoOrder >> " + IncNoOrder);
            System.out.println("결론 IncPrice >> " + IncPrice);
            System.out.println("결론 IncNmCust >> " + IncNmCust);
            System.out.println("#####################################################");
            //System.out.println("Hash 개수 : " + erpList.size());
        } catch (Exception e) { e.getMessage(); }

    }


    public void InsertCardExcelData(Sheet worksheet) {

        try {
            System.out.println(" ------------------------------------------------------ ");
            System.out.println("worksheet.getPhysicalNumberOfRows() : " + worksheet.getPhysicalNumberOfRows());
            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

                Row row = worksheet.getRow(i);

                CardData cardData = new CardData();

                cardData.setNoOrder(row.getCell(8).getStringCellValue());        // NO_ORDER
                cardData.setNmCust(row.getCell(10).getStringCellValue());        // 주문자
                cardData.setPrice((int) row.getCell(12).getNumericCellValue());  // 거래금액

                cardList.put(cardData.getNoOrder(), cardData);

                if(Same.containsKey(cardData.getNoOrder())){   // 주문번호 여부
                    // 주문번호 일치
                    VcData vcData = Same.get(cardData.getNoOrder());

                    if(vcData.getNmCust().equals(cardData.getNmCust())
                            || vcData.getRecipt().equals(cardData.getNmCust())){ // 수취인 = 주문자 일치 여부
                        // 수취인 = 주문자 일치
                        Same.get(cardData.getNmCust());

                        if(vcData.getDpsitTotal() == cardData.getPrice()){   // 입금액 = 거래금액
                            // 입금액 = 금액합계(VAT) + 택배비 - 쿠폰 일치
                            Same.get(cardData.getPrice());
                            CardSame.put(cardData.getNoOrder(), vcData);

                            System.out.println("CardSame : " + CardSame);
                        } else {
                            Same.get(cardData.getPrice());
                            CardIncPrice.put(cardData.getNoOrder(), vcData);
                            System.out.println("CardIncPrice : " + CardIncPrice );
                        }

                    } else { // 수취인 = 주문자 불일치
                        CardIncNmCust.put(cardData.getNoOrder(), vcData);

                        System.out.println("CardIncNmCust : " + CardIncNmCust);
                    }

                } else {  // 주문번호 불일치
                    CardData cardData2 = cardList.get(cardData.getNoOrder());
                    CardIncNoOrder.put(cardData2.getNoOrder(), cardData2);

                    System.out.println("CardIncNoOrder : " + CardIncNoOrder);
                }

            }

            System.out.println("#####################################################");
            System.out.println("결론 CardSame >> " + CardSame);
            System.out.println("결론 CardIncNoOrder >> " + CardIncNoOrder);
            System.out.println("결론 CardIncPrice >> " + CardIncPrice);
            System.out.println("결론 CardIncNmCust >> " + CardIncNmCust);
            System.out.println("#####################################################");
        } catch (Exception e) { e.getMessage(); }

    }


    public void InsertMoneyExcelData(Sheet worksheet) {
        try {
            System.out.println(" ------------------------------------------------------ ");
            System.out.println("worksheet.getPhysicalNumberOfRows() : " + worksheet.getPhysicalNumberOfRows());

            HashMap<String, VcData> testSame = new HashMap<>();    // Money 전용

            Set<String> keySet = Same.keySet();
            Iterator<String> keyIterator = keySet.iterator();

            String key = null;  // key
            ArrayList<String> keyList = new ArrayList<>();  // key만 담아놓은 List

            // value 값으로 key 가져오기
            while (keyIterator.hasNext()) {
                key = keyIterator.next();
                testSame.put(key, Same.get(key));
                keyList.add(key);
                System.out.println("key >> " + key);
                System.out.println("keyList >> " + keyList);
            }

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

                Row row = worksheet.getRow(i);

                MoneyData moneyData = new MoneyData();

                System.out.println( i + "번째" );

                moneyData.setNmCust(row.getCell(5).getStringCellValue());        // 주문자
                moneyData.setPrice((int) row.getCell(6).getNumericCellValue());  // 거래금액

                System.out.println("남은 testSame >> " + testSame);
                System.out.println("남은 testSame 크기 >> " + testSame.size());

                for(int j = 0; j < testSame.size(); j++) {
                    System.out.println("제대로 지워졌나 확인해보자 " + testSame.size());
                    System.out.println("moneyData.getNmCust() >> " + moneyData.getNmCust());
                    System.out.println("testSame.get(keyList.get(j)).getNmCust() >> " + testSame.get(keyList.get(j)).getNmCust());

                    if (testSame.get(keyList.get(j)).getNmCust().equals(moneyData.getNmCust())
                            || testSame.get(keyList.get(j)).getRecipt().equals(moneyData.getNmCust())) {   // 주문자 일치 여
                        // 주문자일치
                        VcData vcData = testSame.get(keyList.get(j));

                        if (vcData.getDpsitTotal() == moneyData.getPrice()) {   // 입금액 = 입금 합계

                            vcData = testSame.get(vcData.getNoOrder());
                            MoneySame.put(vcData.getNoOrder(), vcData);

                            /*if(MoneyIncNmCust.containsValue(vcData.getNoOrder())){
                                System.out.println("삭제삭제");
                                MoneyIncNmCust.remove(vcData.getNoOrder());
                                System.out.println("완료완료");
                            }*/
                            testSame.remove(vcData.getNoOrder());
                            keyList.remove(keyList.get(j));

                            System.out.println(" 입금액 = 입금 합계 remove>>>  "+testSame.size());

                            System.out.println("MoneySame >> " + MoneySame);
                            System.out.println("MoneyIncNmCust" + MoneyIncNmCust);

                        } else {  // 입금액 = 입금 합계 불일치
                            MoneyIncPrice.put(vcData.getNoOrder(), vcData);

                            testSame.remove(vcData.getNoOrder());
                            keyList.remove(keyList.get(j));

                            System.out.println("MoneyIncPrice : " + MoneyIncPrice);
                        }

                    } else {  // 주문자 불일치
                        VcData vcData2 = testSame.get(keyList.get(j));
                        MoneyIncNmCust.put(vcData2.getNoOrder(), vcData2);

                        System.out.println("MoneyIncNmCust : " + MoneyIncNmCust);
                        System.out.println("MoneyIncNmCust.size() : " + MoneyIncNmCust.size());
                    }
                }
            }

            System.out.println("#####################################################");
            System.out.println("결론 MoneySame >> " + MoneySame);
            System.out.println("결론 MoneyIncPrice >> " + MoneyIncPrice);
            System.out.println("결론 MoneyIncNmCust >> " + MoneyIncNmCust);
            System.out.println("결론 MoneyIncNmCust 크기 >> " + MoneyIncNmCust.size());
            System.out.println("#####################################################");

        } catch (NullPointerException e){
            e.getStackTrace();
            System.out.println("null");
        } catch (Exception e) {
            System.out.println("Exception");
            e.getStackTrace();
        }
    }


    public void downExcelData(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet1 = workbook.createSheet("카드 ERP");
        Sheet sheet2 = workbook.createSheet("카드 ERP 주문번호 불일치");
        Sheet sheet3 = workbook.createSheet("카드 ERP 주문자 불일치");
        Sheet sheet4 = workbook.createSheet("카드 ERP 금액 불일치");

        Sheet sheet5 = workbook.createSheet("현금 ERP");
        Sheet sheet6 = workbook.createSheet("현금 ERP 주문자 불일치");
        Sheet sheet7 = workbook.createSheet("현금 ERP 금액 불일치");

        // 이니시스
        Set<String> CardSameKeySet = CardSame.keySet();
        Set<String> CardIncNoKeySet = CardIncNoOrder.keySet();
        Set<String> CardIncNmKeySet = CardIncNmCust.keySet();
        Set<String> CardIncPrKeySet = CardIncPrice.keySet();

        Iterator<String> SameCardkeyIterator = CardSameKeySet.iterator();
        Iterator<String> CardIncNoKeyIterator = CardIncNoKeySet.iterator();
        Iterator<String> CardIncNmKeyIterator = CardIncNmKeySet.iterator();
        Iterator<String> CardIncPrKeyIterator = CardIncPrKeySet.iterator();

        // key (주문번호)
        String SameCardKey = null;
        String CardIncNoKey = null;
        String CardIncNmKey = null;
        String CardIncPrKey = null;

        // key만 담아놓은 List
        ArrayList<String> SameCardKeyList = new ArrayList<>();
        ArrayList<String> CardIncNoKeyList = new ArrayList<>();
        ArrayList<String> CardIncNmKeyList = new ArrayList<>();
        ArrayList<String> CardIncPrKeyList = new ArrayList<>();


        while (SameCardkeyIterator.hasNext()) {
            SameCardKey = SameCardkeyIterator.next();   // 일치
            SameCardKeyList.add(SameCardKey);
        }

        while (CardIncNoKeyIterator.hasNext()) {
            CardIncNoKey = CardIncNoKeyIterator.next();   // 주문번호 불일치
            CardIncNoKeyList.add(CardIncNoKey);
        }

        while (CardIncNmKeyIterator.hasNext()){
            CardIncNmKey = CardIncNmKeyIterator.next();   // 주문자 불일치
            CardIncNmKeyList.add(CardIncNmKey);
        }

        while (CardIncPrKeyIterator.hasNext()){
            CardIncPrKey = CardIncPrKeyIterator.next();   // 금액 불일치
            CardIncPrKeyList.add(CardIncPrKey);
        }

        Row row1;
        Row row2;
        Row row3;
        Row row4;

        int rowCnt1 = 1;
        int rowCnt2 = 1;
        int rowCnt3 = 1;
        int rowCnt4 = 1;

        createHeader(sheet1);
        createHeader(sheet2);
        createHeader(sheet3);
        createHeader(sheet4);

        // 이니시스 데이터

        // 일치
        for(int i=0; i<CardSame.size(); i++ ){

            row1 = sheet1.createRow(rowCnt1);

            row1.createCell(0).setCellValue(erpList.get(SameCardKeyList.get(i)).getS());    // 선택
            row1.createCell(1).setCellValue(erpList.get(SameCardKeyList.get(i)).getNoSo());      // 접수번호
            row1.createCell(2).setCellValue(erpList.get(SameCardKeyList.get(i)).getDtSo());      // 접수일
            row1.createCell(3).setCellValue(erpList.get(SameCardKeyList.get(i)).getNmShop());    // 접수유형
            row1.createCell(4).setCellValue(erpList.get(SameCardKeyList.get(i)).getLnPartner()); // 매출처
            row1.createCell(5).setCellValue(erpList.get(SameCardKeyList.get(i)).getNoOrder());   // NO_ORDER
            row1.createCell(6).setCellValue(erpList.get(SameCardKeyList.get(i)).getCdSpitem());  // 상품코드
            row1.createCell(7).setCellValue(erpList.get(SameCardKeyList.get(i)).getNmSpitem());  // 상품명
            row1.createCell(8).setCellValue(erpList.get(SameCardKeyList.get(i)).getCdOpt());     // 옵션코드
            row1.createCell(9).setCellValue(erpList.get(SameCardKeyList.get(i)).getNmOpt());     // 옵션명
            row1.createCell(10).setCellValue(erpList.get(SameCardKeyList.get(i)).getNmCust());   // 주문자
            row1.createCell(11).setCellValue(erpList.get(SameCardKeyList.get(i)).getQtSo());     // 수량
            row1.createCell(12).setCellValue(erpList.get(SameCardKeyList.get(i)).getUmVat());    // 판매단가(VAT)
            row1.createCell(13).setCellValue(erpList.get(SameCardKeyList.get(i)).getAmVat());    // 판매금액(VAT)
            row1.createCell(14).setCellValue(0);  // 마감단가(VAT)
            row1.createCell(15).setCellValue(0);  // 마감금액(VAT)
            row1.createCell(16).setCellValue(0);  // 마감단가
            row1.createCell(17).setCellValue(0);  // 마감금액
            row1.createCell(18).setCellValue(0);  // VAT
            row1.createCell(19).setCellValue(0);  // 정률수수료
            row1.createCell(20).setCellValue(erpList.get(SameCardKeyList.get(i)).getNoGir());        // 의뢰번호
            row1.createCell(21).setCellValue(erpList.get(SameCardKeyList.get(i)).getPayType());      // 결제형태
            row1.createCell(22).setCellValue(0);  // 발주 번호
            row1.createCell(23).setCellValue(erpList.get(SameCardKeyList.get(i)).getNoGirMin());     //최초 의뢰번호
            row1.createCell(24).setCellValue(vcList.get(SameCardKeyList.get(i)).getShipF());    // 배송비
            row1.createCell(25).setCellValue(vcList.get(SameCardKeyList.get(i)).getCpn() * -1); // 쿠폰

            rowCnt1++;
        }

        // 주문번호 불일치
        for(int i=0; i<CardIncNoOrder.size(); i++ ){

            row2 = sheet2.createRow(rowCnt2);

            if( erpList.containsKey((CardIncNoKeyList.get(i)))){
                row2.createCell(0).setCellValue(erpList.get(SameCardKeyList.get(i)).getS());    // 선택
                row2.createCell(1).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getNoSo());      // 접수번호
                row2.createCell(2).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getDtSo());      // 접수일
                row2.createCell(3).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getNmShop());    // 접수유형
                row2.createCell(4).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getLnPartner()); // 매출처
                row2.createCell(5).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getNoOrder());   // NO_ORDER
                row2.createCell(6).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getCdSpitem());  // 상품코드
                row2.createCell(7).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getNmSpitem());  // 상품명
                row2.createCell(8).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getCdOpt());     // 옵션코드
                row2.createCell(9).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getNmOpt());     // 옵션명
                row2.createCell(10).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getNmCust());   // 주문자
                row2.createCell(11).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getQtSo());     // 수량
                row2.createCell(12).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getUmVat());    // 판매단가(VAT)
                row2.createCell(13).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getAmVat());    // 판매금액(VAT)
                row2.createCell(14).setCellValue(0);  // 마감단가(VAT)
                row2.createCell(15).setCellValue(0);  // 마감금액(VAT)
                row2.createCell(16).setCellValue(0);  // 마감단가
                row2.createCell(17).setCellValue(0);  // 마감금액
                row2.createCell(18).setCellValue(0);  // VAT
                row2.createCell(19).setCellValue(0);  // 정률수수료
                row2.createCell(20).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getNoGir());        // 의뢰번호
                row2.createCell(21).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getPayType());      // 결제형태
                row2.createCell(22).setCellValue(0);  // 발주 번호
                row2.createCell(23).setCellValue(erpList.get(CardIncNoKeyList.get(i)).getNoGirMin());     //최초 의뢰번호
                row2.createCell(24).setCellValue(vcList.get(CardIncNoKeyList.get(i)).getShipF());    // 배송비
                row2.createCell(25).setCellValue(vcList.get(CardIncNoKeyList.get(i)).getCpn() * -1); // 쿠폰

                rowCnt2++;
            }else{
                continue;
            }
        }

        // 주문자 불일치
        for(int i=0; i<CardIncNmCust.size(); i++ ){

           if( erpList.containsKey((CardIncNmKeyList.get(i)))) {
               row3 = sheet3.createRow(rowCnt3);

               row3.createCell(0).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getS());        // 선택
               row3.createCell(1).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getNoSo());     // 접수번호
               row3.createCell(2).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getDtSo());     // 접수일
               row3.createCell(3).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getNmShop());   // 접수유형
               row3.createCell(4).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getLnPartner());// 매출처
               row3.createCell(5).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getNoOrder());  // NO_ORDER
               row3.createCell(6).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getCdSpitem()); // 상품코드
               row3.createCell(7).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getNmSpitem()); // 상품명
               row3.createCell(8).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getCdOpt());    // 옵션코드
               row3.createCell(9).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getNmOpt());    // 옵션명
               row3.createCell(10).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getNmCust());  // 주문자
               row3.createCell(11).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getQtSo());    // 수량
               row3.createCell(12).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getUmVat());   // 판매단가(VAT)
               row3.createCell(13).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getAmVat());   // 판매금액(VAT)
               row3.createCell(14).setCellValue(0);  // 마감단가(VAT)
               row3.createCell(15).setCellValue(0);  // 마감금액(VAT)
               row3.createCell(16).setCellValue(0);  // 마감단가
               row3.createCell(17).setCellValue(0);  // 마감금액
               row3.createCell(18).setCellValue(0);  // VAT
               row3.createCell(19).setCellValue(0);  // 정률수수료
               row3.createCell(20).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getNoGir());    // 의뢰번호
               row3.createCell(21).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getPayType());  // 결제형태
               row3.createCell(22).setCellValue(0);  // 발주 번호
               row3.createCell(23).setCellValue(erpList.get(CardIncNmKeyList.get(i)).getNoGirMin()); //최초 의뢰번호
               row3.createCell(24).setCellValue(vcList.get(CardIncNmKeyList.get(i)).getShipF());     // 배송비
               row3.createCell(25).setCellValue(vcList.get(CardIncNmKeyList.get(i)).getCpn() * -1);  // 쿠폰

               rowCnt3++;
           }else{
               continue;
           }
        }

        // 금액 불일치
        for(int i=0; i<CardIncPrice.size(); i++ ) {

            if (erpList.containsKey((CardIncPrKeyList.get(i)))) {
                row4 = sheet4.createRow(rowCnt4);

                row4.createCell(0).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getS());    // 선택
                row4.createCell(1).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getNoSo());      // 접수번호
                row4.createCell(2).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getDtSo());      // 접수일
                row4.createCell(3).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getNmShop());    // 접수유형
                row4.createCell(4).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getLnPartner()); // 매출처
                row4.createCell(5).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getNoOrder());   // NO_ORDER
                row4.createCell(6).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getCdSpitem());  // 상품코드
                row4.createCell(7).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getNmSpitem());  // 상품명
                row4.createCell(8).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getCdOpt());     // 옵션코드
                row4.createCell(9).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getNmOpt());     // 옵션명
                row4.createCell(10).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getNmCust());   // 주문자
                row4.createCell(11).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getQtSo());     // 수량
                row4.createCell(12).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getUmVat());    // 판매단가(VAT)
                row4.createCell(13).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getAmVat());    // 판매금액(VAT)
                row4.createCell(14).setCellValue(0);  // 마감단가(VAT)
                row4.createCell(15).setCellValue(0);  // 마감금액(VAT)
                row4.createCell(16).setCellValue(0);  // 마감단가
                row4.createCell(17).setCellValue(0);  // 마감금액
                row4.createCell(18).setCellValue(0);  // VAT
                row4.createCell(19).setCellValue(0);  // 정률수수료
                row4.createCell(20).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getNoGir());        // 의뢰번호
                row4.createCell(21).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getPayType());      // 결제형태
                row4.createCell(22).setCellValue(0);  // 발주 번호
                row4.createCell(23).setCellValue(erpList.get(CardIncPrKeyList.get(i)).getNoGirMin());     //최초 의뢰번호
                row4.createCell(24).setCellValue(vcList.get(CardIncPrKeyList.get(i)).getShipF());    // 배송비
                row4.createCell(25).setCellValue(vcList.get(CardIncPrKeyList.get(i)).getCpn() * -1); // 쿠폰

                rowCnt4++;
            } else {
                continue;
            }
        }

        //무통장
        Set<String> MoneySameKeySet = MoneySame.keySet();
        Set<String> MoneyIncNmKeySet = MoneyIncNmCust.keySet();
        Set<String> MoneyIncPrKeySet = MoneyIncPrice.keySet();

        Iterator<String> MoneySameKeyIterator = MoneySameKeySet.iterator();
        Iterator<String> MoneyIncNmkeyIterator = MoneyIncNmKeySet.iterator();
        Iterator<String> MoneyIncPrKeIterator = MoneyIncPrKeySet.iterator();

        // key
        String MoneySameKey = null;
        String MoneyIncNmKey = null;
        String MoneyIncPrKey = null;


        // key만 담아놓은 List
        ArrayList<String> MoneySameKeyList = new ArrayList<>();
        ArrayList<String> MoneyIncNmKeyList = new ArrayList<>();
        ArrayList<String> MoneyIncPrKeyList = new ArrayList<>();

        while (MoneySameKeyIterator.hasNext()) {
            MoneySameKey = MoneySameKeyIterator.next();
            MoneySameKeyList.add(MoneySameKey);
        }

        while (MoneyIncNmkeyIterator.hasNext()) {
            MoneyIncNmKey = MoneyIncNmkeyIterator.next();
            MoneyIncNmKeyList.add(MoneyIncNmKey);
        }

        while (MoneyIncPrKeIterator.hasNext()) {
            MoneyIncPrKey = MoneyIncPrKeIterator.next();
            MoneyIncPrKeyList.add(MoneyIncPrKey);
        }


        int rowCnt5 = 1;
        int rowCnt6 = 1;
        int rowCnt7 = 1;

        Row row5;
        Row row6;
        Row row7;

        createHeader(sheet5);
        createHeader(sheet6);
        createHeader(sheet7);

        for(int j=0; j<MoneySame.size(); j++){
            row5 = sheet5.createRow(rowCnt5);

            row5.createCell(0).setCellValue("N");    // 선택
            row5.createCell(1).setCellValue(erpList.get(MoneySameKeyList.get(j)).getNoSo());      // 접수번호
            row5.createCell(2).setCellValue(erpList.get(MoneySameKeyList.get(j)).getDtSo());      // 접수일
            row5.createCell(3).setCellValue(erpList.get(MoneySameKeyList.get(j)).getNmShop());    // 접수유형
            row5.createCell(4).setCellValue(erpList.get(MoneySameKeyList.get(j)).getLnPartner()); // 매출처
            row5.createCell(5).setCellValue(erpList.get(MoneySameKeyList.get(j)).getNoOrder());   // NO_ORDER
            row5.createCell(6).setCellValue(erpList.get(MoneySameKeyList.get(j)).getCdSpitem());  // 상품코드
            row5.createCell(7).setCellValue(erpList.get(MoneySameKeyList.get(j)).getNmSpitem());  // 상품명
            row5.createCell(8).setCellValue(erpList.get(MoneySameKeyList.get(j)).getCdOpt());     // 옵션코드
            row5.createCell(9).setCellValue(erpList.get(MoneySameKeyList.get(j)).getNmOpt());     // 옵션명
            row5.createCell(10).setCellValue(erpList.get(MoneySameKeyList.get(j)).getNmCust());   // 주문자
            row5.createCell(11).setCellValue(erpList.get(MoneySameKeyList.get(j)).getQtSo());     // 수량
            row5.createCell(12).setCellValue(erpList.get(MoneySameKeyList.get(j)).getUmVat());    // 판매단가(VAT)
            row5.createCell(13).setCellValue(erpList.get(MoneySameKeyList.get(j)).getAmVat());    // 판매금액(VAT)
            row5.createCell(14).setCellValue(0);  // 마감단가(VAT)
            row5.createCell(15).setCellValue(0);  // 마감금액(VAT)
            row5.createCell(16).setCellValue(0);  // 마감단가
            row5.createCell(17).setCellValue(0);  // 마감금액
            row5.createCell(18).setCellValue(0);  // VAT
            row5.createCell(19).setCellValue(0);  // 정률수수료
            row5.createCell(20).setCellValue(erpList.get(MoneySameKeyList.get(j)).getNoGir());        // 의뢰번호
            row5.createCell(21).setCellValue(erpList.get(MoneySameKeyList.get(j)).getPayType());      // 결제형태
            row5.createCell(22).setCellValue(0);  // 발주 번호
            row5.createCell(23).setCellValue(erpList.get(MoneySameKeyList.get(j)).getNoGirMin());     //최초 의뢰번호
            row5.createCell(24).setCellValue(vcList.get(MoneySameKeyList.get(j)).getShipF());    // 배송비
            row5.createCell(25).setCellValue(vcList.get(MoneySameKeyList.get(j)).getCpn() * -1); // 쿠폰

            rowCnt5++;
        }

        for(int j=0; j<MoneyIncNmCust.size(); j++){

            if(erpList.containsKey(MoneyIncNmKeyList.get(j))) {
                row6 = sheet6.createRow(rowCnt6);

                row6.createCell(0).setCellValue("N");    // 선택
                row6.createCell(1).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getNoSo());      // 접수번호
                row6.createCell(2).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getDtSo());      // 접수일
                row6.createCell(3).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getNmShop());    // 접수유형
                row6.createCell(4).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getLnPartner()); // 매출처
                row6.createCell(5).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getNoOrder());   // NO_ORDER
                row6.createCell(6).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getCdSpitem());  // 상품코드
                row6.createCell(7).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getNmSpitem());  // 상품명
                row6.createCell(8).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getCdOpt());     // 옵션코드
                row6.createCell(9).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getNmOpt());     // 옵션명
                row6.createCell(10).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getNmCust());   // 주문자
                row6.createCell(11).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getQtSo());     // 수량
                row6.createCell(12).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getUmVat());    // 판매단가(VAT)
                row6.createCell(13).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getAmVat());    // 판매금액(VAT)
                row6.createCell(14).setCellValue(0);  // 마감단가(VAT)
                row6.createCell(15).setCellValue(0);  // 마감금액(VAT)
                row6.createCell(16).setCellValue(0);  // 마감단가
                row6.createCell(17).setCellValue(0);  // 마감금액
                row6.createCell(18).setCellValue(0);  // VAT
                row6.createCell(19).setCellValue(0);  // 정률수수료
                row6.createCell(20).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getNoGir());        // 의뢰번호
                row6.createCell(21).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getPayType());      // 결제형태
                row6.createCell(22).setCellValue(0);  // 발주 번호
                row6.createCell(23).setCellValue(erpList.get(MoneyIncNmKeyList.get(j)).getNoGirMin());     //최초 의뢰번호
                row6.createCell(24).setCellValue(vcList.get(MoneyIncNmKeyList.get(j)).getShipF());    // 배송비
                row6.createCell(25).setCellValue(vcList.get(MoneyIncNmKeyList.get(j)).getCpn() * -1); // 쿠폰

                rowCnt6++;
            }else{
                continue;
            }
        }

        for(int j=0; j<MoneyIncPrice.size(); j++){
            if(erpList.containsKey(MoneyIncPrKeyList.get(j))) {
                row7 = sheet7.createRow(rowCnt7);

                row7.createCell(0).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getS());    // 선택
                row7.createCell(1).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getNoSo());      // 접수번호
                row7.createCell(2).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getDtSo());      // 접수일
                row7.createCell(3).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getNmShop());    // 접수유형
                row7.createCell(4).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getLnPartner()); // 매출처
                row7.createCell(5).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getNoOrder());   // NO_ORDER
                row7.createCell(6).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getCdSpitem());  // 상품코드
                row7.createCell(7).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getNmSpitem());  // 상품명
                row7.createCell(8).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getCdOpt());     // 옵션코드
                row7.createCell(9).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getNmOpt());     // 옵션명
                row7.createCell(10).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getNmCust());   // 주문자
                row7.createCell(11).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getQtSo());     // 수량
                row7.createCell(12).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getUmVat());    // 판매단가(VAT)
                row7.createCell(13).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getAmVat());    // 판매금액(VAT)
                row7.createCell(14).setCellValue(0);  // 마감단가(VAT)
                row7.createCell(15).setCellValue(0);  // 마감금액(VAT)
                row7.createCell(16).setCellValue(0);  // 마감단가
                row7.createCell(17).setCellValue(0);  // 마감금액
                row7.createCell(18).setCellValue(0);  // VAT
                row7.createCell(19).setCellValue(0);  // 정률수수료
                row7.createCell(20).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getNoGir());        // 의뢰번호
                row7.createCell(21).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getPayType());      // 결제형태
                row7.createCell(22).setCellValue(0);  // 발주 번호
                row7.createCell(23).setCellValue(erpList.get(MoneyIncPrKeyList.get(j)).getNoGirMin());     //최초 의뢰번호
                row7.createCell(24).setCellValue(vcList.get(MoneyIncPrKeyList.get(j)).getShipF());    // 배송비
                row7.createCell(25).setCellValue(vcList.get(MoneyIncPrKeyList.get(j)).getCpn() * -1); // 쿠폰

                rowCnt7++;
            }else {
                continue;
            }
        }

        response.setContentType("application/msexcel");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode("vcMall.xlsx","UTF-8")));
        response.setHeader("Content-Transfer-Encoding", "binary");
        OutputStream fileOut = response.getOutputStream();
        workbook.write(fileOut);
        workbook.close();
        fileOut.close();
    }

    public Row createHeader(Sheet sheet) {
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("선택");
        header.createCell(1).setCellValue("접수번호");
        header.createCell(2).setCellValue("접수일");
        header.createCell(3).setCellValue("접수유형");
        header.createCell(4).setCellValue("매출처");
        header.createCell(5).setCellValue("No order");
        header.createCell(6).setCellValue("상품코드");
        header.createCell(7).setCellValue("상품명");
        header.createCell(8).setCellValue("옵션코드");
        header.createCell(9).setCellValue("옵션명");
        header.createCell(10).setCellValue("주문자");
        header.createCell(11).setCellValue("수량");
        header.createCell(12).setCellValue("판매단가(VAT)");
        header.createCell(13).setCellValue("판매금액(VAT)");
        header.createCell(14).setCellValue("마감단가(VAT)");
        header.createCell(15).setCellValue("마감금액(VAT)");
        header.createCell(16).setCellValue("마감단가");
        header.createCell(17).setCellValue("마감금액");
        header.createCell(18).setCellValue("VAT");
        header.createCell(19).setCellValue("정율수수료");
        header.createCell(20).setCellValue("의뢰번호");
        header.createCell(21).setCellValue("결제형태");
        header.createCell(22).setCellValue("발주번호");
        header.createCell(23).setCellValue("최초의뢰번호");
        header.createCell(24).setCellValue("배송비");
        header.createCell(25).setCellValue("쿠폰");

        return header;
    }
}