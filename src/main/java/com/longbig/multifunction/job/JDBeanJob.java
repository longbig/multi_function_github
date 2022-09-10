package com.longbig.multifunction.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longbig.multifunction.utils.FileUtils;
import com.longbig.multifunction.utils.OkHttpUtils;
import com.longbig.multifunction.utils.ResourceUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuyunlong
 * @date 2022/2/27 12:54 下午
 * @description
 */
@Component
@Slf4j
public class JDBeanJob {

//    @Value("${jd.pt_key}")
//    private String pt_key;
//
//    @Value("${jd.pt_pin}")
//    private String pt_pin;
    @Autowired
    private ResourceUtils resourceUtils;
    @Value("${jd.filePath}")
    private String filePath;

    @Value("${start.docker}")
    private Boolean fromDocker;

    private List<String> cookies;

    @PostConstruct
    public void init() {
        log.info("加载cookie到全局变量");
        cookies = Lists.newArrayList();
        List<String> ptAlls = Lists.newArrayList();
        if (fromDocker) {
            ptAlls = FileUtils.readFileToStringList(filePath);
        } else {
            ptAlls = resourceUtils.readFromClassPath(filePath);
        }
        for (String ptAll : ptAlls) {
            String[] pts = ptAll.split(",");
            String pt_key1 = pts[0];
            String pt_pin1 = pts[1];
            String cookie = "__jd_ref_cls=JingDou_SceneHome_NewGuidExpo; mba_muid=1645885780097318205272.81.1645885790055; mba_sid=81.5; __jda=122270672.1645885780097318205272.1645885780.1645885780.1645885780.1; __jdb=122270672.1.1645885780097318205272|1.1645885780; __jdc=122270672; __jdv=122270672%7Ckong%7Ct_1000170135%7Ctuiguang%7Cnotset%7C1644027879157; pre_seq=0; pre_session=3acd1f6361f86fc0a1bc23971b2e7bbe6197afb6|143; unpl=JF8EAKZnNSttWRkDURtVThUWHAgEWw1dH0dXOjMMAFVcTQQAEwZORxR7XlVdXhRKFx9sZhRUX1NIVw4YBCsiEEpcV1ZVC0kVAV9XNVddaEpkBRwAExEZQ1lWW1kMTBcEaWcAUVpeS1c1KwUbGyB7bVFeXAlOFQJobwxkXGhJVQQZBR0UFU1bZBUzCQYXBG1vBl1VXElRAR8FGxUWS1hRWVsISCcBb2cHUm1b%7CV2_ZzNtbRYAFxd9DUNcKRxYB2ILGloRUUYcIVpAAHsbWQZjVBEJclRCFnUUR11nGlgUZgIZXkFcQRRFCEJkexhdB24LFFtEUHMQfQ5GXH0pXAQJbRZeLAcCVEULRmR6KV5VNVYSCkVVRBUiAUEDKRgMBTRREV9KUUNGdlxAByhNWwVvBUIKEVBzJXwJdlR6GF0GZAoUWUdRQCUpUBkCJE0ZWTVcIlxyVnMURUooDytAGlU1Vl9fEgUWFSIPRFN7TlUCMFETDUIEERZ3AEBUKBoIAzRQRlpCX0VFIltBZHopXA%253d%253d; " +
                    "pt_key=" + pt_key1 +
                    "; pt_pin=" + pt_pin1 +
                    "; pwdt_id=jd_505bacd333f6b; sid=1b2c8b7ce820c4188f048e689bf58c8w; visitkey=36446698972455355";
            cookies.add(cookie);
        }
        log.info("cookies size：{}", cookies.size());
    }

