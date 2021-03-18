package com.caddie.voice.y1.dao;

import com.caddie.voice.y1.domain.ExcelData;
import com.caddie.voice.y1.domain.SalesDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SalesDao {

    List SalesCalDt();
    List<String[]> SalesLnPList(String calDt);

    void deregisterSales(String calDt);
    void deregisterSalesWorkSt(String calDt);

    List<ExcelData> getSalesByCalDtAndLnPartner(String calDt, String lnPartner);

    List<SalesDetail> salesDetail(String calDt);

    void InsertSalesExcelData(ExcelData data);
    void SalesWorkStData(String calDt, String marketCd, String lnPartner);
}
