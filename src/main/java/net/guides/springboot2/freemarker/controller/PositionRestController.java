package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/positions/")
public class PositionRestController {
	private static final Logger log = LoggerFactory.getLogger(PositionController.class);

	private final PositionRepository positionRepository;
	private final EmployeeRepository employeeRepository;

	@Autowired
	public PositionRestController(PositionRepository positionRepository, EmployeeRepository employeeRepository) {
		this.positionRepository = positionRepository;
		this.employeeRepository=employeeRepository;
	}

	@GetMapping("/can_delete/{id}")
	public String canDeletePosition(@PathVariable String id, Model model) {
		log.info("Verify delete position id: {}", id);
		//List<Employee> employees = employeeRepository.findAllByPosition(id);
		//TODO: "false" всегда
		return "{\"deleteable\": true}";
	}
}
