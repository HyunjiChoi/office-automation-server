package com.caddie.voice.y1.controller;

import com.caddie.voice.y1.domain.ExcelData;
import com.caddie.voice.y1.domain.SalesList;
import com.caddie.voice.y1.domain.SalesReg;
import com.caddie.voice.y1.service.SalesService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Array;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(value="/vc-settlement/v1/sales")
public class SalesController {

    private final SalesService salesService;
    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @ApiOperation("통합주문 리스트 전체 조회")
    @GetMapping
    public List<Map<String, Object>> getSalesList() throws SQLException {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object>data;

        List calDtList = salesService.SalesCalDt();

        int cnt = salesService.SalesCalDt().size();

        List salesLnPList = null;

        for(int i=0; i <cnt; i++){
            data = new HashMap<>();

            salesLnPList = salesService.SalesLnPList(calDtList.get(i).toString());
            String str = "";
            // List<String> -> String 으로
            for(Object salesLnPlists : salesLnPList){
                str += salesLnPlists +" ";
            }

            data.put("calDt", calDtList.get(i).toString());
            data.put("lnPartner", str);

            System.out.println("str : " + str);

            dataList.add(data);

            //System.out.println("SalesLnPList() : " +  salesService.SalesLnPList(calDtList.get(i).toString()));
        }
        return dataList;
    }


    @ApiOperation("통합주문 파일 등록")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void registerSales(
            @RequestParam("calDt") String calDt,
            @RequestParam("frontVideoFile") MultipartFile frontVideoFile) throws IOException {

        List<ExcelData> dataList = new ArrayList<>();
        DateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
        //파일 이름
        String fileNm = frontVideoFile.getOriginalFilename();

        String extension = FilenameUtils.getExtension(frontVideoFile.getOriginalFilename());


        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀 파일만 업로드 해주세요.");
        }

        Workbook workbook = null;

        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(frontVideoFile.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new XSSFWorkbook(frontVideoFile.getInputStream());
        }

        Sheet worksheet = workbook.getSheetAt(0);
        System.out.println("if 전");
        System.out.println("fileNm >> "+fileNm);

