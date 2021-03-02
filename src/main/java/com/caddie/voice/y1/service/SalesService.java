package com.caddie.voice.y1.service;

import com.caddie.voice.y1.dao.SalesDao;

import com.caddie.voice.y1.domain.*;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class SalesService {

    SalesDao salesDao;

    public List SalesCalDt() { return salesDao.SalesCalDt(); }
    public List<String[]> SalesLnPList(String calDt) { return salesDao.SalesLnPList(calDt); }

    public void deregisterSales(String calDt, String lnPartner) { salesDao.deregisterSales(calDt, lnPartner); }
    public void deregisterSalesWorkSt(String calDt, String lnPartner) { salesDao.deregisterSalesWorkSt(calDt, lnPartner);}

    public List<SalesList> salesDetail(String calDt) { return salesDao.salesDetail(calDt); }

    public void InsertSalesExcelData_11(ExcelData data) { salesDao.InsertSalesExelData_11(data); }
    public void InsertSalesExcelData_Gal(ExcelData data) { salesDao.InsertSalesExelData_Gal(data); }
    public void InsertSalesExcelData_N(ExcelData data) { salesDao.InsertSalesExelData_N(data); }
    public void InsertSalesExcelData_Au(ExcelData data) { salesDao.InsertSalesExelData_Au(data); }
    public void InsertSalesExcelData_In(ExcelData data) { salesDao.InsertSalesExelData_In(data); }
    public void InsertSalesExcelData_Ha(ExcelData data) { salesDao.InsertSalesExelData_Ha(data); }
}
