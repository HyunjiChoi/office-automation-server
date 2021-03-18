package com.caddie.voice.y1.service;

import com.caddie.voice.y1.dao.ShippingsDao;
import com.caddie.voice.y1.domain.ExcelDataSh;
import com.caddie.voice.y1.domain.ShippingsList;
import com.caddie.voice.y1.exception.common.ExceptionType;
import com.caddie.voice.y1.exception.exceptions.ExistSalesException;
import com.caddie.voice.y1.exception.exceptions.ExistShippingsException;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ShippingsService {

    ShippingsDao shippingsDao;

    public List ShippingsCalDt() { return shippingsDao.ShippingsCalDt(); }
    public List ShippingsFileNm() { return shippingsDao.ShippingsFileNm(); }
    public List<String[]> ShippingsNmShop(String calDt) { return shippingsDao.ShippingsNmShop(calDt); }

    public void deregisterShippings(String calDt, String nmShop) { shippingsDao.deregisterShippings(calDt, nmShop); }
    public void deregisterShippingsWorkSt(String calDt, String nmShop) { shippingsDao.deregisterShippingsWorkSt(calDt, nmShop); }

    public void deregisterShippingsAll(String calDt) {  shippingsDao.deregisterShippingsAll(calDt); }
    public void deregisterShippingsWorkStAll(String calDt) { shippingsDao.deregisterShippingsWorkStAll(calDt); }

    public List<ShippingsList> shippingsDetail(String calDt, String nmShop) { return shippingsDao.shippingsDetail(calDt, nmShop); }

    public int InsertShippingsExcelData(String calDt, MultipartFile frontVideoFile) throws IOException {
        List<ExcelDataSh> dataList = new ArrayList<>();

        //파일 이름
        String fileNm = frontVideoFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(frontVideoFile.getOriginalFilename());
        int count = 0;
        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀 파일만 업로드 해주세요.");
        }


        Workbook workbook = null;

        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(frontVideoFile.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(frontVideoFile.getInputStream());
        }

        Sheet worksheet = workbook.getSheetAt(0);

        if(fileNm.contains(".xlsx")) {
            try {

                for (int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {

                    Row row = worksheet.getRow(i);

                    ExcelDataSh data = new ExcelDataSh();

                    data.setCalDt(calDt);
                    data.setFileNm(fileNm);
                    data.setNmShop(row.getCell(3).getStringCellValue()); // 접수유형 = lnParter "옥션", "스토어팜", ...
                    data.setNoSo(row.getCell(1).getStringCellValue()); // 접수번호
                    data.setDtSo(row.getCell(2).getStringCellValue()); // 접수일
                    if(data.getNmShop().equals("11번가")){
                        if(shippingsDao.shippingsDetail(calDt,"11번가").size() != 0 && count == 0) {
                            throw new ExistShippingsException(ExceptionType.ExistShippingsException);
                        }
                        data.setMarketCd("2001");
                    }else if(data.getNmShop().equals("지마켓")
                            || data.getNmShop().equals("G마켓")
                            || data.getNmShop().equals("g마켓")){
                        if(
                                (shippingsDao.shippingsDetail(calDt,"지마켓").size() != 0
                                        || shippingsDao.shippingsDetail(calDt,"G마켓").size() != 0
                                        || shippingsDao.shippingsDetail(calDt,"g마켓").size() != 0) && count == 0 ) {
                            throw new ExistShippingsException(ExceptionType.ExistShippingsException);
                        }
                        data.setMarketCd("2002");
                    }else if(data.getNmShop().equals("갤러리아몰")){
                        if(shippingsDao.shippingsDetail(calDt,"갤러리아몰").size() != 0 && count == 0) {
                            throw new ExistShippingsException(ExceptionType.ExistShippingsException);
                        }
                        data.setMarketCd("2008");
                    }else if(data.getNmShop().equals("스마트스토어")){
                        if(shippingsDao.shippingsDetail(calDt,"스마트스토어").size() != 0 && count == 0) {
                            throw new ExistShippingsException(ExceptionType.ExistShippingsException);
                        }
                        data.setMarketCd("2007");
                    }else if(data.getNmShop().equals("옥션")){
                        if(shippingsDao.shippingsDetail(calDt,"옥션").size() != 0 && count == 0) {
                            throw new ExistShippingsException(ExceptionType.ExistShippingsException);
                        }
                        data.setMarketCd("2003");
                    }else if(data.getNmShop().equals("인터파크")){
                        if(shippingsDao.shippingsDetail(calDt,"인터파크").size() != 0 && count == 0) {
                            throw new ExistShippingsException(ExceptionType.ExistShippingsException);
                        }
                        data.setMarketCd("2006");
                    }else if(data.getNmShop().equals("하프클럽")){
                        if(shippingsDao.shippingsDetail(calDt,"하프클럽").size() != 0 && count == 0) {
                            throw new ExistShippingsException(ExceptionType.ExistShippingsException);
                        }
                        data.setMarketCd("2009");
                    }

                    data.setLnPartner(row.getCell(4).getStringCellValue()); // 매출처
                    data.setNoOrder(row.getCell(5).getStringCellValue()); // NO_ORDER
                    data.setCdSpitem(row.getCell(6).getStringCellValue());  // 상품코드
                    data.setNmSpitem(row.getCell(7).getStringCellValue()); // 사움명
                    data.setCdOpt(row.getCell(8).getStringCellValue()); // 옵션코드
                    data.setNmOpt(row.getCell(9).getStringCellValue()); // 옵션명
                    data.setNmCust(row.getCell(10).getStringCellValue()); // 주문자
                    data.setQtSo(row.getCell(11).getNumericCellValue()); // 수량
                    data.setUmVat(row.getCell(12).getNumericCellValue()); // 판매단가(VAT)
                    data.setAmVat((row.getCell(13).getNumericCellValue())); // 판매금액(VAT)
                    data.setCmVat(row.getCell(14).getNumericCellValue()); // 마감단가(VAT)
                    data.setCaVat(row.getCell(15).getNumericCellValue()); // 판마감단가(VAT)
                    data.setCm(row.getCell(16).getNumericCellValue()); // 마감단가
                    data.setCa(row.getCell(17).getNumericCellValue()); // 마감금액
                    data.setVat(row.getCell(18).getNumericCellValue()); // VAT
                    // 정률수수료 는 default
                    data.setNoGir(row.getCell(20).getStringCellValue()); // 의뢰번호
                    // 결제형태 default
                    // 발주 번호 default
                    data.setNoGirMin(row.getCell(23).getStringCellValue()); //최초 의뢰번호
                    // confirmSt 는 default
                    // delFlag 는 default
                    dataList.add(data);


                    String nmShop = data.getNmShop();
                    String marketCd = data.getMarketCd();

                    shippingsDao.InsertShippingsExcelData(data);
                    shippingsDao.ShippingsWorkStData(calDt, fileNm, marketCd, nmShop);
                    count++;
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return count;
    }


}
