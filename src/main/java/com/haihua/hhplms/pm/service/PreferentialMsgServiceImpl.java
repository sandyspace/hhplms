package com.haihua.hhplms.pm.service;

import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.ana.vo.UpdateStatusRequest;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.utils.EnumUtil;
import com.haihua.hhplms.common.utils.WebUtils;
import com.haihua.hhplms.pm.entity.PreferentialMsg;
import com.haihua.hhplms.pm.mapper.PreferentialMsgMapper;
import com.haihua.hhplms.pm.vo.PreferentialMsgCreationVO;
import com.haihua.hhplms.pm.vo.PreferentialMsgUpdateVO;
import com.haihua.hhplms.pm.vo.PreferentialMsgVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("preferentialMsgService")
public class PreferentialMsgServiceImpl implements PreferentialMsgService {
    @Autowired
    private PreferentialMsgMapper preferentialMsgMapper;

    public List<PreferentialMsgVO> loadLatestPreferentialMsgs() {
        Map<String, Object> params = new HashMap<>();
        params.put("status", PreferentialMsg.Status.RELEASE);
        params.put("offset", 0);
        params.put("limit", 100);

        List<PreferentialMsg> latestPreferentialMsgs = findByParams(params);
        if (Objects.nonNull(latestPreferentialMsgs) && !latestPreferentialMsgs.isEmpty()) {
            return latestPreferentialMsgs.stream()
                    .map(latestPreferentialMsg -> new PreferentialMsgVO(latestPreferentialMsg))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public PreferentialMsgVO loadDetail(Long sid) {
        PreferentialMsg matchedPreferentialMsg = findBySid(sid);
        if (Objects.isNull(matchedPreferentialMsg)) {
            throw new ServiceException("Notification with id: [" + sid + "] does not exist");
        }
        return new PreferentialMsgVO(matchedPreferentialMsg);
    }

    public PageWrapper<List<PreferentialMsgVO>> loadPreferentialMsgsByPage(String title,
                                                                           String status,
                                                                           Long companyInfoSid,
                                                                           Integer pageNo,
                                                                           Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isBlank(title)) {
            params.put("title", title);
        }
        if(!StringUtils.isBlank(status)) {
            params.put("status",status);
        }
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            params.put("companyInfoSid", WebUtils.getCompanyId());
        } else {
            if (Objects.nonNull(companyInfoSid)) {
                params.put("companyInfoSid",companyInfoSid);
            }
        }
        return loadPreferentialMsgsByPage(params, pageNo, pageSize);
    }

    public PageWrapper<List<PreferentialMsgVO>> loadPreferentialMsgsByPage(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        int count = countByParams(params);
        PageWrapper<List<PreferentialMsgVO>> pageOfPreferentialMsgs = new PageWrapper<>(pageNo, pageSize, count);
        if (count > 0) {
            pageNo = Objects.isNull(pageNo) ? GlobalConstant.DEFAULT_PAGE_NO : pageNo;
            pageSize = Objects.isNull(pageSize) ? GlobalConstant.DEFAULT_PAGE_SIZE : pageSize;
            params.put("offset", (pageNo - 1) * pageSize);
            params.put("limit", pageNo * pageSize);
            List<PreferentialMsg> matchedPreferentialMsgs = findByParams(params);
            if (Objects.nonNull(matchedPreferentialMsgs) && !matchedPreferentialMsgs.isEmpty()) {
                pageOfPreferentialMsgs.setResult(matchedPreferentialMsgs.stream()
                        .map(matchedPreferentialMsg -> new PreferentialMsgVO(matchedPreferentialMsg))
                        .collect(Collectors.toList()));
            }
        }
        return pageOfPreferentialMsgs;
    }

    public PreferentialMsg findBySid(Long sid) {
        return findSingle("sid", sid);
    }

    private PreferentialMsg findSingle(String paramName, Object paramValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(paramName, paramValue);
        List<PreferentialMsg> matchedPreferentialMsgs = findByParams(params);
        if (Objects.nonNull(matchedPreferentialMsgs) && !matchedPreferentialMsgs.isEmpty()) {
            return matchedPreferentialMsgs.get(0);
        }
        return null;
    }

    public int countByParams(Map<String, Object> params) {
        int count;
        try {
            count = preferentialMsgMapper.countByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return count;
    }

    public List<PreferentialMsg> findByParams(Map<String, Object> params) {
        List<PreferentialMsg> matchedPreferentialMsgs;
        try {
            matchedPreferentialMsgs = preferentialMsgMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedPreferentialMsgs;
    }

    public PreferentialMsg createdPreferentialMsg(PreferentialMsgCreationVO preferentialMsgCreationVO) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            preferentialMsgCreationVO.setCompanyId(WebUtils.getCompanyId());
        }
        PreferentialMsg preferentialMsg = new PreferentialMsg();
        preferentialMsg.setTitle(preferentialMsgCreationVO.getTitle());
        preferentialMsg.setContent(preferentialMsgCreationVO.getContent());
        preferentialMsg.setImgUrl(preferentialMsgCreationVO.getImgUrl());
        preferentialMsg.setStatus(EnumUtil.codeOf(PreferentialMsg.Status.class, preferentialMsgCreationVO.getStatus()));
        preferentialMsg.setCompanyInfoSid(preferentialMsgCreationVO.getCompanyId());

        preferentialMsg.setCreatedBy(WebUtils.getLoginName());
        preferentialMsg.setCreatedTime(new Date(System.currentTimeMillis()));
        preferentialMsg.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);

