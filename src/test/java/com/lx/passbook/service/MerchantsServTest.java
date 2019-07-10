package com.lx.passbook.service;

import com.alibaba.fastjson.JSON;
import com.lx.passbook.vo.CreateMerchantsRequest;
import com.lx.passbook.vo.PassTemplate;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MerchantsServTest {

    @Autowired
    private IMerchantsServ merchantsServ;

    @Test
    //@Transactional 加上该注解表示，传入的数据仅是作为测试用例，不写入数据库（或者说自动回滚）
    public void testCreateMerchantServ() {

        CreateMerchantsRequest request = new CreateMerchantsRequest();
        request.setName("whale-2");
        request.setLogoUrl("www.baidu2.com");
        request.setBusinessLicenseUrl("www.baidu2.com");
        request.setPhone("1244565623");
        request.setAddress("NanJing");

        System.out.println(JSON.toJSONString(merchantsServ.createMerchants(request)));
    }

    @Test
    public void testBuildMerchantsInfoById(){
        System.out.println(JSON.toJSONString(merchantsServ.buildMerchantsInfoById(5)));
        //{
        //      "data":{"address":"Shanghai","businessLicenseUrl":"www.baidu.com",
        //              "id":5,"isAudit":true,"logoUrl":"www.baidu.com",
        //              "name":"whale","phone":"1244567890"},
        //      "errorCode":0,
        //      "errorMsg":""}
    }

    @Test
    public void testDropPassTemplate(){

        PassTemplate passTemplate = new PassTemplate();
        passTemplate.setId(5);
        passTemplate.setTitle("title: whale-2");
        passTemplate.setSummary("summary: whale-2");
        passTemplate.setDesc("detail: whale-2");
        passTemplate.setLimit(10000L);
        passTemplate.setHasToken(true);
        passTemplate.setBackground(2);
        passTemplate.setStart(new Date());
        passTemplate.setEnd(DateUtils.addDays(new Date(), 10));

        System.out.println(JSON.toJSONString(
                merchantsServ.dropPassTemplate(passTemplate)
        ));

    }
}
