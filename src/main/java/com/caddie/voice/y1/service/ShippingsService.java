package com.caddie.voice.y1.service;

import com.caddie.voice.y1.dao.ShippingsDao;
import com.caddie.voice.y1.domain.ShippingsList;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ShippingsService {

    ShippingsDao shippingsDao;

    public List ShippingsCalDt() { return shippingsDao.ShippingsCalDt(); }
    public List ShippingsFileNm() { return shippingsDao.ShippingsFileNm(); }
    public List<String[]> ShippingsLnPList(String calDt) { return shippingsDao.ShippingsLnPList(calDt); }

    public void deregisterShippings(String calDt, String fileNm, String lnPartner) { shippingsDao.deregisterShippings(calDt, fileNm, lnPartner); }
    public void deregisterShippingsWorkSt(String calDt, String fileNm, String lnPartner) { shippingsDao.deregisterShippingsWorkSt(calDt, fileNm, lnPartner); }

    public List<ShippingsList> shippingsDetail(String calDt, String lnPartner) { return shippingsDao.shippingsDetail(calDt, lnPartner); }


}
