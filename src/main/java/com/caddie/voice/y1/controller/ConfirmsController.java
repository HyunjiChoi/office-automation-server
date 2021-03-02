package com.caddie.voice.y1.controller;

import com.caddie.voice.y1.domain.ShippingsDetail;
import com.caddie.voice.y1.domain.ShippingsList;
import com.caddie.voice.y1.service.ConfirmsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/vc-settlement/v1/confirms")
public class ConfirmsController {

    private final ConfirmsService confirmsService;
    private final SalesController salesController;
    private final ShippingsController shippingsController;

    public ConfirmsController(ConfirmsService confirmsService, SalesController salesController, ShippingsController shippingsController) {
        this.confirmsService = confirmsService;
        this.salesController = salesController;
        this.shippingsController = shippingsController;
    }


    @ApiOperation("구매확정리스트 리스트 전체 전체조회")
    @GetMapping()
    public List<Map<String, Object>> getConfirms() throws SQLException {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object>data;

        List calDtList = confirmsService.ConfirmsCalDt();

        int cnt = confirmsService.ConfirmsCalDt().size();

        List confirmsLnPList = null;

        for(int i=0; i <cnt; i++){
            data = new HashMap<>();

            confirmsLnPList = confirmsService.ConfirmsLnPList(
                    calDtList.get(i).toString());
            String str = "";

            // List<String> -> String 으로
            for(Object confirmsLnPlists : confirmsLnPList){
                str += confirmsLnPlists +" ";
            }

            data.put("calDt", calDtList.get(i).toString());
            data.put("lnPartner", str);

            dataList.add(data);
        }

        return dataList;
    }


    @ApiOperation("구매확정리스트 리스트 삭제")
    @DeleteMapping()
    public void deregisterConfirms(
            @ApiParam(
                    name = "calDt",
                    type = "String",
                    value = "연월",
                    example = "2021-02-09")
            @RequestParam("calDt") String calDt
    ) {
        confirmsService.deregisterConfirms(calDt);

    }


    @ApiOperation("구매확정리스트 상세조회")
    @GetMapping("/detail")
    public List<ShippingsList> confirmsDtail(
            @RequestParam("calDt") String calDt,
            @RequestParam("lnPartner") String lnPartner) {
        return confirmsService.ConfirmsDetail(calDt, lnPartner);
    }


    @ApiOperation("통합주문 리스트, 구매확정 리스트 리스트 조회")
    @GetMapping("/adjustment")
    public List<Map<String, Object>> selectList() throws SQLException {
        List<Map<String, Object>> confirmList = new ArrayList<>();
        Map<String, Object> list = new HashMap<>();

        list.put("Sales", salesController.getSalesList());
        list.put("Shippings", shippingsController.getShippingsList());

        confirmList.add(list);

        return confirmList;
    }

    @ApiOperation("구매확정리스트 데이터 업데이트")
    @PostMapping("/adjustment")
    public List<ShippingsDetail> registerConfirms(
            @RequestParam("calDtSal") String calDtSal,
            @RequestParam("calDtSh") String calDtSh){
       // return confirmsService.registerConfirms(calDtSal, calDtSh);
        return null;
    }

}

