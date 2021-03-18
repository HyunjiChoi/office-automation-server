package com.caddie.voice.y1.controller;

import com.caddie.voice.y1.domain.ShippingsList;
import com.caddie.voice.y1.exception.common.ExceptionType;
import com.caddie.voice.y1.exception.exceptions.ExistShippingsException;
import com.caddie.voice.y1.service.ShippingsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        Map<String, Object> data;

        List calDtList = shippingsService.ShippingsCalDt();
        List fileNmList = shippingsService.ShippingsFileNm();

        int cnt = shippingsService.ShippingsCalDt().size();

        List shippingsNmShop = null;

        for(int i=0; i <cnt; i++){
            data = new HashMap<>();

            shippingsNmShop = shippingsService.ShippingsNmShop(
                    calDtList.get(i).toString());

            String str = "";
            // List<String> -> String 으로
            for(Object shippingsNmShops : shippingsNmShop){
                str += shippingsNmShops +" ";
            }

            data.put("calDt", calDtList.get(i).toString());
            data.put("fileNm", fileNmList.get(i).toString());
            data.put("nmShop", str);

            dataList.add(data);

        }

        return dataList;
    }

    @ApiOperation("출하리스트 파일 등록")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity registerShippings(
            @RequestParam("calDt") String calDt,
            @RequestParam("frontVideoFile") MultipartFile frontVideoFile) throws IOException {

        int count = shippingsService.InsertShippingsExcelData(calDt, frontVideoFile);
        if(count == 0)
            throw new ExistShippingsException(ExceptionType.ExistShippingsException);
        return new ResponseEntity(this.getShippingsList(), HttpStatus.OK);
    }


    @ApiOperation("출하리스트 삭제")
    @DeleteMapping("/")
    public void deregisterShippingsAll(
            @RequestParam("calDt") String calDt) {
        shippingsService.deregisterShippingsAll(calDt);
        shippingsService.deregisterShippingsWorkStAll(calDt);
    }


    @ApiOperation("출하리스트 삭제")
    @DeleteMapping("/detail")
    public void deregisterShippings(
            @RequestParam("calDt") String calDt,
            @RequestParam("nmShop") String nmShop) {
        shippingsService.deregisterShippings(calDt, nmShop);
        shippingsService.deregisterShippingsWorkSt(calDt, nmShop);
    }

    @ApiOperation("출하리스트 상세조회")
    @GetMapping("/detail")
    public List<ShippingsList> shppingsDetail(
        @RequestParam("calDt") String calDt,
        @RequestParam("nmShop") String nmShop){

        return shippingsService.shippingsDetail(calDt, nmShop);
    }

}

