package com.caddie.voice.y1.dao;

import com.caddie.voice.y1.domain.ShippingsList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShippingsDao {

    List ShippingsCalDt();
    List ShippingsFileNm();
    List<String[]> ShippingsLnPList(String calDt);

    void deregisterShippings(String calDt, String fileNm, String lnPartner);
    void deregisterShippingsWorkSt(String calDt, String fileNm, String lnPartner);

    List<ShippingsList> shippingsDetail(String calDt, String lnPartner);



}
