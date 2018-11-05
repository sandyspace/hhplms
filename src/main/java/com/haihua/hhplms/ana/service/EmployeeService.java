package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.vo.EmployeeUpdateVO;
import com.haihua.hhplms.ana.vo.EmployeeVO;
import com.haihua.hhplms.ana.vo.UpdateStatusRequest;
import com.haihua.hhplms.ana.entity.Employee;
import com.haihua.hhplms.ana.vo.*;
import com.haihua.hhplms.common.model.PageWrapper;

import java.util.List;
import java.util.Map;

public interface EmployeeService {
    PageWrapper<List<EmployeeVO>> loadEmployeesByPage(String realNameLike,
                                                      String mobileLike,
                                                      String gender,
                                                      String status,
                                                      Integer pageNo,
                                                      Integer pageSize);
    EmployeeVO loadDetail(Long sid);
    Employee createEmployee(EmployeeCreationVO employeeCreationVO);
    void updateEmployee(Long employeeSid, EmployeeUpdateVO employeeUpdateVO);
    void resetPassword(Long employeeSid);
    void changePassword(ChangePasswordRequest changePasswordRequest);
    void updateEmployeeStatus(Long employeeSid, UpdateStatusRequest updateStatusRequest);
    void addRolesToGivenEmployee(Long employeeSid, List<Long> roleSids);
    Employee findByLoginName(String loginName);
    Employee findByMobile(String mobile);
    Employee findByEmail(String email);
    List<Employee> findByParams(Map<String, Object> params);
}
