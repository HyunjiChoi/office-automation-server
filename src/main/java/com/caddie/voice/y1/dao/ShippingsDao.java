package com.caddie.voice.y1.dao;

import com.caddie.voice.y1.domain.ExcelData;
import com.caddie.voice.y1.domain.ExcelDataSh;
import com.caddie.voice.y1.domain.ShippingsDetail;
import com.caddie.voice.y1.domain.ShippingsList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShippingsDao {

    List ShippingsCalDt();
    List ShippingsFileNm();
    List<String[]> ShippingsNmShop(String calDt);

    void deregisterShippings(String calDt, String nmShop);
    void deregisterShippingsWorkSt(String calDt, String nmShop);

    void deregisterShippingsAll(String calDt);
    void deregisterShippingsWorkStAll(String calDt);

    List<ShippingsList> shippingsDetail(String calDt, String nmShop);

    void InsertShippingsExcelData(ExcelDataSh data);
    void ShippingsWorkStData(String calDt, String fileNm, String marketCd, String nmShop);


}
