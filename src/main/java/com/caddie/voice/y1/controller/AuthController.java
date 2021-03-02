package com.caddie.voice.y1.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/vc-settlement/v1/login")
public class AuthController {

    @ApiOperation("로그인")
    @PostMapping()
    public int Login(@RequestParam("id") String id,
                     @RequestParam("pwd") String pwd){

            if (id.equals("admin") && pwd.equals("1234")) {
                  return HttpServletResponse.SC_NO_CONTENT;
            }else{
                return HttpServletResponse.SC_UNAUTHORIZED;
            }
    }
}
