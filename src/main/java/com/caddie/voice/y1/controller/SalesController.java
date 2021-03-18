package com.caddie.voice.y1.controller;

import com.caddie.voice.y1.domain.SalesDetail;
import com.caddie.voice.y1.domain.SalesList;
import com.caddie.voice.y1.handler.ApiResponse;
import com.caddie.voice.y1.service.SalesService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import io.swagger.models.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Array;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(value="/vc-settlement/v1/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @ApiOperation("통합주문 리스트 전체 조회")
    @GetMapping
    public List<Map<String, Object>> getSalesList() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object>data;

        List calDtList = salesService.SalesCalDt();

        int cnt = salesService.SalesCalDt().size();

        List salesLnPList = null;

        for(int i=0; i <cnt; i++){
            data = new HashMap<>();
            salesLnPList = salesService.SalesLnPList(calDtList.get(i).toString());
            String str = "";
            for(Object salesLnPlists : salesLnPList){
                str += salesLnPlists +" ";
            }
            data.put("calDt", calDtList.get(i).toString());
            data.put("lnPartner", str);
            dataList.add(data);
        }
        return dataList;
    }


    @ApiOperation("통합주문 파일 등록")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity registerSales(
            @RequestParam("calDt") String calDt,
            @RequestParam("frontVideoFile") MultipartFile frontVideoFile) throws IOException{

            salesService.InsertSalesExcelData(calDt, frontVideoFile);
            return new ResponseEntity(this.getSalesList(), HttpStatus.OK);
    }

    @ApiOperation("판매단가 파일 등록")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            value = "/unit")
    public ResponseEntity registerUnit(
            @RequestParam("calDt") String calDt,
            @RequestParam("frontVideoFile") MultipartFile frontVideoFile) throws IOException{

        salesService. UnitPrice(calDt, frontVideoFile);
        return new ResponseEntity(HttpStatus.OK);
    }


    @ApiOperation("통합주문 리스트 삭제")
    @DeleteMapping("/detail")
    public void deregisterSales(
            @RequestParam("calDt") String calDt) {

        salesService.deregisterSales(calDt);
        salesService.deregisterSalesWorkSt(calDt);
    }

    @ApiOperation("통합주문 상세조회")
    @GetMapping("/detail")
    public List<SalesDetail> salesDetail(
            @RequestParam("calDt") String calDt) {

        return salesService.salesDetail(calDt);
    }

    @ApiOperation("통합주문 엑셀 다운로드")
    @GetMapping("/detail/xlsx")
    public void downloadSales(@RequestParam("calDt") String calDt , HttpServletResponse response) throws IOException {
        salesService.downSales(calDt, response);
    }
}


