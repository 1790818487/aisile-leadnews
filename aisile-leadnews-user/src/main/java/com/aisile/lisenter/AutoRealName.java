package com.aisile.lisenter;

import com.aisile.model.user.pojos.ApUserRealname;
import com.aisile.service.IApUserRealnameService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AutoRealName {

    @Autowired
    private IApUserRealnameService mapper;

    @Autowired
    private RestTemplate restTemplate;

    //监听消息队列,如果里面有咩有处理的信息,就调用自动实名认证执行
    @RabbitListener(queues = "queen.real")
    public void autoRealName(int id) {
        LambdaQueryWrapper<ApUserRealname> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApUserRealname::getId, id);
        ApUserRealname userRealname = mapper.getOne(wrapper);

//        //如果ocr识别没通过,直接就退出,并保存错误信息
        if ("0".equals(ocrIdCard().get("code"))) {
            userRealname.setReason((String) ocrIdCard().get("message"));
            userRealname.setUpdatedTime(new Date());
            userRealname.setStatus((short) 2);
            mapper.updateById(userRealname);
            return;
        }
//认证太快了,停三秒
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("开始执行身份证二要素核验");
//如果身份证二要素没通过,一样直接退出并保存错误的信息
        if ((int) IdCardElement().get("code") != 400100) {
            userRealname.setReason((String) ocrIdCard().get("message"));
            userRealname.setStatus((short) 2);
            userRealname.setUpdatedTime(new Date());
            mapper.updateById(userRealname);
            return;
        }

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("开始活体验证");
        //这里进行活体验证
        Map map = LivingCertification();
        Map data = (Map) map.get("data");
        System.out.println(data.get("code"));
        if (Integer.parseInt(String.valueOf(data.get("code"))) != 0) {
            userRealname.setReason((String) ocrIdCard().get("message"));
            userRealname.setStatus((short) 2);
            userRealname.setUpdatedTime(new Date());
            mapper.updateById(userRealname);
            return;
        }
        if (Integer.parseInt(String.valueOf(data.get("score"))) < 80) {
            userRealname.setReason("活体认证不达标");
            userRealname.setStatus((short) 2);
            userRealname.setUpdatedTime(new Date());
            mapper.updateById(userRealname);
            return;
        }
        //全部验证通过后,更新数据库信息为审核通过
        userRealname.setStatus((short) 9);
        userRealname.setReason("");
        userRealname.setUpdatedTime(new Date());
        mapper.updateById(userRealname);
    }

    //活体认证
    public Map LivingCertification() {
        HttpHeaders headers = new HttpHeaders();//请求参数
        headers.set("apicode", "ce5080c028ff4931b55d13c51f576716");//APICODE
        headers.set("Content-Type", "application/json");

        Map<String, Object> map = new HashMap<>();//请求头参数
        map.put("image", "https://hmtt122.oss-cn-shanghai.aliyuncs.com/222.png");
        map.put("imageType", "URL");
        HttpEntity<Object> entity = new HttpEntity<>(map, headers);
        return restTemplate.postForObject(
                "https://api.yonyoucloud.com/apis/dst/Biologicalexamination/Biologicalexamination",
                entity, Map.class);
    }

    //身份证ocr信息识别
    public Map ocrIdCard() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apicode", "2ce8920918e745a39ade7ad7497040a0");
        headers.set("Content-Type", "application/json");
        Map<String, Object> params = new HashMap<>();
        params.put("image", "http://m.qpic.cn/psc?/V14HDEj52mmhL4/ruAMsa53pVQWN7FLK88i5thbNFAKwfGA8JeCFfsBi8zdqMf4nE58X*nLwPugab3kTyEefZuXCYrojGMshJ8LsEhzB49mu0Gr5ep.KsR1zvs!/b&bo=QAYfCkAGHwoBFzA!&rf=viewer_4");
        params.put("imageType", "URL");
        params.put("ocrType", "0");
        HttpEntity<Object> entity = new HttpEntity<>(params, headers);
        //ocr图片识别
        return restTemplate.postForObject("https://api.yonyoucloud.com/apis/dst/IdcardOCR/IdcardOCR", entity, Map.class);
    }

    //身份证二要素认证
    public Map IdCardElement() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("apicode", "3b640984f12345ebb6fd2fa369283d64");
//        headers.set("Content-Type", "application/json");
//        Map<String, Object> params = new HashMap<>();//请求参数
//        params.put("idNumber", "420683199912273152");
//        params.put("userName", "邓黎明");
//        HttpEntity<Object> entity = new HttpEntity<>(params, headers);
//        Map map = restTemplate.postForObject("https://api.yonyoucloud.com/apis/dst/matchIdentity/matchIdentity",
//                entity, Map.class);
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("code", 400100);
        map.put("message", "一致");
        return map;
    }

//    @Override
//    public ResponseResult addRealNameUser() {
//
//
//
////        HttpHeaders headers = new HttpHeaders();
////        headers.set("apicode", "2ce8920918e745a39ade7ad7497040a0");
////        headers.set("Content-Type", "application/json");
////
////        Map<String, Object> params = new HashMap<>();
////        params.put("image","https://hmtt122.oss-cn-shanghai.aliyuncs.com/demo_idcard.png");
////        params.put("imageType","URL");
////        params.put("ocrType","0");
////        HttpEntity<Object> entity = new HttpEntity<>(params,headers);
////        //ocr图片识别
////        Map map = restTemplate.postForObject("https://api.yonyoucloud.com/apis/dst/IdcardOCR/IdcardOCR", entity, Map.class);
////        System.out.println(map);
////
////        for (Object o : map.keySet()) {
////            System.out.println(map.get(o));
////        }
///*
//* {message=成功, data={tradeNo=1169971139238436865, code=0, riskType=normal, address=沈阳市东陵区文化东路24-8号1-3-6, birth=19510322, name=王东镇, cardNum=210103195103222113, sex=男, nation=汉, issuingDate=, issuingAuthority=, expiryDate=}, code=601200000}
//成功
//{tradeNo=1169971139238436865, code=0, riskType=normal, address=沈阳市东陵区文化东路24-8号1-3-6, birth=19510322, name=王东镇, cardNum=210103195103222113, sex=男, nation=汉, issuingDate=, issuingAuthority=, expiryDate=}
//601200000
//*
//* */
////        HttpHeaders headers = new HttpHeaders();
////        headers.set("apicode", "3b640984f12345ebb6fd2fa369283d64");
////        headers.set("Content-Type", "application/json");
////        Map<String, Object> params = new HashMap<>();//请求参数
////        params.put("idNumber", "420683199912273152");
////        params.put("userName", "邓黎明");
////        HttpEntity<Object> entity = new HttpEntity<>(params, headers);
////
//////        Map map = restTemplate.postForObject("https://api.yonyoucloud.com/apis/dst/matchIdentity/matchIdentity",
//////                entity, Map.class);
////        Map<String, Object>  map=new HashMap<>();
////        map.put("success",true);
////        map.put("code",400100);
////        map.put("message","一致");
//
//
////        assert map != null;
//        /*
//         *
//         * {success=true, code=400100, message=一致, data={orderNumber=011698976374753704}}
//         * true
//         * 400100
//         * 一致
//         * {orderNumber=011698976374753704}
//         */
//
//
//
//        return null;
//    }
}
