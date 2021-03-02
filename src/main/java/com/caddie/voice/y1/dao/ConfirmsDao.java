package com.caddie.voice.y1.dao;

import com.caddie.voice.y1.domain.SalesList;
import com.caddie.voice.y1.domain.SalesReg;
import com.caddie.voice.y1.domain.ShippingsDetail;
import com.caddie.voice.y1.domain.ShippingsList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ConfirmsDao {

    List ConfirmsCalDt();
    List ConfirmsLnPList(String calDt);

    void deregisterConfirms(String calDt);
    List<ShippingsList> ConfirmsDetail(String calDt, String lnPartner);
    List<ShippingsDetail> registerConfirms(String calDtSal, String calDtSh);



}
