package com.haihua.hhplms.sys.web.controller;

import com.haihua.hhplms.common.model.ResultBean;
import com.haihua.hhplms.sys.service.SmsService;
import com.haihua.hhplms.sys.vo.RetrieveDynamicCodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class SmsController {

    @Autowired
    @Qualifier("smsService")
    private SmsService smsService;

    @PostMapping(path = "/api/sys/dynamicCodes/retrieve", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<String> retrieveDynamicCode(@RequestBody final RetrieveDynamicCodeRequest retrieveDynamicCodeRequest) {
        smsService.sendDynamicCode(retrieveDynamicCodeRequest.getMobile());
        return ResultBean.Success.of("", "发送成功");
    }
}
