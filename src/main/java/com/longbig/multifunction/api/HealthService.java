package com.longbig.multifunction.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthService {

    /**
     * 服务探活接口
     * @return
     */
    @GetMapping("/checkHealth")
    public String checkHealth() {
        return "success";
    }
}
