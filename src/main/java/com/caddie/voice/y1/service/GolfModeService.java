package com.caddie.voice.y1.service;

import com.caddie.voice.y1.handler.ApiResponse;
import com.caddie.voice.y1.handler.ApiResponseMap;
import org.springframework.stereotype.Service;

@Service
public class GolfModeService {

    public ApiResponse getGolfModeHome() {
        ApiResponseMap result = new ApiResponseMap();

        result.setResponseData("userName","Tiger Woods");
        result.setResponseData("bestScore","79");
        result.setResponseData("bestScoreDate","1612781089");
        result.setResponseData("bestScoreClubName","VC CC");

        return result;
    }

}
