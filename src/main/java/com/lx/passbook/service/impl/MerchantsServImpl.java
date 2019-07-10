package com.lx.passbook.service.impl;

import com.alibaba.fastjson.JSON;
import com.lx.passbook.constant.Constants;
import com.lx.passbook.constant.ErrorCode;
import com.lx.passbook.dao.MerchantsDao;
import com.lx.passbook.entity.Merchants;
import com.lx.passbook.service.IMerchantsServ;
import com.lx.passbook.vo.CreateMerchantsRequest;
import com.lx.passbook.vo.CreateMerchantsResponse;
import com.lx.passbook.vo.PassTemplate;
import com.lx.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MerchantsServImpl implements IMerchantsServ {

    //日志对象
    //private Logger logger = LoggerFactory.getLogger(this.getClass());

    //数据库接口
    private final MerchantsDao merchantsDao;

    //kafka 客户端
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public MerchantsServImpl(MerchantsDao merchantsDao,
                             KafkaTemplate<String, String> kafkaTemplate){
        this.merchantsDao=merchantsDao;
        this.kafkaTemplate=kafkaTemplate;
    }

    @Override
    @Transactional
    public Response createMerchants(CreateMerchantsRequest request) {

        Response response=new Response();
        CreateMerchantsResponse merchantsResponse=new CreateMerchantsResponse();

        ErrorCode errorCode=request.validate(merchantsDao);
        if(errorCode != ErrorCode.SUCCESS){//未通过验证
            merchantsResponse.setId(-1);
            response.setErrorCode(errorCode.getCode());
            response.setErrorMsg(errorCode.getDesc());
        }else{//通过验证
            //save保存该对象，并返回该对象
            merchantsResponse.setId(merchantsDao.save(request.toMerchants()).getId());
        }
        response.setData(merchantsResponse);
        return response;
    }

    @Override
    public Response buildMerchantsInfoById(Integer id) {

        Response response=new Response();

        Merchants merchants=merchantsDao.findById(id);
        if(null == merchants){
            response.setErrorCode(ErrorCode.MERCHANTS_NOT_EXIST.getCode());
            response.setErrorMsg(ErrorCode.MERCHANTS_NOT_EXIST.getDesc());
        }
        response.setData(merchants);

        return response;
    }

    @Override
    public Response dropPassTemplate(PassTemplate template) {

        Response response=new Response();
        ErrorCode errorCode=template.validate(merchantsDao);

        if(errorCode != ErrorCode.SUCCESS){//未通过验证
            response.setErrorCode(errorCode.getCode());
            response.setErrorMsg(errorCode.getDesc());
        }else{
            String passTemplate= JSON.toJSONString(template);
            kafkaTemplate.send(
                    Constants.TEMPLATE_TOPIC,
                    Constants.TEMPLATE_TOPIC,
                    passTemplate
            );
            log.info("DropPassTemplates: {}", passTemplate);
        }
        return response;
    }







}
