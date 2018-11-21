package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.vo.*;
import com.haihua.hhplms.ana.entity.*;
import com.haihua.hhplms.ana.mapper.EmployeeMapper;
import com.haihua.hhplms.ana.vo.*;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.utils.EnumUtil;
import com.haihua.hhplms.common.utils.ListUtils;
import com.haihua.hhplms.common.utils.WebUtils;
import com.haihua.hhplms.security.auth.ajax.AjaxAuthenticationService;
import com.haihua.hhplms.security.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService, AjaxAuthenticationService {
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    @Qualifier("roleService")
    private RoleService roleService;

    @Autowired
    @Qualifier("permissionService")
    private PermissionService permissionService;

    @Autowired
    @Qualifier("employeeRoleRelationshipService")
    private EmployeeRoleRelationshipService employeeRoleRelationshipService;

    public PageWrapper<List<EmployeeVO>> loadEmployeesByPage(String realNameLike,
                                                             String mobileLike,
                                                             String gender,
                                                             String status,
                                                             Integer pageNo,
                                                             Integer pageSize) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            throw new ServiceException("You have insufficient right to view employees");
        }
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isBlank(realNameLike)) {
            params.put("realNameLike", realNameLike);
        }
        if (!StringUtils.isBlank(mobileLike)) {
            params.put("mobileLike", mobileLike);
        }
        if (!StringUtils.isBlank(gender)) {
            params.put("gender", gender);
        }
        if (!StringUtils.isBlank(status)) {
            params.put("status", status);
        }
        return loadEmployeesByPage(params, pageNo, pageSize);
    }

    public PageWrapper<List<EmployeeVO>> loadEmployeesByPage(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        int count = countByParams(params);
        PageWrapper<List<EmployeeVO>> pageOfEmployees = new PageWrapper<>(pageNo, pageSize, count);
        if (count > 0) {
            pageNo = Objects.isNull(pageNo) ? GlobalConstant.DEFAULT_PAGE_NO : pageNo;
            pageSize = Objects.isNull(pageSize) ? GlobalConstant.DEFAULT_PAGE_SIZE : pageSize;
            params.put("offset", (pageNo - 1) * pageSize);
            params.put("limit", pageNo * pageSize);
            List<Employee> matchedEmployees = findByParams(params);
            if (Objects.nonNull(matchedEmployees) && !matchedEmployees.isEmpty()) {
                pageOfEmployees.setResult(matchedEmployees.stream()
                        .map(matchedEmployee -> new EmployeeVO(matchedEmployee))
                        .collect(Collectors.toList()));
            }
        }
        return pageOfEmployees;
    }

    public EmployeeVO loadDetail(Long sid) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            throw new ServiceException("You have insufficient right to view the detail of this employee");
        }
        Employee matchedEmployee = findBySid(sid);
        if (Objects.isNull(matchedEmployee)) {
            throw new ServiceException("Employee with id: [" + sid + "] does not exist");
        }
        EmployeeVO employDetail = new EmployeeVO(matchedEmployee);

        List<Role> matchedRoles = roleService.findRolesOfGivenEmployee(sid);
        if (Objects.nonNull(matchedRoles) && !matchedRoles.isEmpty()) {
            List<Long> assignedRoleSids = new ArrayList<>(matchedRoles.size());
            List<RoleVO> assignedRoles = new ArrayList<>(matchedRoles.size());
            for (Role matchedRole : matchedRoles) {
                assignedRoleSids.add(matchedRole.getSid());
                assignedRoles.add(new RoleVO(matchedRole));
            }

            employDetail.setGrantedRoles(assignedRoles);
            List<Permission> assignedApiList = permissionService.findPermissionsOfGivenRoles(assignedRoleSids, Permission.Type.API);
            if (Objects.nonNull(assignedApiList) && !assignedApiList.isEmpty()) {
                employDetail.setGrantedApiList(assignedApiList.stream()
                        .map(assignedApi -> new PermissionVO(assignedApi))
                        .collect(Collectors.toList()));
            }

            List<Permission> assignedPermissions = permissionService.findPermissionsOfGivenRoles(assignedRoleSids, Permission.Type.PAGE);
            final Map<Long, PermissionVO> grantedPermissionMap = new HashMap<>();
            assignedPermissions.forEach(assignedPermission -> grantedPermissionMap.put(assignedPermission.getSid(), new PermissionVO(assignedPermission)));
            grantedPermissionMap.values().forEach(grantedPermission -> {
                if (Objects.nonNull(grantedPermission.getPid())) {
                    grantedPermissionMap.get(grantedPermission.getPid()).addSubPermission(grantedPermission);
                }
            });
            employDetail.setGrantedPermissions(grantedPermissionMap.values().stream()
                    .filter(grantedPermission -> Permission.LEVEL_TOP == grantedPermission.getLevel())
                    .collect(Collectors.toList()));
        }

        return employDetail;
    }

    public Employee createEmployee(EmployeeCreationVO employeeCreationVO) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            throw new ServiceException("You have insufficient right to create employee");
        }

        Employee employee = new Employee();
        employee.setLoginName(employeeCreationVO.getLoginName());
        employee.setRealName(employeeCreationVO.getRealName());
        employee.setPassword(encoder.encode(employeeCreationVO.getMobile()));
        employee.setEmail(employeeCreationVO.getEmail());
        employee.setMobile(employeeCreationVO.getMobile());
        employee.setTel(employeeCreationVO.getTel());
        employee.setGender(EnumUtil.codeOf(Gender.class, employeeCreationVO.getGender()));
        employee.setIdCard(employeeCreationVO.getIdCard());
        employee.setTitle(employeeCreationVO.getTitle());
        employee.setStatus(EnumUtil.codeOf(Employee.Status.class, employeeCreationVO.getStatus()));

        employee.setCreatedBy(WebUtils.getLoginName());
        employee.setCreatedTime(new Date(System.currentTimeMillis()));
        employee.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);

        createEmployee(employee);
        return employee;
    }

    public int createEmployee(Employee employee) {
        int insertedRows;
        try {
            insertedRows = employeeMapper.create(employee);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return insertedRows;
    }

    public void updateEmployee(Long employeeSid, EmployeeUpdateVO employeeUpdateVO) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            throw new ServiceException("You have insufficient right to update employee");
        }

        Employee editingEmployee = findBySid(employeeSid);
        if (Objects.isNull(editingEmployee)) {
            throw new ServiceException("Employee with id: [" + employeeSid + "] does not exist");
        }

        Employee employee = new Employee();
        employee.setSid(employeeSid);
        employee.setLoginName(employeeUpdateVO.getLoginName());
        employee.setRealName(employeeUpdateVO.getRealName());
        employee.setEmail(employeeUpdateVO.getEmail());
        employee.setMobile(employeeUpdateVO.getMobile());
        employee.setTel(employeeUpdateVO.getTel());
        employee.setGender(EnumUtil.codeOf(Gender.class, employeeUpdateVO.getGender()));
        employee.setIdCard(employeeUpdateVO.getIdCard());
        employee.setTitle(employeeUpdateVO.getTitle());

        employee.setUpdatedBy(WebUtils.getLoginName());
        employee.setUpdatedTime(new Date(System.currentTimeMillis()));
        employee.setVersionNum(editingEmployee.getVersionNum());

        int updatedRows = updateEmployee(employee);
        if (updatedRows == 0) {
            throw new ServiceException("Employee is being edited by others, please try again later");
        }
    }

    public int updateEmployee(Employee employee) {
        int updatedRows;
        try {
            updatedRows = employeeMapper.updateByExample(employee);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return updatedRows;
    }

    public int updateEmployee(Map<String, Object> params) {
        int updatedRows;
        try {
            updatedRows = employeeMapper.updateByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return updatedRows;
    }

    public void resetPassword(Long employeeSid) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            throw new ServiceException("You have insufficient right to reset password of this employee");
        }

        Employee employee = findBySid(employeeSid);
        if (Objects.isNull(employee)) {
            throw new ServiceException("Employee does not exist, failed to reset password");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("password", encoder.encode(employee.getMobile()));
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", employee.getVersionNum());
        params.put("sid", employee.getSid());

        int updatedRows = updateEmployee(params);
        if (updatedRows == 0) {
            throw new ServiceException("Employee is being edited by others, please try again later");
        }
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        Long employeeId = WebUtils.getUserId();
        Employee employee = findBySid(employeeId);
        if (Objects.isNull(employee)) {
            throw new ServiceException("employee does not exist, failed to change password");
        }

        if (StringUtils.isBlank(changePasswordRequest.getOriginPwd())) {
            throw new ServiceException("origin password can not be blank");
        }

        if (!encoder.matches(changePasswordRequest.getOriginPwd(), employee.getPassword())) {
            throw new ServiceException("origin password is not correct");
        }

        if (StringUtils.isBlank(changePasswordRequest.getNewPwd())) {
            throw new ServiceException("new password can not be blank");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("password", encoder.encode(changePasswordRequest.getNewPwd()));
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", employee.getVersionNum());
        params.put("sid", employee.getSid());

        int updatedRows = updateEmployee(params);
        if (updatedRows == 0) {
            throw new ServiceException("Employee is being edited by others, please try again later");
        }
    }

    public void updateEmployeeStatus(Long employeeSid, UpdateStatusRequest updateStatusRequest) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            throw new ServiceException("You have insufficient right to change status of this employee");
        }

        Employee employee = findBySid(employeeSid);
        if (Objects.isNull(employee)) {
            throw new ServiceException("Employee does not exist, failed to change status");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("status", updateStatusRequest.getStatus());
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", employee.getVersionNum());
        params.put("sid", employee.getSid());

        int updatedRows = updateEmployee(params);
        if (updatedRows == 0) {
            throw new ServiceException("Employee is being edited by others, please try again later");
        }
    }

    public void addRolesToGivenEmployee(Long employeeSid, List<Long> roleSids) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            throw new ServiceException("You have insufficient right to add roles to employee");
        }
        List<Role> rolesToAdd = roleService.findBySids(roleSids);
        if (Objects.isNull(rolesToAdd) || rolesToAdd.isEmpty() || rolesToAdd.size() != roleSids.size()) {
            throw new ServiceException("some roles to be added to this account do not exist");
        }
        List<EmployeeRoleRelationship> originEmployeeRoleRelationships = employeeRoleRelationshipService.findByEmployeeSid(employeeSid);
        List<Long> originRoleSids = originEmployeeRoleRelationships.stream()
                .map(originEmployeeRoleRelationship -> originEmployeeRoleRelationship.getRoleSid())
                .collect(Collectors.toList());

        List<Long> remainRoleSids = ListUtils.getIntersection(originRoleSids, roleSids);
        List<Long> removedRoleSids = ListUtils.getDifference(originRoleSids, remainRoleSids);
        List<Long> addedRoleSids = ListUtils.getDifference(roleSids, remainRoleSids);

        for(Long removedRoleSid : removedRoleSids) {
            int deletedRows = employeeRoleRelationshipService.deleteByEmployeeSidAndRoleSid(employeeSid, removedRoleSid);
            if (deletedRows == 0) {
                throw new ServiceException("Employee is being edited by others, please try again later");
            }
        }

        Date createdTime = new Date(System.currentTimeMillis());
        String createdBy = WebUtils.getLoginName();
        for(Long addedRoleSid : addedRoleSids) {
            EmployeeRoleRelationship employeeRoleRelationship = new EmployeeRoleRelationship();
            employeeRoleRelationship.setEmployeeSid(employeeSid);
            employeeRoleRelationship.setRoleSid(addedRoleSid);
            employeeRoleRelationship.setCreatedBy(createdBy);
            employeeRoleRelationship.setCreatedTime(createdTime);
            employeeRoleRelationship.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);
            try {
                employeeRoleRelationshipService.createEmployeeRoleRelationship(employeeRoleRelationship);
            } catch (ServiceException e) {
                throw new ServiceException("Employee is being edited by others, please try again later", e);
            }
        }
    }

    public Employee findBySid(Long sid) {
        return findSingle("sid", sid);
    }

    public Employee findByLoginName(String loginName) {
        return findSingle("loginName", loginName);
    }

    public Employee findByMobile(String mobile) {
        return findSingle("mobile", mobile);
    }

    public Employee findByEmail(String email) {
        return findSingle("email", email);
    }

    private Employee findSingle(String paramName, Object paramValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(paramName, paramValue);
        List<Employee> matchedEmployees = findByParams(params);
        if (Objects.nonNull(matchedEmployees) && !matchedEmployees.isEmpty()) {
            return matchedEmployees.get(0);
        }
        return null;
    }

    public int countByParams(Map<String, Object> params) {
        int count;
        try {
            count = employeeMapper.countByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return count;
    }

    public List<Employee> findByParams(Map<String, Object> params) {
        List<Employee> matchedEmployees;
        try {
            matchedEmployees = employeeMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedEmployees;
    }


    public UserBasicInfo loadUserBasicInfoByUserName(String username) {
        Employee employee = findByLoginName(username);
        if (Objects.isNull(employee)) {
            employee = findByMobile(username);
        }
        if (Objects.isNull(employee)) {
            employee = findByEmail(username);
        }
        if (Objects.isNull(employee)) {
            throw new UsernameNotFoundException("Username[" + username + "] does not exist");
        }
        if (Employee.Status.ACTIVE != employee.getStatus()) {
            throw new DisabledException("Authentication Failed. Your account was in " + employee.getStatus().getCode() + " status");
        }
        return new UserBasicInfo.Builder()
                .id(employee.getSid())
                .loginName(employee.getLoginName())
                .mobile(employee.getMobile())
                .email(employee.getEmail())
                .type(Role.Category.EMPLOYEE.getCode())
                .password(employee.getPassword())
                .status(employee.getStatus().getCode())
                .realName(employee.getRealName())
                .title(employee.getTitle())
                .gender(employee.getGender().getCode())
                .headImgUrl(employee.getHeadImgUrl())
                .companyId(-1L)
                .build();
    }

    public List<GrantedPermission> loadGrantedApiListByGrantedRoles(List<GrantedRole> grantedRoles) {
        final List<Permission> matchedApiList = permissionService.findPermissionsOfGivenRoles(grantedRoles.stream()
                .map(role -> role.getId())
                .collect(Collectors.toList()), Permission.Type.API);

        if (Objects.nonNull(matchedApiList) && !matchedApiList.isEmpty()) {

            List<GrantedPermission> grantedApiList = matchedApiList.stream()
                    .filter(matchedApi -> Permission.Status.ENABLED == matchedApi.getStatus())
                    .map(matchedApi -> new GrantedPermission.Builder()
                            .id(matchedApi.getSid())
                            .name(matchedApi.getName())
                            .title(matchedApi.getTitle())
                            .path(matchedApi.getPath())
                            .build())
                    .collect(Collectors.toList());

            return grantedApiList;
        }
        return null;
    }

    public List<GrantedPermission> loadGrantedPermissionsByGrantedRoles(List<GrantedRole> grantedRoles) {
        final List<Permission> matchedPermissions = permissionService.findPermissionsOfGivenRoles(grantedRoles.stream()
                .map(role -> role.getId())
                .collect(Collectors.toList()), Permission.Type.PAGE);

        if (Objects.nonNull(matchedPermissions) && !matchedPermissions.isEmpty()) {
            final Map<Long, GrantedPermission> grantedPermissionMap = new HashMap<>();
            matchedPermissions.forEach(matchedPermission -> {
                if (Permission.Status.ENABLED == matchedPermission.getStatus()) {
                    List<Role> associatedRoles = roleService.findRolesOfGivenPermission(matchedPermission.getSid());
                    grantedPermissionMap.put(matchedPermission.getSid(), new GrantedPermission.Builder()
                            .id(matchedPermission.getSid())
                            .path(matchedPermission.getPath())
                            .componentUrl(matchedPermission.getComponentUrl())
                            .noCacheFlag(matchedPermission.getNoCacheFlag())
                            .hiddenFlag(matchedPermission.getHiddenFlag())
                            .alwaysShowFlag(matchedPermission.getAlwaysShowFlag())
                            .redirectPath(matchedPermission.getRedirectPath())
                            .name(matchedPermission.getName())
                            .title(matchedPermission.getTitle())
                            .icon(matchedPermission.getIcon())
                            .level(matchedPermission.getLevel())
                            .pid(matchedPermission.getParentSid())
                            .roles(Objects.isNull(associatedRoles) ? null : associatedRoles.stream()
                                    .map(associatedRole -> new GrantedRole.Builder()
                                            .id(associatedRole.getSid())
                                            .code(associatedRole.getCode())
                                            .name(associatedRole.getName())
                                            .build())
                                    .collect(Collectors.toList()))
                            .build());
                }
            });
            grantedPermissionMap.values().forEach(grantedPermission -> {
                if (Objects.nonNull(grantedPermission.getPid())) {
                    grantedPermissionMap.get(grantedPermission.getPid()).addSubPermission(grantedPermission);
                }
            });
            return grantedPermissionMap.values().stream()
                    .filter(grantedPermission -> Permission.LEVEL_TOP == grantedPermission.getLevel())
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<GrantedRole> loadGrantedRolesByUserBasicInfo(UserBasicInfo userBasicInfo) {
        final List<Role> roles = roleService.findRolesOfGivenEmployee(userBasicInfo.getId());
        if (Objects.isNull(roles) || roles.isEmpty()) {
            throw new InsufficientAuthenticationException("User has no roles assigned");
        }
        return roles.stream()
                .filter(role -> Role.Status.ENABLED == role.getStatus())
                .map(role -> new GrantedRole.Builder()
                        .id(role.getSid())
                        .code(role.getCode())
                        .name(role.getName()).build())
                .collect(Collectors.toList());
    }

    public UserContext loadUserContextByUsername(String username) {
        UserBasicInfo userBasicInfo = loadUserBasicInfoByUserName(username);
        List<GrantedRole> grantedRoles = loadGrantedRolesByUserBasicInfo(userBasicInfo);
        List<GrantedPermission> grantedApiList = loadGrantedApiListByGrantedRoles(grantedRoles);
        List<GrantedPermission> grantedPermissions = loadGrantedPermissionsByGrantedRoles(grantedRoles);
        UserProfile userProfile = new UserProfile.Builder()
                .basicInfo(userBasicInfo)
                .roles(grantedRoles)
                .permissions(grantedPermissions)
                .grantedApiList(grantedApiList)
                .build();
        return new UserContext(username, userProfile);
    }

    public UserBasicInfo login(String username, String password) {
        UserBasicInfo userBasicInfo = loadUserBasicInfoByUserName(username);
        if (!encoder.matches(password, userBasicInfo.getPassword())) {
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }
        return userBasicInfo;
    }
}
