package com.haihua.hhplms.ana.web.controller;

import com.haihua.hhplms.ana.entity.CompanyInfo;
import com.haihua.hhplms.ana.service.CompanyInfoService;
import com.haihua.hhplms.ana.vo.CompanyInfoCreationVO;
import com.haihua.hhplms.ana.vo.CompanyInfoUpdateVO;
import com.haihua.hhplms.ana.vo.CompanyInfoVO;
import com.haihua.hhplms.ana.vo.UpdateStatusRequest;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.model.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyInfoController {
    @Autowired
    @Qualifier("companyInfoService")
    private CompanyInfoService companyInfoService;

    @GetMapping(path = "/api/ana/companyInfos", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<PageWrapper<List<CompanyInfoVO>>> loadCompanyInfos(@RequestParam(name = "code", required = false) String code,
                                                                                 @RequestParam(name = "type", required = false) String type,
                                                                                 @RequestParam(name = "contactName", required = false) String contactNameLike,
                                                                                 @RequestParam(name = "contactMobile", required = false) String contactMobileLike,
                                                                                 @RequestParam(name = "status", required = false) String status,
                                                                                 @RequestParam(name = "pageNo") Integer pageNo,
                                                                                 @RequestParam(name = "pageSize") Integer pageSize) {
        PageWrapper<List<CompanyInfoVO>> companyInfos = companyInfoService.loadCompanyInfosByPage(code, type, contactNameLike, contactMobileLike, status, pageNo, pageSize);
        return ResultBean.Success.of(companyInfos, "");
    }

    @GetMapping(path = "/api/ana/companyInfos/available", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<CompanyInfoVO>> getAvailableRoles() {
        return ResultBean.Success.of(companyInfoService.getAvailableCompanyInfos(), "");
    }

    @DeleteMapping(path = "/api/ana/companyInfos/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> deleteCompanyInfo(@PathVariable("id") Long sid) {
        companyInfoService.deleteBySid(sid);
        return ResultBean.Success.of(sid, "");
    }

    @PostMapping(path = "/api/ana/companyInfos", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> createCompanyInfo(@RequestBody CompanyInfoCreationVO companyInfoCreationVO) {
        CompanyInfo companyInfo = companyInfoService.createCompanyInfo(companyInfoCreationVO);
        return ResultBean.Success.of(companyInfo.getSid(), "");
    }

    @PutMapping(path = "/api/ana/companyInfos/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> updateCompanyInfo(@PathVariable("id") Long sid, @RequestBody CompanyInfoUpdateVO companyInfoUpdateVO) {
        companyInfoService.updateCompanyInfo(sid, companyInfoUpdateVO);
        return ResultBean.Success.of(sid, "");
    }

    @PatchMapping(path = "/api/ana/companyInfos/{id}/updateStatus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> updateCompanyInfoStatus(@PathVariable("id") Long sid,
                                                            @RequestBody UpdateStatusRequest updateStatusRequest) {
        companyInfoService.updateCompanyInfoStatus(sid, updateStatusRequest);
        return ResultBean.Success.of(sid, "");
    }
}
