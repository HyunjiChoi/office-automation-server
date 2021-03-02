package com.caddie.voice.y1.controller;

import com.caddie.voice.y1.domain.ShippingsList;
import com.caddie.voice.y1.service.ShippingsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/vc-settlement/v1/shippings")
public class ShippingsController {

    private final ShippingsService shippingsService;

    public ShippingsController(ShippingsService shippingsService) {
        this.shippingsService = shippingsService;
    }


    @ApiOperation("출하리스트 리스트 전체 조회")
    @GetMapping("")
    public List<Map<String, Object>> getShippingsList() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object>data;

        List calDtList = shippingsService.ShippingsCalDt();
        //System.out.println("calDtList : " + calDtList);
        List fileNmList = shippingsService.ShippingsFileNm();
        //System.out.println("filNmList : " + fileNmList);
        int cnt = shippingsService.ShippingsCalDt().size();

        List shippingsLnPList = null;

        for(int i=0; i <cnt; i++){
            data = new HashMap<>();

            shippingsLnPList = shippingsService.ShippingsLnPList(
                    calDtList.get(i).toString());
            //System.out.println("ShippingsList : " + shippingsLnPList);
            String str = "";
            // List<String> -> String 으로
            for(Object shippingsLnPlists : shippingsLnPList){
                str += shippingsLnPlists +" ";
                //System.out.println("str : " + str);
            }

            data.put("calDt", calDtList.get(i).toString());
            data.put("fileNm", fileNmList.get(i).toString());
            data.put("lnPartner", str);

            dataList.add(data);

            //System.out.println("SalesLnPList() : " +  salesService.SalesLnPList(calDtList.get(i).toString()));
        }

        return dataList;
    }

    @ApiOperation("출하리스트 파일 등록")
    @PostMapping("")
    public String registerShippings() {
        System.out.println("registerShippings 작동");
        return "registerShippings";
    }

    @ApiOperation("출하리스트 삭제")
    @DeleteMapping("")
    public void deregisterShippings(
            @RequestParam("calDt") String calDt,
            @RequestParam("fileNm") String fileNm,
            @RequestParam("lnPartner") String lnPartner) {
        shippingsService.deregisterShippings(calDt, fileNm, lnPartner);
        shippingsService.deregisterShippingsWorkSt(calDt, fileNm, lnPartner);
    }

    @ApiOperation("출하리스트 상세조회")
    @GetMapping("/detail")
    public List<ShippingsList> shppingsDetail(
        @RequestParam("calDt") String calDt,
        @RequestParam("lnPartner") String lnPartner){

        return shippingsService.shippingsDetail(calDt, lnPartner);
    }

}

