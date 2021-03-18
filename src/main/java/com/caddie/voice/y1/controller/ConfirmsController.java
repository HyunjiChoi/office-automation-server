package com.caddie.voice.y1.controller;

import com.caddie.voice.y1.domain.ShippingsDetail;
import com.caddie.voice.y1.domain.ShippingsList;
import com.caddie.voice.y1.domain.confirmsSelect;
import com.caddie.voice.y1.dto.AdjustmentRequest;
import com.caddie.voice.y1.service.ConfirmsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/vc-settlement/v1/confirms")
@Slf4j
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
        Map<String, Object>data = new HashMap<>();

        List calDtList = confirmsService.ConfirmsCalDt();

        int cnt = confirmsService.ConfirmsCalDt().size();

        List confirmsNmShop = null;

        for(int i=0; i <cnt; i++) {
            data = new HashMap<>();

            confirmsNmShop = confirmsService.ConfirmsNmShop(
                    calDtList.get(i).toString());
            String str = "";

            // List<String> -> String 으로
            for (Object confirmsNmShops : confirmsNmShop) {
                str += confirmsNmShops + " ";
            }

            data.put("calDt", calDtList.get(i).toString());
            data.put("nmShop", str);

            dataList.add(data);
        }
            return  dataList;
    }


    @ApiOperation("구매확정리스트 리스트 날짜별 전체 삭제")
    @DeleteMapping("/")
    public void deregisterConfirmsAll(
            @RequestParam("calDt") String calDt){

        confirmsService.deregisterConfirmsAll(calDt);
        confirmsService.deregisterConfirmsWorkStAll(calDt);

    }


    @ApiOperation("구매확정리스트 리스트 삭제")
    @DeleteMapping("/detail")
    public void deregisterConfirms(
            @RequestParam("calDt") String calDt,
            @RequestParam("nmShop") String nmShop){

        confirmsService.deregisterConfirms(calDt, nmShop);
        confirmsService.deregisterConfirmsWorkSt(calDt, nmShop);

    }



    @ApiOperation("구매확정리스트 상세조회")
    @GetMapping("/detail")
    public List<ShippingsDetail> confirmsDtail(
            @RequestParam("calDt") String calDt,
            @RequestParam("nmShop") String nmShop) {
        return confirmsService.ConfirmsDetail(calDt, nmShop);
    }

    @ApiOperation("통합주문 리스트, 구매확정 리스트 리스트 조회")
    @GetMapping("/adjustment")
    public Map<String, Object> selectList() throws SQLException {
        Map<String, Object> list = new HashMap<>();

        List<confirmsSelect> selectList = confirmsService.getShippingsListSelect();

        list.put("sales", salesController.getSalesList());
        list.put("shippings", selectList);

        return list;
    }

    @ApiOperation("구매확정리스트 데이터 업데이트")
    @PostMapping("/adjustment")
    public void registerConfirms(
            @RequestBody AdjustmentRequest adjustmentRequest){

        confirmsService.registerConfirms(adjustmentRequest.getCalDtSal(), adjustmentRequest.getCalDtSh(), adjustmentRequest.getNmShop());
        confirmsService.registerConfirmsWorkSt(adjustmentRequest.getCalDtSh(), adjustmentRequest.getNmShop());
    }

    @ApiOperation("구매확정 엑셀 다운로드")
    @GetMapping("/detail/xlsx")
    public void downloadSales(
            @RequestParam("calDt") String calDt,
            @RequestParam("nmShop") String nmShop, HttpServletResponse response) throws IOException {

        confirmsService.downConfirm(calDt, nmShop, response);
    }
}

