package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.ana.entity.TemplateRole;
import com.haihua.hhplms.ana.entity.TemplateRolePermissionRelationship;
import com.haihua.hhplms.ana.mapper.TemplateRoleMapper;
import com.haihua.hhplms.ana.vo.TemplateRoleCreationVO;
import com.haihua.hhplms.ana.vo.TemplateRoleUpdateVO;
import com.haihua.hhplms.ana.vo.TemplateRoleVO;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.utils.EnumUtil;
import com.haihua.hhplms.common.utils.JsonUtil;
import com.haihua.hhplms.common.utils.ListUtils;
import com.haihua.hhplms.common.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("templateRoleService")
public class TemplateRoleServiceImpl implements TemplateRoleService {

    private static final Logger log = LoggerFactory.getLogger(TemplateRoleServiceImpl.class);

    @Autowired
    private TemplateRoleMapper templateRoleMapper;

    @Autowired
    @Qualifier("templateRolePermissionRelationshipService")
    private TemplateRolePermissionRelationshipService templateRolePermissionRelationshipService;

    public PageWrapper<List<TemplateRoleVO>> listTempRoleByPage(String codeLike,
                                                                String nameLike,
                                                                String type,
                                                                Integer pageNo,
                                                                Integer pageSize) {
        if (!WebUtils.isEmployee()) {
            throw new ServiceException("你不是" + Role.Category.EMPLOYEE.getName() + "，请立刻停止非法操作");
        }

        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isBlank(codeLike)) {
            params.put("codeLike", codeLike);
        }
        if (!StringUtils.isBlank(nameLike)) {
            params.put("nameLike", nameLike);
        }
        if (!StringUtils.isBlank(type)) {
            params.put("type", type);
        }
        return listTempRoleByPage(params, pageNo, pageSize);
    }

    public PageWrapper<List<TemplateRoleVO>> listTempRoleByPage(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        int count = countByParams(params);
        PageWrapper<List<TemplateRoleVO>> pageOfTemplateRoles = new PageWrapper<>(pageNo, pageSize, count);
        if (count > 0) {
            pageNo = Objects.isNull(pageNo) ? GlobalConstant.DEFAULT_PAGE_NO : pageNo;
            pageSize = Objects.isNull(pageSize) ? GlobalConstant.DEFAULT_PAGE_SIZE : pageSize;
            params.put("offset", (pageNo - 1) * pageSize);
            params.put("limit", pageSize);
            List<TemplateRole> matchedTemplateRoles = findByParams(params);
            pageOfTemplateRoles.setResult(matchedTemplateRoles.stream()
                    .map(matchedTemplateRole -> new TemplateRoleVO(matchedTemplateRole))
                    .collect(Collectors.toList()));
        }
        return pageOfTemplateRoles;
    }

    public TemplateRoleVO loadDetail(Long sid) {
        if (!WebUtils.isEmployee()) {
            throw new ServiceException("你不是" + Role.Category.EMPLOYEE.getName() + "，请立刻停止非法操作");
        }
        TemplateRole tempRole = findBySid(sid);
        if (Objects.isNull(tempRole)) {
            throw new ServiceException("ID为[" + sid + "]的模板角色不存在");
        }
        return new TemplateRoleVO(tempRole);
    }

    public TemplateRole createTempRole(TemplateRoleCreationVO templateRoleCreationVO) {
        if (!WebUtils.isEmployee()) {
            throw new ServiceException("你不是" + Role.Category.EMPLOYEE.getName() + "，请立刻停止非法操作");
        }

        if (log.isInfoEnabled()) {
            log.info(String.format("templateRoleCreationVO: %s, createdBy: {loginName: %s, userType: %s, subType: %s}", templateRoleCreationVO, WebUtils.getLoginName(), WebUtils.getUserType(), WebUtils.getSubType()));
        }

        if (TemplateRole.Type.OWNER == EnumUtil.codeOf(TemplateRole.Type.class, templateRoleCreationVO.getType())) {
            List<TemplateRole> tempRolesOfOwnerType = findByType(TemplateRole.Type.OWNER);
            if (Objects.nonNull(tempRolesOfOwnerType) && !tempRolesOfOwnerType.isEmpty()) {
                throw new ServiceException(String.format("只能创建一个类型为%s的模板角色", TemplateRole.Type.OWNER.getName()));
            }
        }

        if (codeExist(templateRoleCreationVO.getCode())) {
            throw new ServiceException(String.format("编码[%s]已被占用", templateRoleCreationVO.getCode()));
        }

        TemplateRole tempRole = new TemplateRole();
        tempRole.setCode(templateRoleCreationVO.getCode());
        tempRole.setName(templateRoleCreationVO.getName());
        tempRole.setMemo(templateRoleCreationVO.getMemo());
        tempRole.setType(EnumUtil.codeOf(TemplateRole.Type.class, templateRoleCreationVO.getType()));
        tempRole.setStatus(TemplateRole.Status.ENABLED);

        tempRole.setCreatedBy(WebUtils.getLoginName());
        tempRole.setCreatedTime(new Date(System.currentTimeMillis()));
        tempRole.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);
        createTempRole(tempRole);
        return tempRole;
    }

    public int createTempRole(TemplateRole tempRole) {
        int insertedRows;
        try {
            insertedRows = templateRoleMapper.create(tempRole);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return insertedRows;
    }

    public void updateTempRole(Long tempRoleSid, TemplateRoleUpdateVO tempRoleUpdateVO) {
        if (!WebUtils.isEmployee()) {
            throw new ServiceException("你不是" + Role.Category.EMPLOYEE.getName() + "，请立刻停止非法操作");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("roleSid: %d, tempRoleUpdateVO: %s, updatedBy: {loginName: %s, userType: %s, subType: %s}", tempRoleSid, tempRoleUpdateVO, WebUtils.getLoginName(), WebUtils.getUserType(), WebUtils.getSubType()));
        }
        TemplateRole tempRole = findBySid(tempRoleSid);
        if (Objects.isNull(tempRole)) {
            throw new ServiceException(String.format("ID为[%d]的模板角色不存在", tempRole));
        }
        TemplateRole example = new TemplateRole();
        example.setSid(tempRoleSid);
        example.setName(tempRoleUpdateVO.getName());
        example.setMemo(tempRoleUpdateVO.getMemo());
        example.setUpdatedBy(WebUtils.getLoginName());
        example.setUpdatedTime(new Date(System.currentTimeMillis()));
        example.setVersionNum(tempRole.getVersionNum());

        int updatedRows = updateTempRole(example);
        if (updatedRows == 0) {
            throw new ServiceException("此模板角色正在被其他人修改，请稍后再试");
        }
    }

    public int updateTempRole(TemplateRole tempRole) {
        int updatedRows;
        try {
            updatedRows = templateRoleMapper.updateByExample(tempRole);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return updatedRows;
    }

    @Transactional
    public void removeTempRole(Long sid) {
        if (!WebUtils.isEmployee()) {
            throw new ServiceException("你不是" + Role.Category.EMPLOYEE.getName() + "，请立刻停止非法操作");
        }

        TemplateRole tempRole = findBySid(sid);
        if (Objects.isNull(tempRole)) {
            throw new ServiceException(String.format("ID为[%d]的模板角色不存在", sid));
        }

        templateRolePermissionRelationshipService.deleteByTempRoleSid(tempRole.getSid());
        if (log.isInfoEnabled()) {
            log.info(String.format("menus and apis related to templateRole with id: [%d] has been deleted by {loginName: %s, userType: %s, subType: %s}", tempRole.getSid(), WebUtils.getLoginName(), WebUtils.getUserType(), WebUtils.getSubType()));
        }

        deleteBySid(tempRole.getSid());
        if (log.isInfoEnabled()) {
            log.info(String.format("TemplateRole %s has been deleted by {loginName: %s, userType: %s, subType: %s}", JsonUtil.toJson(tempRole), WebUtils.getLoginName(), WebUtils.getUserType(), WebUtils.getSubType()));
        }
    }

    public int deleteBySid(Long sid) {
        Map<String, Object> params = new HashMap<>();
        params.put("sid", sid);
        return deleteByParams(params);
    }

    public int deleteByParams(Map<String, Object> params) {
        int deletedRows;
        try {
            deletedRows = templateRoleMapper.deleteByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return deletedRows;
    }

    @Transactional
    public void addPermissionsToTempRole(Long tempRoleSid, List<Long> permissionSids) {
        if (!WebUtils.isEmployee()) {
            throw new ServiceException("你不是" + Role.Category.EMPLOYEE.getName() + "，请立刻停止非法操作");
        }
        if (Objects.isNull(permissionSids) || permissionSids.isEmpty()) {
            return;
        }
        TemplateRole tempRole = findBySid(tempRoleSid);
        if (Objects.isNull(tempRole)) {
            throw new ServiceException("ID为[" + tempRoleSid + "]的模板角色不存在");
        }
        addPermissionsToGivenTempRole(tempRoleSid, permissionSids);
    }

    public void addPermissionsToGivenTempRole(Long tempRoleSid, List<Long> permissionSids) {
        List<TemplateRolePermissionRelationship> originTempRolePermissionRelationships = templateRolePermissionRelationshipService.findByTempRoleSid(tempRoleSid);
        List<Long> originPermissionSids = originTempRolePermissionRelationships.stream()
                .map(originTempRolePermissionRelationship -> originTempRolePermissionRelationship.getPermissionSid())
                .collect(Collectors.toList());
        List<Long> remainPermissionSids = ListUtils.getIntersection(originPermissionSids, permissionSids);
        List<Long> removedPermissionSids = ListUtils.getDifference(originPermissionSids, remainPermissionSids);
        List<Long> addedPermissionSids = ListUtils.getDifference(permissionSids, remainPermissionSids);

        for (Long removedPermissionSid : removedPermissionSids) {
            int deletedRows = templateRolePermissionRelationshipService.deleteByTempRoleSidAndPermissionSid(tempRoleSid, removedPermissionSid);
            if (deletedRows == 0) {
                throw new ServiceException("其他人正在给该模板角色分配菜单或者API，请稍后再试");
            }
        }

        Date createdTime = new Date(System.currentTimeMillis());
        String createdBy = WebUtils.getLoginName();
        for (Long addedPermissionSid : addedPermissionSids) {
            TemplateRolePermissionRelationship tempRolePermissionRelationship = new TemplateRolePermissionRelationship();
            tempRolePermissionRelationship.setTemplateRoleSid(tempRoleSid);
            tempRolePermissionRelationship.setPermissionSid(addedPermissionSid);
            tempRolePermissionRelationship.setCreatedBy(createdBy);
            tempRolePermissionRelationship.setCreatedTime(createdTime);
            tempRolePermissionRelationship.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);
            try {
                templateRolePermissionRelationshipService.createTempRolePermissionRelationship(tempRolePermissionRelationship);
            } catch (Exception e) {
                throw new ServiceException("其他人正在给该模板角色分配菜单或者API，请稍后再试");
            }
        }
    }

    public List<TemplateRole> getAvailableTempRoles() {
        Map<String, Object> params = new HashMap<>();
        params.put("status", Role.Status.ENABLED.getCode());
        return findByParams(params);
    }

    private boolean codeExist(String code) {
        TemplateRole tempRole = findByCode(code);
        return Objects.nonNull(tempRole);
    }

    public TemplateRole findByCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        return findSingle("code", code);
    }

    public List<TemplateRole> findByType(TemplateRole.Type type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type.getCode());
        return findByParams(params);
    }

    public TemplateRole findBySid(Long sid) {
        if (Objects.isNull(sid)) {
            return null;
        }
        return findSingle("sid", sid);
    }

    private TemplateRole findSingle(String paramName, Object paramValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(paramName, paramValue);
        List<TemplateRole> matchedTempRoles = findByParams(params);
        if (Objects.nonNull(matchedTempRoles) && !matchedTempRoles.isEmpty()) {
            return matchedTempRoles.get(0);
        }
        return null;
    }

    public int countByParams(Map<String, Object> params) {
        int count;
        try {
            count = templateRoleMapper.countByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return count;
    }

    public List<TemplateRole> findByParams(Map<String, Object> params) {
        List<TemplateRole> matchedTempRoles;
        try {
            matchedTempRoles = templateRoleMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedTempRoles;
    }
}
