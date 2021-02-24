package com.caddie.voice.y1.dao;

import com.caddie.voice.y1.domain.Sales;
import com.caddie.voice.y1.domain.Test;
import com.caddie.voice.y1.dto.ResponseSalesDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SalesDao {

    List<Test> getSales();
    /*void deregister(Date CAL_DT);*/
}
