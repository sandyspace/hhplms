package com.haihua.hhplms.ana.mapper;

import com.haihua.hhplms.ana.entity.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeMapper {
    int countByParams(Map<String, Object> params);
    List<Employee> findByParams(Map<String, Object> params);
    int create(Employee employee);
    int updateByParams(Map<String, Object> params);
    int updateByExample(Employee employee);
}
