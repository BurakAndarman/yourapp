package com.example.SpringVue.Controller;

import com.example.SpringVue.Dto.EmployeeDto;
import com.example.SpringVue.Dto.LoginDto;
import com.example.SpringVue.Response.LoginResponse;
import com.example.SpringVue.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;

    }

    @PostMapping("/save")
    public String saveEmployee(@RequestBody EmployeeDto employeeDto) {

        String employeeName = employeeService.addEmployee(employeeDto);
        return employeeName;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginEmployee(@RequestBody LoginDto loginDto) {

        LoginResponse loginResponse = employeeService.loginEmployee(loginDto);

        return ResponseEntity.ok(loginResponse);
    }

}