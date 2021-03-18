package com.caddie.voice.y1.dao;

import com.caddie.voice.y1.domain.ShippingsDetail;
import com.caddie.voice.y1.domain.ShippingsList;
import com.caddie.voice.y1.domain.confirmsSelect;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ConfirmsDao {

    List ConfirmsCalDt();
    List ConfirmsNmShop(String calDt);

    List<confirmsSelect> getShippingsListSelect();

    void deregisterConfirms(String calDt, String nmShop);
    void deregisterConfirmsWorkSt(String calDt, String nmShop);

    void deregisterConfirmsAll(String calDt);
    void deregisterConfirmsWorkStAll(String calDt);

    List<ShippingsDetail> ConfirmsDetail(String calDt, String nmShop);

    ShippingsDetail registerConfirms(String calDtSal, String calDtSh, String nmShop);
    ShippingsDetail registerConfirmsWorkSt(String calDtSh, String nmShop);



}
