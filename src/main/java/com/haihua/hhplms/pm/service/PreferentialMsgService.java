package com.haihua.hhplms.pm.service;

import com.haihua.hhplms.ana.vo.UpdateStatusRequest;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.pm.entity.PreferentialMsg;
import com.haihua.hhplms.pm.vo.PreferentialMsgCreationVO;
import com.haihua.hhplms.pm.vo.PreferentialMsgUpdateVO;
import com.haihua.hhplms.pm.vo.PreferentialMsgVO;

import java.util.List;

public interface PreferentialMsgService {
    List<PreferentialMsgVO> loadLatestPreferentialMsgs();
    PreferentialMsgVO loadDetail(Long sid);
    PageWrapper<List<PreferentialMsgVO>> loadPreferentialMsgsByPage(String title,
                                                                    String status,
                                                                    Long companyInfoSid,
                                                                    Integer pageNo,
                                                                    Integer pageSize);
    PreferentialMsg createdPreferentialMsg(PreferentialMsgCreationVO preferentialMsgCreationVO);
    void updatePreferentialMsg(Long preferentialMsgSid, PreferentialMsgUpdateVO preferentialMsgUpdateVO);
    void updatePreferentialMsgStatus(Long preferentialMsgSid, UpdateStatusRequest updateStatusRequest);
    void deleteBySid(Long sid);
}
