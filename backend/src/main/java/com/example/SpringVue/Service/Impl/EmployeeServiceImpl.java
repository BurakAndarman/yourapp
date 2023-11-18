package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Dto.EmployeeDto;
import com.example.SpringVue.Dto.LoginDto;
import com.example.SpringVue.Entity.Employee;
import com.example.SpringVue.Repo.EmployeeRepo;
import com.example.SpringVue.Response.LoginResponse;
import com.example.SpringVue.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepo employeeRepo;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepo employeeRepo, PasswordEncoder passwordEncoder){
        this.employeeRepo = employeeRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String addEmployee(EmployeeDto employeeDto) {

        Employee employee = new Employee(
          employeeDto.getEmployeeId(),
          employeeDto.getEmployeeName(),
          employeeDto.getEmail(),
          this.passwordEncoder.encode(employeeDto.getPassword())
        );

        employeeRepo.save(employee);

        return employee.getEmployeeName();
    }

    @Override
    public LoginResponse loginEmployee(LoginDto loginDto) {
        Employee employee = employeeRepo.findByEmail(loginDto.getEmail());

        if(employee != null) {
            String password = loginDto.getPassword();
            String encodedPassword = employee.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);

            if(isPwdRight) {
                Optional<Employee> employeeFromDatabase = employeeRepo.findOneByEmailAndPassword(loginDto.getEmail(),encodedPassword);

                if(employeeFromDatabase.isPresent()) {
                    return new LoginResponse("Login Success",true);
                } else {
                    return new LoginResponse("Login Failed", false);
                }
            } else {

                return new LoginResponse("Password is not correct",false);

            }
        } else {
            return new LoginResponse("Email doesn't exist",false);
        }
    }
}
