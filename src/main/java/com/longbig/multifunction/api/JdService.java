package com.longbig.multifunction.api;

import com.longbig.multifunction.job.JDBeanJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JdService {

    @Autowired
    private JDBeanJob jdBeanJob;

    @GetMapping("/getJD")
    public String getJD(@RequestParam("type") Integer type) throws Exception {
        if (type == 1) {
            return jdBeanJob.getJdSign();
        } else if (type == 2) {
            return jdBeanJob.getSharkBean();
        } else if (type == 3) {
            return jdBeanJob.getLottery();
        } else if (type == 4) {
            return jdBeanJob.plusSign();
        }
        return "fail";
    }
}
