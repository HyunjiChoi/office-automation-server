package com.caddie.voice.y1.controller;

import com.caddie.voice.y1.domain.Sales;
import com.caddie.voice.y1.domain.Test;
import com.caddie.voice.y1.dto.ResponseSalesDto;
import com.caddie.voice.y1.service.SalesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/vc-settlement")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }


    @GetMapping("/sales/v1")
    public List<Test> getSales() {
//        Sales sales = salesService.getSales();
//        ResponseSalesDto marketList;
        System.out.println(salesService.getSales());

        return salesService.getSales();
    }

/*    @PostMapping("/orders/v1")
    public registerSales(){


        return null;
    }*/

/*    @DeleteMapping(path="/")
    public void deleteSales(@RequestParam(value="CAL_DT"){
        salesService.deregister(Date CAL_DT);

    }*/

    @GetMapping("/orders/detail/v1")
    public List<Object> orderListDetail(){
        return null;
    }


}

