package com.caddie.voice.y1.service;

import com.caddie.voice.y1.dao.SalesDao;

import com.caddie.voice.y1.domain.*;
import com.caddie.voice.y1.dto.UnitPrice;
import com.caddie.voice.y1.exception.common.ErrorResponse;
import com.caddie.voice.y1.exception.common.ExceptionType;
import com.caddie.voice.y1.exception.exceptions.ExistSalesException;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.round;

@AllArgsConstructor
@Service
public class SalesService {

    SalesDao salesDao;
    HashMap<String, Object> unitPList= new HashMap<>();

    public List SalesCalDt() { return salesDao.SalesCalDt(); }
    public List<String[]> SalesLnPList(String calDt) { return salesDao.SalesLnPList(calDt); }

    public void deregisterSales(String calDt) {
        salesDao.deregisterSales(calDt);
    }
    public void deregisterSalesWorkSt(String calDt) {
        salesDao.deregisterSalesWorkSt(calDt);
    }

    public List<SalesDetail> salesDetail(String calDt) {

        return salesDao.salesDetail(calDt);
    }

    private int getProductName(int month, int day, String product) {
        int result = 0;

        if(product.contains("A2")) {
            result = 199000;
        }else if(product.contains("EL1")){
            result = 199000;
        }else if(product.contains("SL1")) {
            result = 659000;
        }else if(product.contains("T8")) {
            result = 399000;
        }else if(product.contains("L3파우치")) {
            result = 365000;
        }else if(product.contains("T4G-B")) {
            result = 299000;
        }else if(product.contains("L4")) {
            result = 299000;
        }else if(product.contains("T5-W")) {
            result = 374000;
        }else if(product.contains("T5-B")) {
            result = 374000;
        }else if(product.contains("CL1")) {
            result = 459000;
        }else if(product.contains("SC200+")) {
            result = 319000;
        }else if(product.contains("70초록")) {
            result = 99000;
        }else if(product.contains("70검정+")) {
            result = 69000;
        }else if(product.contains("T3충전기")) {
            result = 20000;
        }else if(product.contains("T4충전기")) {
            result = 20000;
        }else if(product.contains("T5충전기")) {
            result = 20000;
        }else if(product.contains("충전기")) {
            result = 20000;
        }else if(product.contains("L3파우치")) {
            result = 30000;
        }else if(product.contains("SL1파우치")) {
            result = 50000;
        }else if(product.contains("양피장갑")) {
            result = 25000;
        }else if(product.contains("T4G-W")) {
            result = 299000;
        }else if(product.contains("T4-W")) {
            result = 349000;
        }else if(product.contains("T1충전기")) {
            result = 25000;
        }else if(product.contains("T6")) {
            result = 399000;
        }else if(product.contains("SC300")) {
            result = 699000;
        }else if(product.contains("SC300파우치")) {
            result = 49900;
        }else if(product.contains("GL1")) {
            result = 599000;
        }else if(product.contains("G1")) {
            result = 319000;
        }else if(product.contains("레이저파우치")) {
            result = 69000;
        }else if(product.contains("T6나토밴드")) {
            result = 25900;
        }else if(product.contains("A1")) {
            result = 279000;
        }else if(product.contains("T6나토밴드할인")) {
            result = 19900;
        }else if(product.contains("T7")) {
            result = 399000;
        }else if(product.contains("VC4")) {
            if (product.contains("에이밍")){
                return 169000;
            }
            result = 129000;
        }else if(product.contains("SL2")) {
            result = 799000;
        }else if(product.contains("L5")) {
            result = 299000;
        }else if(product.contains("CL2")) {
            if(month <= 1 && day < 15){
                result = 529000;
            }else{
                result = 499000;
            }
        }else if(product.contains("나토밴드2")
                || product.contains("나토밴드")
                || product.contains("레드")
                || product.contains("네이비")) {
            result = 24900;
        }else if (product.contains("sc200")
                || product.contains("SC200")){
            result = 319000;
        }else if (product.contains("배송비")){
            result = 3000;
        }else if(product.contains("혜택정산")){
            result = 0;
        }

        return result;
    }

