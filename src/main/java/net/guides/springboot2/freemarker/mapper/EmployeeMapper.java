package net.guides.springboot2.freemarker.mapper;

import net.guides.springboot2.freemarker.dto.EmployeeDto;
import net.guides.springboot2.freemarker.model.Employee;

public class EmployeeMapper implements IMapper<EmployeeDto, Employee> {
    PositionMapper positionMapper = new PositionMapper();

    @Override
    public EmployeeDto toDTO(Employee employee) {
        if (employee == null) return null;
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setPosition(positionMapper.toDTO(employee.getPosition()));
        return employeeDto;
    }

    @Override
    public Employee toEntity(EmployeeDto employeeDto) {
        if (employeeDto == null) return null;
        Employee employee = new Employee();
        employee.setId(employeeDto.getId());
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());
        employee.setPosition(positionMapper.toEntity(employeeDto.getPosition()));
        return employee;
    }
}
