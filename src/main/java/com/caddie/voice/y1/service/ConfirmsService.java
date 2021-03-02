package com.caddie.voice.y1.service;

import com.caddie.voice.y1.dao.ConfirmsDao;
import com.caddie.voice.y1.dao.SalesDao;
import com.caddie.voice.y1.domain.SalesList;
import com.caddie.voice.y1.domain.SalesReg;
import com.caddie.voice.y1.domain.ShippingsDetail;
import com.caddie.voice.y1.domain.ShippingsList;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class ConfirmsService {

    ConfirmsDao confirmsDao;

    public List ConfirmsCalDt() { return confirmsDao.ConfirmsCalDt(); }
    public List ConfirmsLnPList(String calDt) { return confirmsDao.ConfirmsLnPList(calDt); }

    public void deregisterConfirms(String calDt) { confirmsDao.deregisterConfirms(calDt); }

    public List<ShippingsList> ConfirmsDetail(String calDt, String lnPartner) { return confirmsDao.ConfirmsDetail(calDt, lnPartner); }

    public List<ShippingsDetail> registerConfirms(String calDtSal, String calDtSh) {
        return confirmsDao.registerConfirms(calDtSal, calDtSh);
    }



}
