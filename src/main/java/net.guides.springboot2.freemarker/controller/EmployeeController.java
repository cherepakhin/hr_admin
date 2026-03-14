package net.guides.springboot2.freemarker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

	@GetMapping("/")
	public String listEmployees(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
    	Pageable pageable = PageRequest.of(page, size);
    	Page<Employee> employeePage = employeeRepository.findAll(pageable);
		model.addAttribute("employees", employeePage.getContent());
		model.addAttribute("currentPage", employeePage.getNumber());
		model.addAttribute("totalPages", employeePage.getTotalPages());
		model.addAttribute("totalElements", employeePage.getTotalElements());

		return "index";
	}

    @GetMapping("/employees/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "create_employee";
    }

    @PostMapping("/employees")
    public String createEmployee(@ModelAttribute Employee employee, Model model) {
        employeeRepository.save(employee);
        return "redirect:/";
    }

    @GetMapping("/employees/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Employee> optional = employeeRepository.findById(id);
		if(optional.isPresent()) {
			model.addAttribute("employee", optional.get());
			return "edit_employee";
		}
		throw new IllegalArgumentException("Employee not exist with id=" + id);
    }

    @PostMapping("/employees/update/{id}")
    public String updateEmployee(@PathVariable("id") Long id,
                                 @ModelAttribute Employee employee,
                                 Model model) {
        employee.setId(id);
        employeeRepository.save(employee);
        return "redirect:/";
    }

    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id, Model model) {
        employeeRepository.deleteById(id);
        return "redirect:/";
    }

	@GetMapping("/showEmployees")
	public String showAllEmployees(Model model,
								   @RequestParam(defaultValue = "0") int page,
								   @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Employee> employeePage = employeeRepository.findAll(pageable);

		model.addAttribute("employees", employeePage.getContent());
		model.addAttribute("currentPage", employeePage.getNumber());
		model.addAttribute("totalPages", employeePage.getTotalPages());
		model.addAttribute("totalElements", employeePage.getTotalElements());

		return "showEmployees";
	}
}