        if(fileNm.contains("11번가")) {
            //11번가
            for (int i = 6; i < worksheet.getPhysicalNumberOfRows(); i++) {

                Row row = worksheet.getRow(i);

                ExcelData data = new ExcelData();

                System.out.println("before 파싱");

                data.setCalDt(calDt);
                data.setMarketCd("2001");
                System.out.println("1");
                data.setCalMonth(Integer.parseInt(row.getCell(1).getStringCellValue().substring(4, 6))); // 월
                System.out.println("2");
                data.setCalDay(Integer.parseInt(row.getCell(1).getStringCellValue().substring(6, 8))); // 일
                System.out.println("3");
                data.setSalesType("일반매출"); // 구분 default '일반매출'
                System.out.println("4");
                data.setAp("브이씨"); // 소구 default '브이씨'
                System.out.println("5");
                data.setNoOrder(row.getCell(1).getStringCellValue()); // 주문번호
                System.out.println("6");
                data.setLnPartner("11번가"); // 업체명 '파일명에서 가져오기'
                System.out.println("7");
                data.setNmSpitem(row.getCell(14).getStringCellValue()); // 품목
                System.out.println("8");
                if ((Integer.parseInt(row.getCell(16).getStringCellValue())) == 0) {
                    data.setQtSo(1);
                } else {
                    data.setQtSo(Integer.parseInt(row.getCell(16).getStringCellValue()));
                } // 수량
                System.out.println("9");
                data.setCm(Integer.parseInt(row.getCell(18).getStringCellValue())
                            / data.getQtSo()); // 판매단가 (판매금액/수량)
                // 색상 default 'null'
                System.out.println("10");
                data.setCa(Integer.parseInt(row.getCell(18).getStringCellValue())); // 판매금액 (판매금액합계)
                System.out.println("11");
                data.setSum((int)row.getCell(17).getNumericCellValue()); // 합계 (정산금액)
                System.out.println("13");
                data.setVat(data.getCa() - data.getSum()); // 수수료 (판매금액-합계)
                System.out.println("12");
                data.setPrice(data.getSum() / data.getQtSo()); // 금액 = 합계/수량 (정산금액/수량)
                //비고 default 'null'
                System.out.println("14");
                data.setNmCust(row.getCell(5).getStringCellValue()); // 구매자
                data.setCalWh(calDt.substring(5, 7) + "월완료");// 정산여부

                System.out.println("after 파싱");

                dataList.add(data);
                System.out.println("data list 값 : " + dataList);

                salesService.InsertSalesExcelData_11(data);
            }
        }
        else if(fileNm.contains("갤러리아")) {
            //갤러리아
            try {
                System.out.println("갤러리아");
                System.out.println("worksheet.getLastRowNum() >> " + worksheet.getLastRowNum());
                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    Row row = worksheet.getRow(i);

                    ExcelData data = new ExcelData();

                    System.out.println("before 파싱" + i + "번째");

                    data.setCalDt(calDt);
                    data.setMarketCd("2008");
                    System.out.println("1");
                    data.setCalMonth(Integer.parseInt(row.getCell(3).getStringCellValue().substring(5, 7))); // 월
                    System.out.println("2");
                    data.setCalDay(Integer.parseInt(row.getCell(3).getStringCellValue().substring(6, 8))); // 일
                    System.out.println("3");
                    data.setSalesType("일반매출"); // 구분 default '일반매출'
                    System.out.println("4");
                    data.setAp("브이씨"); // 소구 default '브이씨'
                    System.out.println("5");
                    data.setNoOrder(row.getCell(3).getStringCellValue()); // 주문번호
                    System.out.println("6");
                    data.setLnPartner("갤러리아"); // 업체명 '파일명에서 가져오기'
                    System.out.println("7");
                    data.setNmSpitem(row.getCell(6).getStringCellValue()); // 품목
                    System.out.println("8");
                    if (((int) row.getCell(9).getNumericCellValue()) == 0) {
                        data.setQtSo(1);
                    }else { data.setQtSo((int) row.getCell(9).getNumericCellValue()); } // 수량
                    System.out.println("9");
                    data.setCm((int) row.getCell(17).getNumericCellValue() / data.getQtSo()); // 판매단가 (판매금액 / 수량)
                    // 색상 default 'null'
                    System.out.println("10");
                    data.setSum((int) row.getCell(14).getNumericCellValue()); // 합계  (금액 * 수량) = 매입금액
                    System.out.println("11");
                    data.setPrice(data.getSum() / data.getQtSo()); // 금액(매입금액/수량)
                    System.out.println("12");
                    data.setCa(data.getCm() * data.getQtSo()); // 판매금액 (판매 단가*수량)
                    System.out.println("13");
                    data.setVat(data.getCa() - data.getSum()); // 수수료 (판매금액-합계)
                    System.out.println("14");
                    //비고 default 'null'
                    //data.setNmCust(row.getCell(5).getStringCellValue()); // 구매자
                    data.setCalWh(calDt.substring(5, 7) + "월완료");// 정산여부

                    System.out.println("after 파싱" + i + "번째");

                    dataList.add(data);
                    System.out.println("data list 값 : " + dataList);

                    salesService.InsertSalesExcelData_Gal(data);
                }
            }catch(Exception e){
                System.out.println("에러 : " + e.getMessage());
                e.getStackTrace();
            }}
        else if(fileNm.contains("네이버")){
            //네이버
            try {
                System.out.println("네이버");
                System.out.println("worksheet.getLastRowNum() >> " + worksheet.getLastRowNum());
                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    Row row = worksheet.getRow(i);

                    ExcelData data = new ExcelData();

                    System.out.println("before 파싱" + i + "번째");

                    data.setCalDt(calDt);
                    data.setMarketCd("2007");
                    System.out.println("1");
                    data.setCalMonth(Integer.parseInt(row.getCell(1).getStringCellValue().substring(4, 6))); // 월
                    System.out.println("2");
                    data.setCalDay(Integer.parseInt(row.getCell(1).getStringCellValue().substring(6, 8))); // 일
                    System.out.println("3");
                    data.setSalesType("일반매출"); // 구분 default '일반매출'
                    System.out.println("4");
                    data.setAp("브이씨"); // 소구 default '브이씨'
                    System.out.println("5");
                    data.setNoOrder(row.getCell(1).getStringCellValue()); // 주문번호
                    System.out.println("6");
                    data.setLnPartner("스토어팜"); // 업체명 '파일명에서 가져오기'
                    System.out.println("7");
                    data.setNmSpitem(row.getCell(4).getStringCellValue()); // 품목
                    System.out.println("8");
                    if (((int) row.getCell(10).getNumericCellValue()) == 0) {
                        data.setQtSo(1);
                    }else { data.setQtSo((int) row.getCell(10).getNumericCellValue()); } // 수량 K열
                    System.out.println("9");
                    data.setCa((int) row.getCell(11).getNumericCellValue()); // 판매금액 = 결제금액
                    System.out.println("10");
                    data.setCm(data.getCa() / data.getQtSo()); // 판매단가 (판매금액/수량)
                    // 색상 default 'null'
                    System.out.println("11");
                    data.setSum((int) row.getCell(23).getNumericCellValue()); // 합계 (금액 * 수량) = X열
                    System.out.println("12");
                    data.setPrice(data.getSum() / data.getQtSo()); // 금액(합계/수량)
                    System.out.println("13");
                    data.setVat(data.getCa() - data.getSum()); // 수수료 (판매금액-합계)
                    System.out.println("14");
                    //비고 default 'null'
                    data.setNmCust(row.getCell(5).getStringCellValue()); // 구매자
                    data.setCalWh(calDt.substring(5, 7) + "월완료");// 정산여부

                    System.out.println("after 파싱" + i + "번째");

                    dataList.add(data);
                    System.out.println("data list 값 : " + dataList);

                    salesService.InsertSalesExcelData_N(data);
                }

            }catch(Exception e){
                System.out.println("에러 : " + e.getMessage());
                e.getStackTrace();
            }
        }else if(fileNm.contains("옥션")) {
            //옥션
            try {
                System.out.println("옥션");
                System.out.println("worksheet.getLastRowNum() >> " + worksheet.getLastRowNum());
                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    Row row = worksheet.getRow(i);

                    ExcelData data = new ExcelData();

                    System.out.println("before 파싱" + i + "번째");

                    data.setCalDt(calDt);
                    data.setMarketCd("2003");
                    System.out.println("1");
                    System.out.println("날짜날짜 : " + sdFormat.format((int) row.getCell(2).getNumericCellValue()));
                    /*data.setCalMonth(Integer.parseInt(
                             simpleDataFormat.format( (int) row.getCell(2).getNumericCellValue())
                             .substring(5, 7))); // 월*/
                    System.out.println("2");
                    /*data.setCalDay(Integer.parseInt(
                             sdFormat.format( (int) row.getCell(2).getNumericCellValue())
                            .substring(8,10))); // 일*/
                    System.out.println("3");
                    data.setSalesType("일반매출"); // 구분 default '일반매출'
                    System.out.println("4");
                    data.setAp("브이씨"); // 소구 default '브이씨'
                    System.out.println("5");
                    data.setNoOrder(Integer.toString((int) row.getCell(7).getNumericCellValue())); // 주문번호
                    System.out.println("6");
                    data.setLnPartner("옥션"); // 업체명 '파일명에서 가져오기'
                    System.out.println("7");
                    data.setNmSpitem(row.getCell(8).getStringCellValue()); // 품목
                    System.out.println("8");
                    if (((int) row.getCell(9).getNumericCellValue()) == 0) { data.setQtSo(1); }
                    else { data.setQtSo((int) row.getCell(9).getNumericCellValue()); } // 수량
                    System.out.println("9");
                    data.setCa((int) row.getCell(12).getNumericCellValue()); // 판매금액 (판매 단가*수량) = 상품금액
                    System.out.println("10");
                    data.setCm(data.getCa() / data.getQtSo()); // 판매단가 (판매금액 / 수량)
                    // 색상 default 'null'
                    System.out.println("11");
                    data.setSum((int) row.getCell(15).getNumericCellValue()); // 합계 = 송금액
                    System.out.println("12");
                    data.setPrice(data.getSum() / data.getQtSo()); // 금액(합계/수량)
                    System.out.println("13");
                    if ((data.getCa() - data.getSum()) < 0) { data.setVat(0); }
                    else { data.setVat(data.getCa() - data.getSum()); }  // 수수료 (판매금액-합계)
                    System.out.println("14");
                    //비고 default 'null'
                    data.setNmCust(row.getCell(11).getStringCellValue()); // 구매자
                    data.setCalWh(calDt.substring(5, 7) + "월완료");// 정산여부
                    System.out.println("after 파싱" + i + "번째");
                    dataList.add(data);
                    System.out.println("data list 값 : " + dataList);

                    salesService.InsertSalesExcelData_Au(data);
                }
            }catch (Exception e){ e.getStackTrace(); }
        }else if(fileNm.contains("인터파크")){
            //인터파크
            try{
                for (int i = 14; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    Row row = worksheet.getRow(i);

                    ExcelData data = new ExcelData();

                    System.out.println("before 파싱" + i + "번째");

                    data.setCalDt(calDt);
                    data.setMarketCd("2006");
                    System.out.println("1");
                    data.setCalMonth(Integer.parseInt(row.getCell(4).getStringCellValue().substring(4, 6))); // 월
                    System.out.println("2");
                    data.setCalDay(Integer.parseInt(row.getCell(4).getStringCellValue().substring(6, 8))); // 일
                    System.out.println("3");
                    data.setSalesType("일반매출"); // 구분 default '일반매출'
                    System.out.println("4");
                    data.setAp("브이씨"); // 소구 default '브이씨'
                    System.out.println("5");
                    data.setNoOrder(row.getCell(4).getStringCellValue()); // 주문번호
                    System.out.println("6");
                    data.setLnPartner("인터파크"); // 업체명 '파일명에서 가져오기'
                    System.out.println("7");
                    data.setNmSpitem(row.getCell(10).getStringCellValue());// 품목
                    System.out.println("8");
                    if (((int) row.getCell(24).getNumericCellValue()) == 0) {
                        data.setQtSo(1);
                    } else {
                        data.setQtSo((int) row.getCell(24).getNumericCellValue());
                    } // 수량
                    System.out.println("9");
                    data.setCm((int) row.getCell(14).getNumericCellValue()); // 판매단가 = 판매금액
                    System.out.println("10");
                    data.setCa(data.getCm() * data.getQtSo()); // 판매금액 (판매 단가*수량) = 상품금액
                    // 색상 default 'null'
                    System.out.println("11");
                    data.setSum((int) row.getCell(39).getNumericCellValue()); // 합계 = AN열
                    System.out.println("12");
                    data.setPrice(data.getSum() / data.getQtSo()); // 금액(합계/수량)
                    System.out.println("13");
                    if ((data.getCa() - data.getSum()) < 0) {
                        data.setVat(0);
                    } else {
                        data.setVat(data.getCa() - data.getSum());
                    }  // 수수료 (판매금액-합계)
                    System.out.println("14");
                    //비고 default 'null'
                    data.setNmCust(row.getCell(7).getStringCellValue()); // 구매자
                    data.setCalWh(calDt.substring(5, 7) + "월완료");// 정산여부

                    System.out.println("after 파싱" + i + "번째");

                    dataList.add(data);
                    System.out.println("data list 값 : " + dataList);

                    salesService.InsertSalesExcelData_In(data);
                }
            }catch(Exception e){ e.printStackTrace(); }
        }else if(fileNm.contains("하프클럽")){
            //하프클럽
            try{
                for (int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    Row row = worksheet.getRow(i);

                    ExcelData data = new ExcelData();

                    System.out.println("before 파싱" + i + "번째");

                    data.setCalDt(calDt);
                    data.setMarketCd("2006");
                    System.out.println("1");
                    data.setCalMonth(Integer.parseInt(
                            (Integer.toString(
                                    (int)row.getCell(4).getNumericCellValue())
                                    .substring(2, 4)))); // 월
                    System.out.println("2");
                    data.setCalDay(Integer.parseInt(
                            (Integer.toString(
                                    (int)row.getCell(4).getNumericCellValue())
                                    .substring(6, 8)))); // 일
                    System.out.println("3");
                    data.setSalesType("일반매출"); // 구분 default '일반매출'
                    System.out.println("4");
                    data.setAp("브이씨"); // 소구 default '브이씨'
                    System.out.println("5");
                    data.setNoOrder(Integer.toString((int) row.getCell(4).getNumericCellValue())); // 주문번호
                    System.out.println("6");
                    data.setLnPartner("하프클럽"); // 업체명 '파일명에서 가져오기'
                    System.out.println("7");
                    data.setNmSpitem(row.getCell(7).getStringCellValue());// 품목
                    System.out.println("8");
                    if (((int) row.getCell(14).getNumericCellValue()) == 0) { data.setQtSo(1); }
                    else { data.setQtSo((int) row.getCell(14).getNumericCellValue()); } // 수량
                    System.out.println("9");
                    data.setPrice((int) row.getCell(22).getNumericCellValue()); // 금액 = wlrmqrmador
                    System.out.println("10");
                    data.setCm((int) row.getCell(13).getNumericCellValue()); // 판매단가 = 판매금액
                    // 색상 default 'null'
                    System.out.println("11");
                    data.setSum(data.getPrice() * data.getQtSo()); // 합계 = AN열
                    System.out.println("12");
                    data.setCa(data.getCm() * data.getQtSo()); // 판매금액 (판매 단가*수량)
                    System.out.println("13");
                    if ((data.getCa() - data.getSum()) < 0) {
                        data.setVat(0);
                    } else {
                        data.setVat(data.getCa() - data.getSum());
                    }  // 수수료 (판매금액-합계)
                    System.out.println("14");
                    //비고 default 'null'
                    data.setNmCust(row.getCell(6).getStringCellValue()); // 구매자
                    data.setCalWh(calDt.substring(5, 7) + "월완료");// 정산여부

                    System.out.println("after 파싱" + i + "번째");

                    dataList.add(data);
                    System.out.println("data list 값 : " + dataList);

                    salesService.InsertSalesExcelData_Ha(data);
                }
            }catch(Exception e){ e.printStackTrace(); }
        }
    }


    @ApiOperation("통합주문 리스트 삭제")
    @DeleteMapping
    public void deregisterSales(
            @RequestParam("calDt") String calDt,
            @RequestParam("lnPartner") String lnPartner) {

        salesService.deregisterSales(calDt, lnPartner);
        salesService.deregisterSalesWorkSt(calDt, lnPartner);
    }

    @ApiOperation("통합주문 상세조회")
    @GetMapping("/detail")
    public List<SalesList> salesDetail(
            @ApiParam(
                    name = "calDt",
                    type = "String",
                    value = "연월",
                    example = "2021-02-24")
            @RequestParam("calDt") String calDt) throws IOException {

        return salesService.salesDetail(calDt);
    }



