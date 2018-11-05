package com.haihua.hhplms.ana.web.controller;

import com.haihua.hhplms.ana.vo.EmployeeUpdateVO;
import com.haihua.hhplms.ana.vo.EmployeeVO;
import com.haihua.hhplms.ana.vo.UpdateStatusRequest;
import com.haihua.hhplms.ana.entity.Employee;
import com.haihua.hhplms.ana.service.EmployeeService;
import com.haihua.hhplms.ana.vo.*;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.model.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    @Qualifier("employeeService")
    private EmployeeService employeeService;

    @GetMapping(path = "/api/ana/employees", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<PageWrapper<List<EmployeeVO>>> loadEmployees(@RequestParam(name = "realName", required = false) String realNameLike,
                                                                           @RequestParam(name = "mobile", required = false) String mobileLike,
                                                                           @RequestParam(name = "gender", required = false) String gender,
                                                                           @RequestParam(name = "status", required = false) String status,
                                                                           @RequestParam(name = "pageNo") Integer pageNo,
                                                                           @RequestParam(name = "pageSize") Integer pageSize) {
        PageWrapper<List<EmployeeVO>> pageOfEmployees = employeeService.loadEmployeesByPage(realNameLike, mobileLike, gender, status, pageNo, pageSize);
        return ResultBean.Success.of(pageOfEmployees, "");
    }

    @GetMapping(path = "/api/ana/employees/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<EmployeeVO> loadDetail(@PathVariable("id") Long sid) {
        EmployeeVO employeeDetail = employeeService.loadDetail(sid);
        return ResultBean.Success.of(employeeDetail, "");
    }

    @PostMapping(path = "/api/ana/employees", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> createEmployee(@RequestBody EmployeeCreationVO employeeCreationVO) {
        Employee createdEmployee = employeeService.createEmployee(employeeCreationVO);
        return ResultBean.Success.of(createdEmployee.getSid(), "");
    }

    @PutMapping(path = "/api/ana/employees/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> updateEmployee(@PathVariable("id") Long sid,
                                                  @RequestBody EmployeeUpdateVO employeeUpdateVO) {
        employeeService.updateEmployee(sid, employeeUpdateVO);
        return ResultBean.Success.of(sid, "");
    }

    @PostMapping(path = "/api/ana/employees/{id}/roles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> addRolesToEmployee(@PathVariable("id") Long employeeSid,
                                                       @RequestBody List<Long> roleSids) {
        employeeService.addRolesToGivenEmployee(employeeSid, roleSids);
        return ResultBean.Success.of(employeeSid, "");
    }

    @PatchMapping(path = "/api/ana/employees/{id}/resetPwd", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> resetPwd(@PathVariable("id") Long employeeSid) {
        employeeService.resetPassword(employeeSid);
        return ResultBean.Success.of(employeeSid, "");
    }

    @PatchMapping(path = "/api/ana/employees/changePwd", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<String> changePwd(@RequestBody ChangePasswordRequest changePasswordRequest) {
        employeeService.changePassword(changePasswordRequest);
        return ResultBean.Success.of("", "");
    }

    @PatchMapping(path = "/api/ana/employees/{id}/updateStatus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> updateAccountStatus(@PathVariable("id") Long employeeSid,
                                                        @RequestBody UpdateStatusRequest updateStatusRequest) {
        employeeService.updateEmployeeStatus(employeeSid, updateStatusRequest);
        return ResultBean.Success.of(employeeSid, "");
    }
}
