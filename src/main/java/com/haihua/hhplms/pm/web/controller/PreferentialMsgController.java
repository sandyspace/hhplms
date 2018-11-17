package com.haihua.hhplms.pm.web.controller;

import com.haihua.hhplms.ana.vo.UpdateStatusRequest;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.model.ResultBean;
import com.haihua.hhplms.pm.entity.PreferentialMsg;
import com.haihua.hhplms.pm.service.PreferentialMsgService;
import com.haihua.hhplms.pm.vo.PreferentialMsgCreationVO;
import com.haihua.hhplms.pm.vo.PreferentialMsgUpdateVO;
import com.haihua.hhplms.pm.vo.PreferentialMsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PreferentialMsgController {
    @Autowired
    @Qualifier("preferentialMsgService")
    private PreferentialMsgService preferentialMsgService;

    @GetMapping(path = "/api/pm/preferentialMsgs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<PageWrapper<List<PreferentialMsgVO>>> loadPreferentialMsgs(@RequestParam(name = "title", required = false) String titleLike,
                                                                                         @RequestParam(name = "status", required = false) String status,
                                                                                         @RequestParam(name = "companyId", required = false) Long companyInfoSid,
                                                                                         @RequestParam(name = "pageNo") Integer pageNo,
                                                                                         @RequestParam(name = "pageSize") Integer pageSize) {
        PageWrapper<List<PreferentialMsgVO>> pageOfPreferentialMsgs = preferentialMsgService.loadPreferentialMsgsByPage(titleLike, status, companyInfoSid, pageNo, pageSize);
        return ResultBean.Success.of(pageOfPreferentialMsgs, "");
    }

    @GetMapping(path = "/api/pm/preferentialMsgs/latest", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<PreferentialMsgVO>> loadLatestPreferentialMsgs() {
        return ResultBean.Success.of(preferentialMsgService.loadLatestPreferentialMsgs(), "");
    }

    @PostMapping(path = "/api/pm/preferentialMsgs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> createPreferentialMsg(@RequestBody PreferentialMsgCreationVO preferentialMsgCreationVO) {
        PreferentialMsg preferentialMsg = preferentialMsgService.createdPreferentialMsg(preferentialMsgCreationVO);
        return ResultBean.Success.of(preferentialMsg.getSid(), "");
    }

    @GetMapping(path = "/api/pm/preferentialMsgs/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<PreferentialMsgVO> loadDetail(@PathVariable("id") Long sid) {
        return ResultBean.Success.of(preferentialMsgService.loadDetail(sid), "");
    }

    @PutMapping(path = "/api/pm/preferentialMsgs/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> updatePreferentialMsg(@PathVariable("id") Long sid, @RequestBody PreferentialMsgUpdateVO preferentialMsgUpdateVO) {
        preferentialMsgService.updatePreferentialMsg(sid, preferentialMsgUpdateVO);
        return ResultBean.Success.of(sid, "");
    }

    @DeleteMapping(path = "/api/pm/preferentialMsgs/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> deletePreferentialMsg(@PathVariable("id") Long sid) {
        preferentialMsgService.deleteBySid(sid);
        return ResultBean.Success.of(sid, "");
    }

    @PatchMapping(path = "/api/pm/preferentialMsgs/{id}/updateStatus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> updatePreferentialMsgStatus(@PathVariable("id") Long sid, @RequestBody UpdateStatusRequest updateStatusRequest) {
        preferentialMsgService.updatePreferentialMsgStatus(sid, updateStatusRequest);
        return ResultBean.Success.of(sid, "");
    }
}