    /**
     * 京东每日签到
     *
     * @return
     */
    @Scheduled(cron = "0 0 6 1/1 * ?")
    public String getJdSign() throws Exception {
        String url = "https://api.m.jd.com/client.action?functionId=signBeanAct&body=%7B%22fp%22%3A%22-1%22%2C%22shshshfp%22%3A%22-1%22%2C%22shshshfpa%22%3A%22-1%22%2C%22referUrl%22%3A%22-1%22%2C%22userAgent%22%3A%22-1%22%2C%22jda%22%3A%22-1%22%2C%22rnVersion%22%3A%223.9%22%7D&appid=ld&client=apple&clientVersion=10.0.4&networkType=wifi&osVersion=14.8.1&uuid=3acd1f6361f86fc0a1bc23971b2e7bbe6197afb6&openudid=3acd1f6361f86fc0a1bc23971b2e7bbe6197afb6&jsonp=jsonp_1645885800574_58482";
        String body = "{\"eid\":\"eidAb47c8121a5s24aIy0D0WQXSKdROGt9BUSeGiNEbMeQodwSwkLi6x5/GTFC7BV7lPMjljpMxVNCcAW/qdrQvDSdhaI5715Sui3MB7nluMccMWqWFL\",\"fp\":\"-1\",\"jda\":\"-1\",\"referUrl\":\"-1\",\"rnVersion\":\"4.7\",\"shshshfp\":\"-1\",\"shshshfpa\":\"-1\",\"userAgent\":\"-1\"}";

        int n = 0;
        Map<String, String> header = Maps.newHashMap();
        RequestBody requestBody = new FormBody.Builder().add("body", body).build();

        for (String cookie : cookies) {
            String response = OkHttpUtils.post(url, cookie, requestBody, header);
            log.info("京东签到任务执行次数:{}, 结果:{}", ++n, response);
            Thread.sleep(1000L);
        }
        return "执行完成";
    }

