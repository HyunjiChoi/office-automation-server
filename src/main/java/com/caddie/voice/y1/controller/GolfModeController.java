package com.caddie.voice.y1.controller;

import com.caddie.voice.y1.handler.ApiResponse;
import com.caddie.voice.y1.service.GolfModeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "y1 golf mode api service", description = "y1 golf mode api service")
@RequestMapping("/y1/golfMode")
public class GolfModeController {
    private final GolfModeService golfModeService;

    public GolfModeController(GolfModeService golfModeService) {
        this.golfModeService = golfModeService;
    }

    @ApiOperation(value="golf mode home")
    @GetMapping("/home")
    public ApiResponse getGolfModeHome() {
        return golfModeService.getGolfModeHome();
    }

}
