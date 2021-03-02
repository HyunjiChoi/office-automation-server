package com.caddie.voice.y1.dao;

import com.caddie.voice.y1.domain.ExcelData;
import com.caddie.voice.y1.domain.SalesList;
import com.caddie.voice.y1.domain.SalesReg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesDao {

    List SalesCalDt();
    List<String[]> SalesLnPList(String calDt);

    void deregisterSales(String calDt,  String lnPartner);
    void deregisterSalesWorkSt(String calDt, String lnPartner);

    List<SalesList> salesDetail(String calDt);

    void InsertSalesExelData_11(ExcelData data);
    void InsertSalesExelData_Gal(ExcelData data);
    void InsertSalesExelData_N(ExcelData data);
    void InsertSalesExelData_Au(ExcelData data);
    void InsertSalesExelData_In(ExcelData data);
    void InsertSalesExelData_Ha(ExcelData data);
}