    public void InsertSalesExcelData(String calDt, MultipartFile frontVideoFile) throws IOException {

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

        if(fileNm.contains("11번가")) {
            if(salesDao.getSalesByCalDtAndLnPartner(calDt, "11번가").size() != 0) {
                throw new ExistSalesException(ExceptionType.ExistSalesException);
            }
           try {

                //UnitPrice(calDt, frontVideoFile);

                for (int i = 6; i < worksheet.getPhysicalNumberOfRows(); i++) {

                    Row row = worksheet.getRow(i);

                    if(row.getCell(1).getStringCellValue().equals("")) {
                        break;
                    }

                    ExcelData data = new ExcelData();

                    data.setCalDt(calDt);
                    data.setMarketCd("2001");
                    data.setCalMonth(Integer.parseInt(row.getCell(1).getStringCellValue().substring(4, 6))); // 월
                    data.setCalDay(Integer.parseInt(row.getCell(1).getStringCellValue().substring(6, 8))); // 일
                    data.setSalesType("일반매출"); // 구분
                    data.setAp("브이씨"); // 소구
                    data.setNoOrder(row.getCell(1).getStringCellValue()); // 주문번호
                    data.setLnPartner("11번가"); // 업체명
                    if(Integer.parseInt(row.getCell(7).getStringCellValue()) == 6000     // 품목
                        || Integer.parseInt(row.getCell(7).getStringCellValue()) == 3000){
                        data.setNmSpitem("배송비");
                    } else {
                        data.setNmSpitem(row.getCell(3).getStringCellValue());
                    }
                    // 색상
                    if(data.getNmSpitem().equals("배송비")){   // 수량
                        data.setQtSo(1);
                    } else { data.setQtSo(Integer.parseInt(row.getCell(5).getStringCellValue()));
                    }
                    data.setCa(Integer.parseInt(row.getCell(7).getStringCellValue())); // 판매금액 = 판매금액합계
                    /*System.out.println("if >> "+unitPList.get("A2"));
                    if(unitPList.get().contains(data.getNmSpitem())){
                        data.setCm(Integer.parseInt(unitPList.get(data.getNmSpitem()).toString()) );
                        System.out.println("data.getCm>" + data.getCm());
                    }*/
                    if(data.getCa() == 6000){
                        data.setCm(data.getCa());
                    }else {
                        data.setCm(getProductName((int) data.getCalMonth(), (int) data.getCalDay(), data.getNmSpitem())); // 판매단가
                    }
                    data.setPrice((int) row.getCell(6).getNumericCellValue()); // 금액 = 정산금액
                    data.setSum(data.getPrice() * data.getQtSo()); // 합계 = 금액 * 수량
                    data.setVat(data.getCa() - data.getSum()); // 수수료 (판매금액-합계)
                    //비고
                    data.setNmCust(row.getCell(2).getStringCellValue()); // 구매자
                    data.setCalWh(calDt.substring(5, 7) + "월완료");// 정산여부
                    dataList.add(data);

                    salesDao.InsertSalesExcelData(data);
                    salesDao.SalesWorkStData(calDt, data.getMarketCd(), data.getLnPartner());
                }
             }catch (Exception e){e.printStackTrace();}
        }else if(fileNm.contains("갤러리아")) {
            if(salesDao.getSalesByCalDtAndLnPartner(calDt, "갤러리아").size() != 0) {
                throw new ExistSalesException(ExceptionType.ExistSalesException);
            }
            try {
                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

                    Row row = worksheet.getRow(i);

                    if(row.getCell(1).getStringCellValue().equals("")) {
                        break;
                    }

                    ExcelData data = new ExcelData();

                    data.setCalDt(calDt);
                    data.setMarketCd("2008");
                    data.setCalMonth(Integer.parseInt(row.getCell(1).getStringCellValue().substring(4, 6))); // 월
                    data.setCalDay(Integer.parseInt(row.getCell(1).getStringCellValue().substring(6, 8))); // 일
                    data.setSalesType("일반매출"); // 구분
                    data.setAp("브이씨"); // 소구
                    data.setNoOrder(row.getCell(1).getStringCellValue()); // 주문번호
                    data.setLnPartner("갤러리아"); // 업체명
                    data.setNmSpitem(row.getCell(3).getStringCellValue()); // 품목 = 상품명
                    data.setCm( getProductName((int) data.getCalMonth(), (int) data.getCalDay(), data.getNmSpitem()));  // 판매단가
                    data.setQtSo((int) row.getCell(4).getNumericCellValue()); // 수량
                    // 색상
                    data.setCa(data.getCm() * data.getQtSo()); // 판매금액 (판매단가 * 수량)
                    data.setPrice((int) row.getCell(5).getNumericCellValue()); // 금액 = 매임금액
                    data.setSum( data.getPrice() * data.getQtSo()); // 합계  (금액 * 수량)
                    data.setVat(data.getCa() - data.getSum()); // 수수료 (판매금액-합계)
                    // 비고
                    data.setNmCust(" ");
                    data.setCalWh(calDt.substring(5, 7) + "월완료");// 정산여부

                    dataList.add(data);

                    salesDao.InsertSalesExcelData(data);
                    salesDao.SalesWorkStData(calDt, data.getMarketCd(), data.getLnPartner());
                }
            } catch (Exception e) { e.getStackTrace(); }
        }else if(fileNm.contains("네이버")){
            if(salesDao.getSalesByCalDtAndLnPartner(calDt, "네이버").size() != 0) {
                throw new ExistSalesException(ExceptionType.ExistSalesException);
            }
            //네이버
            try {
                System.out.println("worksheet.getPhysicalNumberOfRows()" + worksheet.getPhysicalNumberOfRows());

                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    Row row = worksheet.getRow(i);
                    if(row.getCell(1).getStringCellValue().equals("")) {
                        break;
                    }

                    ExcelData data = new ExcelData();

                    data.setCalDt(calDt);
                    data.setMarketCd("2007");
                    data.setCalMonth(Integer.parseInt(row.getCell(1).getStringCellValue().substring(4, 6))); // 월
                    data.setCalDay(Integer.parseInt(row.getCell(1).getStringCellValue().substring(6, 8))); // 일
                    data.setSalesType("일반매출"); // 구분
                    data.setAp("브이씨"); // 소구
                    data.setNoOrder(row.getCell(1).getStringCellValue()); // 주문번호
                    data.setLnPartner("스마트스토어"); // 업체명
                    if(row.getCell(2).getStringCellValue().equals("포토/동영상 리뷰") // 품목
                        ||row.getCell(2).getStringCellValue().equals("텍스트 리뷰")){
                        data.setNmSpitem("혜택정산");
                    }else if (row.getCell(2).getStringCellValue().equals("")) {
                        data.setNmSpitem("배송비");
                    }else{
                        data.setNmSpitem(row.getCell(2).getStringCellValue());
                    }
                    if(data.getNmSpitem().equals("배송비") && (int)row.getCell(4).getNumericCellValue() > 3000){
                        data.setCm((int)row.getCell(4).getNumericCellValue());
                    }else {
                        data.setCm(getProductName((int) data.getCalMonth(), (int) data.getCalDay(), data.getNmSpitem())); // 판매단가
                    }
                    if(data.getNmSpitem().equals("혜택정산")) {  // 수량 (결제금액/판매단가)
                        data.setQtSo(-1);
                    }else{
                        data.setQtSo( round((int) row.getCell(4).getNumericCellValue() / data.getCm()));
                    }
                    if(data.getNmSpitem().equals("혜택정산")){  // 판매금액
                        data.setCa(0);
                    }else {
                        data.setCa(data.getCm() * data.getQtSo());
                    }
                    // 색상
                    data.setSum((int) row.getCell(19).getNumericCellValue()); // 합계 = AD열 (T열)
                    data.setPrice(round(data.getSum() / data.getQtSo())); // 금액(AD열 / 수량)
                    data.setVat(data.getCa() - data.getSum()); // 수수료 (판매금액-합계)
                    //비고
                    data.setNmCust(row.getCell(3).getStringCellValue()); // 구매자
                    data.setCalWh(calDt.substring(5, 7) + "월완료");// 정산여부

                    dataList.add(data);

                    salesDao.InsertSalesExcelData(data);
                    salesDao.SalesWorkStData(calDt, data.getMarketCd(), data.getLnPartner());
                }
            }catch(Exception e){e.printStackTrace();}
        }else if(fileNm.contains("옥션")) {
            if(salesDao.getSalesByCalDtAndLnPartner(calDt, "옥션").size() != 0) {
                throw new ExistSalesException(ExceptionType.ExistSalesException);
            }
            //옥션
            try {
                System.out.println("worksheet.getPhysicalNumberOfRows() : " + worksheet.getPhysicalNumberOfRows());
                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

                    Row row = worksheet.getRow(i);

                    if (row.getCell(3).getStringCellValue().equals("")) {
                        break;
                    }

                    ExcelData data = new ExcelData();

                    data.setCalDt(calDt);
                    data.setMarketCd("2003");
                    if (row.getCell(3).getStringCellValue().equals("배송비")) { // 월
                        data.setCalMonth(Integer.parseInt(data.getCalDt().substring(5, 7)));
                    } else {
                        data.setCalMonth(Integer.parseInt(
                                sdFormat.format(row.getCell(1).getDateCellValue())
                                        .substring(4, 6)));
                    }
                    if (row.getCell(3).getStringCellValue().equals("배송비")) {   // 일
                        data.setCalDay(Integer.parseInt(data.getCalDt().substring(8)));
                    } else {
                        data.setCalDay(Integer.parseInt(
                                sdFormat.format(row.getCell(1).getDateCellValue())
                                        .substring(6)));
                    }
                    data.setSalesType("일반매출"); // 구분
                    data.setAp("브이씨"); // 소구
                    data.setNoOrder(Integer.toString((int) row.getCell(2).getNumericCellValue()));  // 주문번호
                    data.setLnPartner("옥션"); // 업체명
                    data.setNmSpitem(row.getCell(3).getStringCellValue()); // 품목
                    data.setCm( getProductName((int) data.getCalMonth(), (int) data.getCalDay(), data.getNmSpitem())); // 판매단가
                    // 색상
                    data.setPrice((int) row.getCell(8).getNumericCellValue()); // 금액 = 송금액
                    if (data.getNmSpitem().equals("배송비")) {  // 수량 (상품금액/송금액)
                        data.setQtSo(1);
                    } else {
                        data.setQtSo( (int) (data.getCm() / data.getPrice()));
                    }
                    data.setCa(data.getCm() * data.getQtSo()); // 판매금액 (판매 단가 * 수량)
                    data.setSum(data.getPrice() * data.getQtSo()); // 합계 = 금액 * 수량
                    data.setVat(data.getCm() - data.getSum()); // 수수료 (판매금액 - 합계)
                    //비고
                    data.setNmCust(row.getCell(4).getStringCellValue()); // 구매자
                    data.setCalWh(calDt.substring(5, 7) + "월완료");// 정산여부

                    dataList.add(data);

                    salesDao.InsertSalesExcelData(data);
                    salesDao.SalesWorkStData(calDt, data.getMarketCd(), data.getLnPartner());
                }
            }catch (Exception e){ e.getStackTrace(); }
        }else if(fileNm.contains("인터파크")){
            if(salesDao.getSalesByCalDtAndLnPartner(calDt, "인터파크").size() != 0) {
                throw new ExistSalesException(ExceptionType.ExistSalesException);
            }
            //인터파크
            try{
                for (int i = 13; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    Row row = worksheet.getRow(i);

                    if ( Double.parseDouble(row.getCell(2).getStringCellValue()) == 0.0) {
                            break;
                    }

                    ExcelData data = new ExcelData();

                    data.setCalDt(calDt);
                    data.setMarketCd("2006");
                    data.setCalMonth(Integer.parseInt(row.getCell(2).getStringCellValue().substring(5, 7))); // 월
                    data.setCalDay(Integer.parseInt(row.getCell(2).getStringCellValue().substring(7, 9))); // 일
                    data.setSalesType("일반매출"); // 구분
                    data.setAp("브이씨"); // 소구
                    data.setNoOrder(row.getCell(2).getStringCellValue()); // 주문번호
                    data.setLnPartner("인터파크"); // 업체명
                    data.setNmSpitem(row.getCell(5).getStringCellValue());// 품목
                    if (((int) row.getCell(17).getNumericCellValue()) == 0) { // 수량
                        data.setQtSo(1);
                    } else {
                        data.setQtSo((int) row.getCell(17).getNumericCellValue());
                    }
                    data.setCm(getProductName((int) data.getCalMonth(), (int) data.getCalDay(), data.getNmSpitem())); // 판매단가 = 판매금액
                    data.setCa(data.getCm() * data.getQtSo()); // 판매금액 (판매 단가*수량) = 상품금액
                    // 색상
                    data.setPrice((int) row.getCell(26).getNumericCellValue()); // 금액 = AA열
                    data.setSum(data.getPrice() * data.getQtSo()); // 합계 (금액 * 수량)
                    data.setVat(data.getCa() - data.getSum());  // 수수료 (판매금액-합계)
                    //비고
                    data.setNmCust(row.getCell(3).getStringCellValue()); // 구매자
                    data.setCalWh(calDt.substring(5, 7) + "월완료");// 정산여부

                    dataList.add(data);

                    salesDao.InsertSalesExcelData(data);
                    salesDao.SalesWorkStData(calDt, data.getMarketCd(), data.getLnPartner());
                }
            } catch (NullPointerException e ) {
                System.out.println("null");
            } catch(Exception e) {
                e.printStackTrace();
            }

        }else if(fileNm.contains("하프클럽")) {
            if(salesDao.getSalesByCalDtAndLnPartner(calDt, "하프클럽").size() != 0) {
                throw new ExistSalesException(ExceptionType.ExistSalesException);
            }
            //하프클럽
            try{
                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

                    Row row = worksheet.getRow(i);

                    if (row.getCell(1).getStringCellValue().equals("")){
                        break;
                    }

                    ExcelData data = new ExcelData();

                    data.setCalDt(calDt);
                    data.setMarketCd("2009");
                    data.setCalMonth(Integer.parseInt(row.getCell(1).getStringCellValue().substring(2, 4))); // 월
                    data.setCalDay(Integer.parseInt(row.getCell(1).getStringCellValue().substring(4, 6))); // 일
                    data.setSalesType("일반매출"); // 구분
                    data.setAp("브이씨"); // 소구
                    data.setNoOrder( row.getCell(1).getStringCellValue()); // 주문번호
                    data.setLnPartner("하프클럽"); // 업체명
                    data.setNmSpitem(row.getCell(3).getStringCellValue());// 품목 = 상품명
                    data.setCm(getProductName((int) data.getCalMonth(), (int) data.getCalDay(), data.getNmSpitem())); // 판매단가
                    data.setQtSo(Integer.parseInt(row.getCell(6).getStringCellValue()));// 수량 = 실출고수량
                    if((int) row.getCell(8).getNumericCellValue() == 0){    // 금액 = 실지급금액
                        data.setPrice(0);
                    }else if( (int) row.getCell(8).getNumericCellValue() < 0){
                        data.setPrice( (int)row.getCell(8).getNumericCellValue() * -1);
                    }else {
                        data.setPrice((int) row.getCell(8).getNumericCellValue());
                    }
                    // 색상
                    data.setSum(data.getPrice() * data.getQtSo()); // 합계 ( 금액 * 수량)
                    data.setCa(data.getCm() * data.getQtSo());  // 판매금액 (판매 단가 * 수량)
                    data.setVat(data.getCa() - data.getSum());  // 수수료 (판매금액 - 합계)
                    // 비고
                    data.setNmCust(row.getCell(5).getStringCellValue()); // 구매자 = 수령인
                    data.setCalWh(calDt.substring(5, 7) + "월완료");// 정산여부


                    dataList.add(data);

                    salesDao.InsertSalesExcelData(data);
                    salesDao.SalesWorkStData(calDt, data.getMarketCd(), data.getLnPartner());
                }
            } catch (NullPointerException e ) {
                System.out.println("null");
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else if(fileNm.contains("G마켓")
                || fileNm.contains("지마켓")
                || fileNm.contains("g마켓")) {
            if (salesDao.getSalesByCalDtAndLnPartner(calDt, "G마켓").size() != 0
                    || salesDao.getSalesByCalDtAndLnPartner(calDt, "지마켓").size() != 0
                    || salesDao.getSalesByCalDtAndLnPartner(calDt, "g마켓").size() != 0) {
                throw new ExistSalesException(ExceptionType.ExistSalesException);
            }
            //g마켓
            try {
                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

                    Row row = worksheet.getRow(i);

                    if (row.getCell(1).getStringCellValue().equals("")) {
                        break;
                    }

                    ExcelData data = new ExcelData();

                    data.setCalDt(calDt);
                    data.setMarketCd("2002");
                    if ((row.getCell(2).getStringCellValue()).equals("배송비")) { // 월 (입금확인일)
                        data.setCalMonth(Integer.parseInt(calDt.substring(5, 7)));
                    } else {
                        data.setCalMonth(Integer.parseInt(row.getCell(4).getStringCellValue().substring(5, 7)));
                    }
                    if (row.getCell(2).getStringCellValue().equals("배송비")) { // 일 (입금확인일)
                        data.setCalDay(Integer.parseInt(calDt.substring(8)));
                    } else {
                        data.setCalDay(Integer.parseInt(row.getCell(4).getStringCellValue().substring(8)));
                    }
                    data.setSalesType("일반매출"); // 구분
                    data.setAp("브이씨"); // 소구
                    data.setNoOrder(row.getCell(1).getStringCellValue()); // 주문번호
                    data.setLnPartner("지마켓"); // 업체명
                    data.setNmSpitem(row.getCell(2).getStringCellValue());// 품목 = 상품명
                    if(data.getNmSpitem().equals("배송비")){   // 수량 = 구매수량
                        data.setQtSo(1);
                    }else{
                        data.setQtSo(Integer.parseInt(row.getCell(5).getStringCellValue()));
                    }
                    data.setSum((int) row.getCell(7).getNumericCellValue()); // 합계 = 송금액
                    data.setCm(getProductName((int) data.getCalMonth(), (int) data.getCalDay(), data.getNmSpitem())); // 판매단가
                   /* if (data.getNmSpitem().equals("배송비")){      // 판매 금액 (판매단가 * 수량)
                        data.setCa(3000);
                    }else {  data.setCa(Integer.parseInt(row.getCell(6).getStringCellValue()
                            .replaceAll(",", "")));}*/
                    data.setCa(data.getCm() * data.getQtSo());   // 판매 금액 (판매단가 * 수량)
                    // 색상
                    if (data.getQtSo() == 0) {
                        data.setPrice(data.getSum() / 1); // 금액 (합계 / 수량)
                    } else {
                        data.setPrice(data.getSum() / data.getQtSo());
                    }
                    data.setVat(data.getCa() - data.getSum()); // 수수료 (판매금액-합계)
                    //비고
                    if (data.getNmSpitem().equals("배송비")) {  // 구매자.
                        data.setNmCust("");
                    } else {
                        data.setNmCust(row.getCell(3).getStringCellValue());
                    }
                    data.setCalWh(calDt.substring(5, 7) + "월완료");// 정산여부

                    dataList.add(data);

                    salesDao.InsertSalesExcelData(data);
                    salesDao.SalesWorkStData(calDt, data.getMarketCd(), data.getLnPartner());
                }
/*            } catch (NumberFormatException e) {
                System.out.println("numberFormat");*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    public void UnitPrice(String calDt, MultipartFile frontVideoFile) throws IOException {

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
        try {
            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

                Row row = worksheet.getRow(i);

                if (row.getCell(0).getStringCellValue().equals("")) {
                    break;
                }

                UnitPrice unitPrice = new UnitPrice();

                unitPrice.setItem(row.getCell(0).getStringCellValue());     // 상품명
                unitPrice.setPrice((int) row.getCell(1).getNumericCellValue()); // 금액

                unitPList.put(unitPrice.getItem(), unitPrice);

            }

            System.out.println("unitPrice 개수 : " + unitPList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void downSales(String calDt, HttpServletResponse response) throws IOException {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();

            List<SalesDetail> salesDetailData = salesDao.salesDetail(calDt);

            int rowCnt = 1;

            Row row;
            Row header = sheet.createRow(0);

            // 헤더
            header.createCell(0).setCellValue("월");
            header.createCell(1).setCellValue("일");
            header.createCell(2).setCellValue("구분");
            header.createCell(3).setCellValue("소구");
            header.createCell(4).setCellValue("주문번호");
            header.createCell(5).setCellValue("업체명");
            header.createCell(6).setCellValue("품목");
            header.createCell(7).setCellValue("판매단가");
            header.createCell(8).setCellValue("색상");
            header.createCell(9).setCellValue("수량");
            header.createCell(10).setCellValue("판매금액");
            header.createCell(11).setCellValue("수수료");
            header.createCell(12).setCellValue("금액");
            header.createCell(13).setCellValue("합계");
            header.createCell(14).setCellValue("비고");
            header.createCell(15).setCellValue("구매자");
            header.createCell(16).setCellValue("정산여부");

            // 데이터
            for(SalesDetail salesDetailDatas : salesDetailData) {
               row = sheet.createRow(rowCnt);
               // 가져온 값 셋팅
               row.createCell(0).setCellValue(salesDetailDatas.getCalMonth());
               row.createCell(1).setCellValue(salesDetailDatas.getCalDay());
               row.createCell(2).setCellValue(salesDetailDatas.getSalesType());
               row.createCell(3).setCellValue(salesDetailDatas.getAp());
               row.createCell(4).setCellValue(salesDetailDatas.getNoOrder());
               row.createCell(5).setCellValue(salesDetailDatas.getLnPartner());
               row.createCell(6).setCellValue(salesDetailDatas.getNmSpitem());
               row.createCell(7).setCellValue(salesDetailDatas.getCm());
               row.createCell(8).setCellValue(salesDetailDatas.getColor());
               row.createCell(9).setCellValue(salesDetailDatas.getQtSo());
               row.createCell(10).setCellValue(salesDetailDatas.getCa());
               row.createCell(11).setCellValue(salesDetailDatas.getVat());
               row.createCell(12).setCellValue(salesDetailDatas.getPrice());
               row.createCell(13).setCellValue(salesDetailDatas.getSum());
               row.createCell(14).setCellValue(salesDetailDatas.getEtc());
               row.createCell(15).setCellValue(salesDetailDatas.getNmCust());
               row.createCell(16).setCellValue(salesDetailDatas.getCalWh());

               rowCnt++;
            }

        response.setContentType("application/msexcel");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode("sales.xlsx","UTF-8")));
        response.setHeader("Content-Transfer-Encoding", "binary");
        OutputStream fileOut = response.getOutputStream();
        workbook.write(fileOut);
        workbook.close();
        fileOut.close();
    }

}
