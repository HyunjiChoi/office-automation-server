package com.caddie.voice.y1.service;

import com.caddie.voice.y1.dao.SalesDao;

import com.caddie.voice.y1.domain.Sales;
import com.caddie.voice.y1.domain.Test;
import com.caddie.voice.y1.dto.ResponseSalesDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class SalesService {

    SalesDao salesDao;

    public List<Test> getSales() {

        return salesDao.getSales();
    }


   /* public void deregister(Date CAL_DT) {
        return salesDao.deregister(Date CAL_DT);
    }*/
}
