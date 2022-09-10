package com.longbig.multifunction.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author yuyunlong
 * @date 2022/9/10 12:02 下午
 * @description
 */
@Component
@Slf4j
public class ResourceUtils {

    @Autowired
    private ResourceLoader resourceLoader;

    public List<String> readFromClassPath(String filePath) {
        Resource resource = resourceLoader.getResource(filePath);
        List<String> data = Lists.newArrayList();
        try {
            InputStream inputStream = resource.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                if (StringUtils.isNotBlank(line)) {
                    data.add(line);
                }
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException e) {
            log.error("读取classpath下文件失败,filePath:{}", filePath, e);
        }
        return data;
    }
}