    /**
     * 京东摇京豆签到
     *
     * @return
     */
    @Scheduled(cron = "0 0 7 1/1 * ?")
    public String getSharkBean() throws Exception {
        log.info("摇京豆签到开始");
        int n = 0;
        for (String cookie : cookies) {
            for (int i = 1; i < 8; i++) {
                String url = "https://api.m.jd.com/?appid=sharkBean&functionId=pg_interact_interface_invoke&body=%7B%22floorToken%22:%22f1d574ec-b1e9-43ba-aa84-b7a757f27f0e%22,%22dataSourceCode%22:%22signIn%22,%22argMap%22:%7B%22currSignCursor%22:" +
                        i +
                        "%7D,%22riskInformation%22:%7B%22platform%22:1,%22pageClickKey%22:%22%22,%22eid%22:%227IJ4SBWVAY6L5FOEQHCBZ57B3CYAYAA4LGJH2NGO6F6BE7PLEAJUY5WQOUI4BDGFRPH3RSGPLV5APHF4YV4DMJZ2UQ%22,%22fp%22:%22e0e4fadfadac7be71f89b78901f60fe4%22,%22shshshfp%22:%2298d7f7d062531be7af606b13b9c57a3e%22,%22shshshfpa%22:%222768c811-4a2f-1596-cf01-9d0cbd0319b9-1651280386%22,%22shshshfpb%22:%22iMZyawmZjTHrSJ72sZmuHog%22%7D%7D";

//                String cookie = "__jd_ref_cls=; mba_muid=16504967721461800060416.99.1651285154110; mba_sid=99.4; shshshfpa=2768c811-4a2f-1596-cf01-9d0cbd0319b9-1651280386; shshshfpb=iMZyawmZjTHrSJ72sZmuHog; 3AB9D23F7A4B3C9B=7IJ4SBWVAY6L5FOEQHCBZ57B3CYAYAA4LGJH2NGO6F6BE7PLEAJUY5WQOUI4BDGFRPH3RSGPLV5APHF4YV4DMJZ2UQ; _gia_s_e_joint={\"eid\":\"7IJ4SBWVAY6L5FOEQHCBZ57B3CYAYAA4LGJH2NGO6F6BE7PLEAJUY5WQOUI4BDGFRPH3RSGPLV5APHF4YV4DMJZ2UQ\",\"dt\":\"iPhone12,1\",\"ma\":\"\",\"im\":\"\",\"os\":\"iOS\",\"osv\":\"15.4.1\",\"ip\":\"120.244.234.209\",\"apid\":\"jdapp\",\"ia\":\"3B61EEC6-516C-4DA4-AF6C-E99F313C64D0\",\"uu\":\"\",\"cv\":\"10.0.4\",\"nt\":\"WIFI\",\"at\":\"1\"}; cid=8; shshshfp=dabca35fcbc92cb1d009acde19f60b81; shshshsID=6cd6fac4a28dea23e4e76cf97b9c6cf4_1_1651285134177; unpl=JF8EAKZnNSttWRkDURtVThUWHAgEWw1dH0dXOjMMAFVcTQQAEwZORxR7XlVdXhRKFx9sZhRUX1NIVw4YBCsiEEpcV1ZVC0kVAV9XNVddaEpkBRwAExEZQ1lWW1kMTBcEaWcAUVpeS1c1KwUbGyB7bVFeXAlOFQJobwxkXGhJVQQZBR0UFU1bZBUzCQYXBG1vBl1VXElRAR8FGxUWS1hRWVsISCcBb2cHUm1b%7CV2_ZzNtbRYAFxd9DUNcKRxYB2ILGloRUUYcIVpAAHsbWQZjVBEJclRCFnUUR11nGlgUZgIZXkFcQRRFCEJkexhdB24LFFtEUHMQfQ5GXH0pXAQJbRZeLAcCVEULRmR6KV5VNVYSCkVVRBUiAUEDKRgMBTRREV9KUUNGdlxAByhNWwVvBUIKEVBzJXwJdlR6GF0GZAoUWUdRQCUpUBkCJE0ZWTVcIlxyVnMURUooDytAGlU1Vl9fEgUWFSIPRFN7TlUCMFETDUIEERZ3AEBUKBoIAzRQRlpCX0VFIltBZHopXA%253d%253d; wxa_level=1; BATQW722QTLYVCRD={\"tk\":\"jdd014C2R445NQYRGM2X3QKQ7BOICIGKUL57ZGXLUETYHP6DPFMF3FC5FADWM5W3DOGWGEMFPMW3KGPGLDUNAB23DY5GEX3BCC4GPCJKUUJI01234567\",\"t\":1651285133703}; __jda=182444734.16504967721461800060416.1650496772.1651280279.1651285101.7; __jdb=182444734.2.16504967721461800060416|7.1651285101; __jdc=182444734; __jdv=182444734%7Cjdzt_refer_null%7Ct_232310336_1%7Cjzt-zhitou%7Cfiwrnksz5tchm01%7C1650496771000; _gia_s_local_fingerprint=e0e4fadfadac7be71f89b78901f60fe4; pre_seq=2; pre_session=3acd1f6361f86fc0a1bc23971b2e7bbe6197afb6|169; " +
//                        "pt_key=" + pt_key +
//                        "; " +
//                        "pt_pin=" + pt_pin +
//                        "; pwdt_id=jd_505bacd333f6b; sid=6ec7b9b0ff6956ee81461a9de14ae5bw; qd_fs=1651280528306; qd_ls=1651280528306; qd_sq=1; qd_ts=1651280528306; qd_uid=L2L5SI43-E24VE8SYB4FZVDYK53ZZ; qd_ad=-%7C-%7Cdirect%7C-%7C0; joyya=1651280501.0.32.0tuefmd; joyytokem=babel_23ebsEwajrvYj9qqsqhDJwZprQBoMDFuTUZPazk5MQ==.X3tzfllWfXJ2WFx0djENDRoNJF8dH3N3FV9hcGNaQnw4fRVfMy8CMRcsMSIxBBkOPTgkenQ8MQM4DiAMEA==.d806f45a; mobilev=touch; __jdu=16504967721461800060416; visitkey=36446698972455355";
                Map<String, String> header = new HashMap<>();
                header.put("origin", "https://spa.jd.com");
                header.put("referer", "https://spa.jd.com/");
                RequestBody requestBody = new FormBody.Builder().build();

                String response = OkHttpUtils.post(url, cookie, requestBody, header);
                log.info("摇京豆执行{}次，response:{}", ++n, response);
                JSONObject object = JSON.parseObject(response);
                String success = object.getString("success");
            }
        }
        return "success";
    }