/*
            }else if(fileName.equals("G마켓")){
                for(int i = 1; i < r.getPhysicalNumberOfCells(); i++) {

                    SalesReg data = new SalesReg();

                    data.setMarketCd("2002");
                    data.setCalMonth(r.getCell(6).getStringCellValue().substring(5, 7)); // 월
                    data.setCalDay(r.getCell(6).getStringCellValue().substring(8)); // 일
                    //data.setSalesType(row.getCell(1).getStringCellValue()); // 구분 default '일반매출'
                    //data.setAp(row.getCell(2).getStringCellValue()); // 소구 default '브이씨'
                    data.setNoOrder(r.getCell(1).getStringCellValue()); // 주문번호
                    data.setLnPartner("지마켓"); // 업체명 '파일명에서 가져오기'
                    data.setNmSpitem(r.getCell(4).getStringCellValue()); // 품목
                    *//*data.setCm((int) r.getCell(13).getNumericCellValue())*//*; // 판매단가
                    //판매단가 미쳤나 db에 테이블 구성해서 거기서 값 가져와야함.......
                    //얘 떄문에 판매금액 수수료도 계산이 안된다고....
                    //data.setColor(row.getCell(2).getStringCellValue()); // 색상 default 'null'
                    data.setQtSo((int) r.getCell(10).getNumericCellValue()); // 수량
                    *//*data.setCa((int) r.getCell(13).getNumericCellValue()
                            * (int) r.getCell(14).getNumericCellValue());*//* // 판매금액 (판매단가*수량)
                    *//*data.setVat((int) r.getCell(13).getNumericCellValue()
                            * (int) r.getCell(14).getNumericCellValue()
                            - (int) r.getCell(25).getNumericCellValue());*//* // 수수료 엑셀 함수 사용 (판매금액-합계)
                    data.setPrice((int) r.getCell(15).getNumericCellValue()); // 금액
                    data.setSum((int) r.getCell(15).getNumericCellValue()
                            * (int) r.getCell(10).getNumericCellValue()); // 합계 (금액 * 수량)
                    //data.setEtc(row.getCell(2).getStringCellValue()); // 비고 default 'null'
                    data.setNmCust(r.getCell(5).getStringCellValue()); // 구매자
                    data.setCalWh(r.getCell(9).getStringCellValue().substring(5, 7) + "월완료"); // 정산여부 default 'null'
                }

            }

        }
*/


    }


