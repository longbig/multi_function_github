package com.longbig.multifunction.api;

import com.google.common.collect.Maps;
import com.longbig.multifunction.utils.OkHttpUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author yuyunlong
 * @date 2022/9/18 11:00 上午
 * @description
 */
@RestController
public class YangService {

    @GetMapping("/yang/finish_game")
    public String getYang(String cookie) {
        //完成羊群接口
        String finish_sheep_api = "http://cat-match.easygame2021.com/sheep/v1/game/game_over?rank_score=1&rank_state=1&rank_time=60&rank_role=1&skin=1";

        Map<String, String> header = Maps.newHashMap();
        try {
            String response = OkHttpUtils.get(finish_sheep_api, cookie, header);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "执行完成";
    }
}