    /**
     * 京豆抽奖任务，抽奖获取的京豆随机
     *
     * @return
     */
    @Scheduled(cron = "0 0 8 1/1 * ?")
    public String getLottery() throws Exception {
        String url = "https://api.m.jd.com/client.action?functionId=babelGetLottery";
//        String cookie = "__jd_ref_cls=Babel_H5FirstClick; mba_muid=16504967721461800060416.100.1651305586474; mba_sid=100.3; shshshfp=98d7f7d062531be7af606b13b9c57a3e; shshshfpa=2768c811-4a2f-1596-cf01-9d0cbd0319b9-1651280386; shshshfpb=iMZyawmZjTHrSJ72sZmuHog; shshshsID=8d0fce7416ae29c7b63dfe7754b256f1_1_1651305586715; __jda=182444734.16504967721461800060416.1650496772.1651285101.1651305562.8; __jdb=182444734.2.16504967721461800060416|8.1651305562; __jdc=182444734; __jdv=182444734%7Cjdzt_refer_null%7Ct_232310336_1%7Cjzt-zhitou%7Cfiwrnksz5tchm01%7C1650496771000; joyya=1651305562.0.36.04twf37; pre_seq=1; pre_session=3acd1f6361f86fc0a1bc23971b2e7bbe6197afb6|171; unpl=JF8EAKZnNSttWRkDURtVThUWHAgEWw1dH0dXOjMMAFVcTQQAEwZORxR7XlVdXhRKFx9sZhRUX1NIVw4YBCsiEEpcV1ZVC0kVAV9XNVddaEpkBRwAExEZQ1lWW1kMTBcEaWcAUVpeS1c1KwUbGyB7bVFeXAlOFQJobwxkXGhJVQQZBR0UFU1bZBUzCQYXBG1vBl1VXElRAR8FGxUWS1hRWVsISCcBb2cHUm1b%7CV2_ZzNtbRYAFxd9DUNcKRxYB2ILGloRUUYcIVpAAHsbWQZjVBEJclRCFnUUR11nGlgUZgIZXkFcQRRFCEJkexhdB24LFFtEUHMQfQ5GXH0pXAQJbRZeLAcCVEULRmR6KV5VNVYSCkVVRBUiAUEDKRgMBTRREV9KUUNGdlxAByhNWwVvBUIKEVBzJXwJdlR6GF0GZAoUWUdRQCUpUBkCJE0ZWTVcIlxyVnMURUooDytAGlU1Vl9fEgUWFSIPRFN7TlUCMFETDUIEERZ3AEBUKBoIAzRQRlpCX0VFIltBZHopXA%253d%253d; 3AB9D23F7A4B3C9B=7IJ4SBWVAY6L5FOEQHCBZ57B3CYAYAA4LGJH2NGO6F6BE7PLEAJUY5WQOUI4BDGFRPH3RSGPLV5APHF4YV4DMJZ2UQ; _gia_s_e_joint={\"eid\":\"7IJ4SBWVAY6L5FOEQHCBZ57B3CYAYAA4LGJH2NGO6F6BE7PLEAJUY5WQOUI4BDGFRPH3RSGPLV5APHF4YV4DMJZ2UQ\",\"dt\":\"iPhone12,1\",\"ma\":\"\",\"im\":\"\",\"os\":\"iOS\",\"osv\":\"15.4.1\",\"ip\":\"120.244.234.209\",\"apid\":\"jdapp\",\"ia\":\"3B61EEC6-516C-4DA4-AF6C-E99F313C64D0\",\"uu\":\"\",\"cv\":\"10.0.4\",\"nt\":\"WIFI\",\"at\":\"1\"}; cid=8; wxa_level=1; BATQW722QTLYVCRD={\"tk\":\"jdd014C2R445NQYRGM2X3QKQ7BOICIGKUL57ZGXLUETYHP6DPFMF3FC5FADWM5W3DOGWGEMFPMW3KGPGLDUNAB23DY5GEX3BCC4GPCJKUUJI01234567\",\"t\":1651285133703}; _gia_s_local_fingerprint=e0e4fadfadac7be71f89b78901f60fe4; " +
//                "pt_key=" + pt_key +
//                "; pt_pin=" + pt_pin +
//                "; pwdt_id=jd_505bacd333f6b; sid=6ec7b9b0ff6956ee81461a9de14ae5bw; qd_fs=1651280528306; qd_ls=1651280528306; qd_sq=1; qd_ts=1651280528306; qd_uid=L2L5SI43-E24VE8SYB4FZVDYK53ZZ; qd_ad=-%7C-%7Cdirect%7C-%7C0; mobilev=touch; __jdu=16504967721461800060416; visitkey=36446698972455355";
        String body = "{\"enAwardK\":\"ltvTJ/WYFPZcuWIWHCAjRz/NdrezuUkm8ZIGKKD06/oaqi8FPY5ILISE5QLULmK6RUnNSgnFndqy\\ny4p8d6/bK/bwdZK6Aw80mPSE7ShF/0r28HWSugMPNPm5JQ8b9nflgkMfDwDJiaqThDW7a9IYpL8z\\n7mu4l56kMNsaMgLecghsgTYjv+RZ8bosQ6kKx+PNAP61OWarrOeJ2rhtFmhQncw6DQFeBryeMUM1\\nw9SpK5iag4uLvHGIZstZMKOALjB/r9TIJDYxHs/sFMU4vtb2jX9DEwleHSLTLeRpLM1w+RakAk8s\\nfC4gHoKM/1zPHJXq1xfwXKFh5wKt4jr5hEqddxiI8N28vWT05HuOdPqtP+0EbGMDdSPdisoPmlru\\n+CyHR5Kt0js9JUM=_babel\",\"awardSource\":\"1\",\"srv\":\"{\\\"bord\\\":\\\"0\\\",\\\"fno\\\":\\\"0-0-2\\\",\\\"mid\\\":\\\"70952802\\\",\\\"bi2\\\":\\\"2\\\",\\\"bid\\\":\\\"0\\\",\\\"aid\\\":\\\"01155413\\\"}\",\"encryptProjectId\":\"3u4fVy1c75fAdDN6XRYDzAbkXz1E\",\"encryptAssignmentId\":\"2x5WEhFsDhmf8JohWQJFYfURTh9w\",\"authType\":\"2\",\"riskParam\":{\"platform\":\"3\",\"orgType\":\"2\",\"openId\":\"-1\",\"pageClickKey\":\"Babel_WheelSurf\",\"eid\":\"eidI69b381246dseNGdrD6vtTrOauSQ/zRycuDRnbInWZmVfFbyoI59uVkzYYiQZrUGzGkpqNpHHJHv37CthY6ooTnYpqX2mBZ2riJHvc8c9kta1QpZh\",\"fp\":\"-1\",\"shshshfp\":\"98d7f7d062531be7af606b13b9c57a3e\",\"shshshfpa\":\"2768c811-4a2f-1596-cf01-9d0cbd0319b9-1651280386\",\"shshshfpb\":\"iMZyawmZjTHrSJ72sZmuHog\",\"childActivityUrl\":\"https%3A%2F%2Fpro.m.jd.com%2Fmall%2Factive%2F2xoBJwC5D1Q3okksMUFHcJQhFq8j%2Findex.html%3Ftttparams%3DjyJinIeyJnTG5nIjoiMTE2LjQwNjQ1IiwiZ0xhdCI6IjQwLjA2MjkxIn60%253D%26un_area%3D1_2901_55565_0%26lng%3D116.4065317104862%26lat%3D40.06278498159455\",\"userArea\":\"-1\",\"client\":\"\",\"clientVersion\":\"\",\"uuid\":\"\",\"osVersion\":\"\",\"brand\":\"\",\"model\":\"\",\"networkType\":\"\",\"jda\":\"-1\"},\"siteClient\":\"apple\",\"mitemAddrId\":\"\",\"geo\":{\"lng\":\"116.4065317104862\",\"lat\":\"40.06278498159455\"},\"addressId\":\"5777681655\",\"posLng\":\"\",\"posLat\":\"\",\"homeLng\":\"116.40645\",\"homeLat\":\"40.06291\",\"focus\":\"\",\"innerAnchor\":\"\",\"cv\":\"2.0\"}";

        log.info("抽京豆开始");
        Map<String, String> header = Maps.newHashMap();
        header.put("origin", "https://pro.m.jd.com");
        header.put("referer", "https://pro.m.jd.com/");

        RequestBody requestBody = new FormBody.Builder()
                .add("body", body)
                .add("client", "wh5")
                .add("clientVersion", "1.0.0")
                .build();
        int n = 0;
        for (String cookie : cookies) {
            String response = OkHttpUtils.post(url, cookie, requestBody, header);
            log.info("抽京豆执行{}次，response：{}", ++n, response);
            Thread.sleep(1000L);

        }
        return "success";

    }

