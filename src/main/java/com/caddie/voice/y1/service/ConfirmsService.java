package com.caddie.voice.y1.service;

import com.caddie.voice.y1.dao.ConfirmsDao;
import com.caddie.voice.y1.domain.ShippingsDetail;
import com.caddie.voice.y1.domain.confirmsSelect;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
@AllArgsConstructor
@Service
public class ConfirmsService {
    ConfirmsDao confirmsDao;

    public List ConfirmsCalDt() { return confirmsDao.ConfirmsCalDt(); }

    public List ConfirmsNmShop(String calDt) { return confirmsDao.ConfirmsNmShop(calDt); }

    public List<confirmsSelect> getShippingsListSelect() { return confirmsDao.getShippingsListSelect(); }

    public void deregisterConfirms(String calDt, String nmShop) { confirmsDao.deregisterConfirms(calDt, nmShop); }

    public void deregisterConfirmsWorkSt(String calDt, String nmShop) { confirmsDao.deregisterConfirmsWorkSt(calDt, nmShop);}

    public void deregisterConfirmsAll(String calDt) { confirmsDao.deregisterConfirmsAll(calDt); }

    public void deregisterConfirmsWorkStAll(String calDt) { confirmsDao.deregisterConfirmsWorkStAll(calDt);}

    public List<ShippingsDetail> ConfirmsDetail(String calDt, String nmShop) { return confirmsDao.ConfirmsDetail(calDt, nmShop); }

    public void registerConfirms(String calDtSal, String calDtSh, String nmShop) {
        confirmsDao.registerConfirms(calDtSal, calDtSh, nmShop);
    }

    public void registerConfirmsWorkSt(String calDtSh, String nmShop) {
        confirmsDao.registerConfirmsWorkSt( calDtSh, nmShop);
    }

    public void downConfirm(String calDt, String nmShop, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<ShippingsDetail> confirmsDetailData = confirmsDao.ConfirmsDetail(calDt, nmShop);
        int rowCnt = 2;
        Row row;
        Row header1 = sheet.createRow(0);
        // 헤더
        header1.createCell(0).setCellValue("S");
        header1.createCell(1).setCellValue("NO_SO");
        header1.createCell(2).setCellValue("DT_SO");
        header1.createCell(3).setCellValue("NM_SHOP");
        header1.createCell(4).setCellValue("LN_PARTNER");
        header1.createCell(5).setCellValue("NO_ORDER");
        header1.createCell(6).setCellValue("CD_SPITEM");
        header1.createCell(7).setCellValue("NM_SPITEM");
        header1.createCell(8).setCellValue("CD_OPT");
        header1.createCell(9).setCellValue("NM_OPT");
        header1.createCell(10).setCellValue("NM_CUST");
        header1.createCell(11).setCellValue("QT_SO");
        header1.createCell(12).setCellValue("UM_VAT");
        header1.createCell(13).setCellValue("AM_VAT");
        header1.createCell(14).setCellValue("마감단가_VAT");
        header1.createCell(15).setCellValue("마감금액_VAT");
        header1.createCell(16).setCellValue("마감단가");
        header1.createCell(17).setCellValue("마감금액");
        header1.createCell(18).setCellValue("VAT");
        header1.createCell(19).setCellValue("AM_RATE_CHARGE");
        header1.createCell(20).setCellValue("NO_GIR");
        header1.createCell(21).setCellValue("PAY_TYPE");
        header1.createCell(22).setCellValue("NO_PO");
        header1.createCell(23).setCellValue("NO_GIR_MIN");
        Row header2 = sheet.createRow(1);
        // 헤더
        header2.createCell(0).setCellValue("선택");
        header2.createCell(1).setCellValue("접수번호");
        header2.createCell(2).setCellValue("접수일");
        header2.createCell(3).setCellValue("접수유형");
        header2.createCell(4).setCellValue("매출처");
        header2.createCell(5).setCellValue("No order");
        header2.createCell(6).setCellValue("상품코드");
        header2.createCell(7).setCellValue("상품명");
        header2.createCell(8).setCellValue("옵션코드");
        header2.createCell(9).setCellValue("옵션명");
        header2.createCell(10).setCellValue("주문자");
        header2.createCell(11).setCellValue("수량");
        header2.createCell(12).setCellValue("판매단가(VAT)");
        header2.createCell(13).setCellValue("판매금액(VAT)");
        header2.createCell(14).setCellValue("마감단가(VAT)");
        header2.createCell(15).setCellValue("마감금액(VAT)");
        header2.createCell(16).setCellValue("마감단가");
        header2.createCell(17).setCellValue("마감금액");
        header2.createCell(18).setCellValue("VAT");
        header2.createCell(19).setCellValue("정율수수료");
        header2.createCell(20).setCellValue("의뢰번호");
        header2.createCell(21).setCellValue("결제형태");
        header2.createCell(22).setCellValue("발주번호");
        header2.createCell(23).setCellValue("최초의뢰번호");

        // 데이터
        for(ShippingsDetail confirmsDetailDatas : confirmsDetailData) {
            row = sheet.createRow(rowCnt);
            // 가져온 값 셋팅
            row.createCell(0).setCellValue(confirmsDetailDatas.getS());
            row.createCell(1).setCellValue(confirmsDetailDatas.getNoSo());
            row.createCell(2).setCellValue(confirmsDetailDatas.getDtSo());
            row.createCell(3).setCellValue(confirmsDetailDatas.getNmShop());
            row.createCell(4).setCellValue(confirmsDetailDatas.getLnPartner());
            row.createCell(5).setCellValue(confirmsDetailDatas.getNoOrder());
            row.createCell(6).setCellValue(confirmsDetailDatas.getCdSpitem());
            row.createCell(7).setCellValue(confirmsDetailDatas.getNmSpitem());
            row.createCell(8).setCellValue(confirmsDetailDatas.getCdOpt());
            row.createCell(9).setCellValue(confirmsDetailDatas.getNmOpt());
            row.createCell(10).setCellValue(confirmsDetailDatas.getNmCust());
            row.createCell(11).setCellValue(confirmsDetailDatas.getQtSo());
            row.createCell(12).setCellValue(confirmsDetailDatas.getUmVat());
            row.createCell(13).setCellValue(confirmsDetailDatas.getAmVat());
            row.createCell(14).setCellValue(confirmsDetailDatas.getCmVat());
            row.createCell(15).setCellValue(confirmsDetailDatas.getCaVat());
            row.createCell(16).setCellValue(confirmsDetailDatas.getCm());
            row.createCell(17).setCellValue(confirmsDetailDatas.getCa());
            row.createCell(18).setCellValue(confirmsDetailDatas.getVat());
            row.createCell(19).setCellValue(confirmsDetailDatas.getAmRateCharge());
            row.createCell(20).setCellValue(confirmsDetailDatas.getNoGir());
            row.createCell(21).setCellValue(confirmsDetailDatas.getPayType());
            row.createCell(22).setCellValue(confirmsDetailDatas.getNoPo());
            row.createCell(23).setCellValue(confirmsDetailDatas.getNoGirMin());
            rowCnt++;
        }
        response.setContentType("application/msexcel");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode("confirms.xlsx","UTF-8")));
        response.setHeader("Content-Transfer-Encoding", "binary");
        OutputStream fileOut = response.getOutputStream();
        workbook.write(fileOut);
        workbook.close();
        fileOut.close();
    }
}