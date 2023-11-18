package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.EmployeeDto;
import com.example.SpringVue.Dto.LoginDto;
import com.example.SpringVue.Response.LoginResponse;

public interface EmployeeService {

    String addEmployee(EmployeeDto employeeDto);

    LoginResponse loginEmployee(LoginDto loginDto);
}