    /**
     * 京东plus会员签到
     *
     * @return
     * @throws IOException
     */
    @Scheduled(cron = "0 0 9 1/1 * ?")
    public String plusSign() throws Exception {
//        String cookie = "__jda=76161171.16512799826871486287173.1651279983.1651279983.1651279983.1; __jdc=76161171; __jdv=76161171|direct|-|none|-|1651279982687; __jdu=16512799826871486287173; areaId=1; ipLoc-djd=1-2802-0-0; PCSYCityID=CN_110000_110100_0; wxa_level=1; retina=1; jxsid=16512799876625643916; appCode=ms0ca95114; webp=1; mba_muid=16512799826871486287173; visitkey=142671134079494; sc_width=400; shshshfpa=7884f666-91a4-18a7-6fc9-b12b754ab327-1651280152; shshshfpb=oMiLjed03uL_RaQJxnJwqgQ; 3AB9D23F7A4B3C9B=IWPBYOW5BK53ZLQPLF3CODUTXGQNHUVF3BQW6YTGDSV5ZOH6Y5ND4NBV4DVFQYIT5LPVBNERC4JNL5JNDY5NOLYNHM; jcap_dvzw_fp=aS2oEeOT_LYmDdwn2ArUVyeat2PTKF8FKITMPZJzvUoAqnrPN-kKfnxf9N3AH3UREZYULA==; whwswswws=; TrackerID=-C9xSqVf4X7KepU2UMaDdRvT1FY2XfKJDFHViqXn7UimMcCiv6h63uOAg0YqeErmaR0QrqVqiwlJ3YujRjnckKaIC-J1tf_TDucPtCqABwzoZpIYinQT2BAeZ89lHSH6ieZFel8nMo15Qj3avkykbg; " +
//                "pt_key=" + pt_key +
//                "; pt_pin=" + pt_pin +
//                "; pt_token=dpd4uwmi; pwdt_id=%E9%BE%99%E6%88%98%E5%A3%AB520; sfstoken=tk01mdf701d1ca8sMisxKzN4MXgxGnMwyo0N/0U9alRF28hqmrssFFQh4QpqxRmjW+qPRNtacKlPqQjluHBa8vzB4/X2; autoOpenApp_downCloseDate_auto=1651280224263_1800000; equipmentId=IWPBYOW5BK53ZLQPLF3CODUTXGQNHUVF3BQW6YTGDSV5ZOH6Y5ND4NBV4DVFQYIT5LPVBNERC4JNL5JNDY5NOLYNHM; fingerprint=69738c14bb2a81939b5f018f509895a0; deviceVersion=100.0.4896.127; deviceOS=android; deviceOSVersion=6.0; deviceName=Chrome; wq_area=1_2802_0%7C1; cid=9; PPRD_P=UUID.16512799826871486287173-LOGID.1651280432023.772488001; sbx_hot_h=null; _gia_s_local_fingerprint=23c9371ecbeadbaa4231b9ba8f4a958b; __wga=1651280643494.1651280032643.1651280032643.1651280032643.13.1; jxsid_s_t=1651280643548; jxsid_s_u=https%3A//m.jd.com/portal/app_center.shtml; wqmnx1=MDEyNjM4MXQuP2xkOTZwczQxOWFpZDAgLyBiLk0gQzA2YmEzcmQyNE9JSEg%3D; autoOpenApp_downCloseDate_jd_homePage=1651280864563_1; plusCustomBuryPointToken=1651280914605_4020; __jdb=76161171.45.16512799826871486287173|1.1651279983; mba_sid=16512799879876217710087685309.44; shshshfp=a5de4b4af7a07ee5043c70faf357ca81; shshshsID=788f41f9898fe85b37980918e5860c5f_14_1651280943415; joyytokem=babel_3joSPpr7RgdHMbcuqoRQ8HbcPo9UMDFGRlluRzk5MQ==.d3BsX3V+dmBadHV2bxBxf3NgXyYAczZfOXdqb0J2ancnXDl3ODYjLgosPAp3dTMVMRUnFxMWKQwxKAkWOA==.6aac9775; _gia_s_e_joint={\"eid\":\"IWPBYOW5BK53ZLQPLF3CODUTXGQNHUVF3BQW6YTGDSV5ZOH6Y5ND4NBV4DVFQYIT5LPVBNERC4JNL5JNDY5NOLYNHM\",\"ma\":\"\",\"im\":\"\",\"os\":\"Android 6.x\",\"ip\":\"120.244.234.209\",\"ia\":\"\",\"uu\":\"\",\"at\":\"6\"}; __jd_ref_cls=Babel_dev_other_sign; joyya=1651280943.1651280955.54.0zl3krs";
        String url = "https://api.m.jd.com/client.action?functionId=doInteractiveAssignment";
        String body = "{\"sourceCode\":\"acetttsign\",\"encryptProjectId\":\"3FCTNcsr7BoQUw7dx1h3KJ9Hi9yJ\",\"encryptAssignmentId\":\"3o2cWjTjZoCjKJcQwQ2bFgLkTnZC\",\"completionFlag\":true,\"itemId\":\"1\",\"extParam\":{\"forceBot\":\"1\",\"businessData\":{\"random\":\"LLSuE5uy\"},\"signStr\":\"1651280954835~1OgLu20Tq0QMDFGRlluRzk5MQ==.d3BsX3V+dmBadHV2bxBxf3NgXyYAczZfOXdqb0J2ancnXDl3ODYjLgosPAp3dTMVMRUnFxMWKQwxKAkWOA==.6aac9775~1,1~18BD8887199573132F7270C7423274FE2B819200~1yxx4at~C~TRNMWhYJbWwUFUBdWxMCbBZXAxx6eRhydB0BBGYfBx8IBwQfQhMUFVAEG3N2G3VxGggOfhgCGAEIBxhHFGwUFVNBWBMCBhgRRUIaDRYCAAQJBQwDDwULBwMLDwMAAxYfFEZdUxYJFEVMQ0BHUEReFRgRQVRZFQ4RUFdMQ0FHQ1AaGxZDUl8aDW8ABB0JDgMfBwIUDhgCAx0JahgRXFsaDQUfFFJLFQ4RUwkOBVYHVwMMBABRUAIODlBRV1QMAgIBAwELBARRAVAaGxZdRhMCFXlSVXhWQ1FfFB0aQxYJBwcNBgYLBggNAwwAAx0aXV8RDBNZFRgRUEFaFQ4RWXxRe15WUgELQnhsZFBpfUxdfEZJUGURGhNWQRYJFHZXWFNfUxFxWVcdFB0aWVVFFAsaVBYfFEJbRRYJbQkKDhgGBgFlGxZBWRMCbBZSFB0aVhYfFFAaGxZSFB0aVhYfFFAaGxZSFGwUFV1cVxMCFVJVUFdeUUBHFB0aVl4RDBNNFRgRVVgaDRZEBR8NGQYRGhNbUWtFFAsaDg0RGhNaUxYJFENZWVBcWwx0e2dGcwRNThYfFFxSFQ5oBx0IGwRuGhNaW1tUFAsaVhYfFFxLUBYJFFAaSg==~1cs6hu7\",\"sceneid\":\"babel_3joSPpr7RgdHMbcuqoRQ8HbcPo9U\"},\"activity_id\":\"3joSPpr7RgdHMbcuqoRQ8HbcPo9U\",\"template_id\":\"00019605\",\"floor_id\":\"75471325\",\"enc\":\"A4737E261D02E91C30C566C1C671734D124B75F8759F591EFAFB127342C10708BAA7D80C309F2B17973BB15312D14004B865E9A1F04C7C3E3E312AA7309E7B31\"}";
        log.info("京东plus会员签到开始");
        Map<String, String> header = Maps.newHashMap();
        header.put("origin", "https://pro.m.jd.com");
        header.put("referer", "https://pro.m.jd.com/");

        RequestBody requestBody = new FormBody.Builder()
                .add("body", body)
                .add("appid", "babelh5")
                .add("sign", "11")
                .build();
        int n = 0;
        for (String cookie : cookies) {
            String response = OkHttpUtils.post(url, cookie, requestBody, header);
            log.info("京东plus会员签到执行{}次，response：{}", ++n, response);
            Thread.sleep(1000L);
        }

        return "success";
    }
}
