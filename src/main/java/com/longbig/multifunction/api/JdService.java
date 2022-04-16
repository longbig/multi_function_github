package com.longbig.multifunction.api;

import com.longbig.multifunction.job.JDBeanJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JdService {

    @Autowired
    private JDBeanJob jdBeanJob;

    @GetMapping("/getJD")
    public String getJD() throws Exception {
        return jdBeanJob.getJd();
    }
}