        createdPreferentialMsg(preferentialMsg);
        return preferentialMsg;
    }

    public int createdPreferentialMsg(PreferentialMsg preferentialMsg) {
        int insertedRows;
        try {
            insertedRows = preferentialMsgMapper.create(preferentialMsg);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return insertedRows;
    }

    public void updatePreferentialMsg(Long preferentialMsgSid, PreferentialMsgUpdateVO preferentialMsgUpdateVO) {
        PreferentialMsg editingPreferentialMsg = findBySid(preferentialMsgSid);
        if (Objects.isNull(editingPreferentialMsg)) {
            throw new ServiceException("PreferentialMsg with id: [" + preferentialMsgSid + "] does not exist");
        }
        if (Role.Category.ACCOUNT.getCode().equals(WebUtils.getUserType())) {
            if (!WebUtils.getCompanyId().equals(editingPreferentialMsg.getCompanyInfoSid())) {
                throw new ServiceException("You have insufficient right to edit this PreferentialMsg");
            }
            preferentialMsgUpdateVO.setCompanyId(null);
        }

        PreferentialMsg preferentialMsg = new PreferentialMsg();
        preferentialMsg.setSid(preferentialMsgSid);
        preferentialMsg.setTitle(preferentialMsgUpdateVO.getTitle());
        preferentialMsg.setContent(preferentialMsgUpdateVO.getContent());
        preferentialMsg.setImgUrl(preferentialMsgUpdateVO.getImgUrl());
        preferentialMsg.setCompanyInfoSid(preferentialMsgUpdateVO.getCompanyId());

        preferentialMsg.setUpdatedBy(WebUtils.getLoginName());
        preferentialMsg.setUpdatedTime(new Date(System.currentTimeMillis()));
        preferentialMsg.setVersionNum(editingPreferentialMsg.getVersionNum());

        int updatedRows = updatePreferentialMsg(preferentialMsg);
        if (updatedRows == 0) {
            throw new ServiceException("PreferentialMsg is being edited by others, please try again later");
        }
    }

    public int updatePreferentialMsg(PreferentialMsg preferentialMsg) {
        int updatedRows;
        try {
            updatedRows = preferentialMsgMapper.updateByExample(preferentialMsg);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return updatedRows;
    }

    public void updatePreferentialMsgStatus(Long preferentialMsgSid, UpdateStatusRequest updateStatusRequest) {
        PreferentialMsg preferentialMsg = findBySid(preferentialMsgSid);
        if (Objects.isNull(preferentialMsg)) {
            throw new ServiceException("PreferentialMsg with id: [" + preferentialMsgSid + "] does not exist, failed to change status");
        }
        if (Role.Category.ACCOUNT.getCode().equals(WebUtils.getUserType())) {
            if (!WebUtils.getCompanyId().equals(preferentialMsg.getCompanyInfoSid())) {
                throw new ServiceException("You have insufficient right to change status of this PreferentialMsg");
            }
        }

        Map<String, Object> params = new HashMap<>();
        params.put("status", updateStatusRequest.getStatus());
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", preferentialMsg.getVersionNum());
        params.put("sid", preferentialMsg.getSid());

        int updatedRows = updatePreferentialMsg(params);
        if (updatedRows == 0) {
            throw new ServiceException("PreferentialMsg is being edited by others, please try again later");
        }
    }

    public int updatePreferentialMsg(Map<String, Object> params) {
        int updatedRows;
        try {
            updatedRows = preferentialMsgMapper.updateByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return updatedRows;
    }

    public void deleteBySid(Long sid) {
        PreferentialMsg preferentialMsg = findBySid(sid);
        if (Objects.isNull(preferentialMsg)) {
            throw new ServiceException("PreferentialMsg does not exist, failed to delete");
        }
        if (Role.Category.ACCOUNT.getCode().equals(WebUtils.getUserType())) {
            if (!WebUtils.getCompanyId().equals(preferentialMsg.getCompanyInfoSid())) {
                throw new ServiceException("You have insufficient right to delete this PreferentialMsg");
            }
        }

        Map<String, Object> params = new HashMap<>();
        params.put("sid", sid);
        int deletedRows = deleteByParams(params);
        if (deletedRows == 0) {
            throw new ServiceException("PreferentialMsg with id:[" + sid + "] has been deleted by others");
        }
    }

    public int deleteByParams(Map<String, Object> params) {
        int deletedRows;
        try {
            deletedRows = preferentialMsgMapper.deleteByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return deletedRows;
    }
}
