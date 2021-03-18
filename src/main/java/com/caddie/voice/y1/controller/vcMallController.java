package com.caddie.voice.y1.controller;

import com.caddie.voice.y1.service.VcMallService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Slf4j
@RestController
@RequestMapping(value="/vc-settlement/v1/vcMall")
public class vcMallController {

    private final VcMallService vcMallService;

    public vcMallController(VcMallService vcMallService) {
        this.vcMallService = vcMallService;
    }


    @ApiOperation("자사몰, erp 주문내역 등록")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity registerErp(
            @RequestParam("frontVideoFile") MultipartFile frontVideoFile,
            HttpServletResponse response) throws IOException{

        vcMallService.insertExcelData(frontVideoFile);
        vcMallService.downExcelData(response);

        return new ResponseEntity(HttpStatus.OK);
    }


}
