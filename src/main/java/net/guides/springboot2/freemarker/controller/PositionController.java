package net.guides.springboot2.freemarker.controller;

import jakarta.validation.Valid;
import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//TODO: добавить валидацию
@Controller
@RequestMapping("/positions")
public class PositionController {

	private static final Logger log = LoggerFactory.getLogger(PositionController.class);

	private final PositionRepository positionRepository;
	private final EmployeeRepository employeeRepository;

	// Используем внедрение через конструктор (рекомендуется)
	@Autowired
	public PositionController(PositionRepository positionRepository, EmployeeRepository employeeRepository) {
		this.positionRepository = positionRepository;
		this.employeeRepository=employeeRepository;
	}

	// GET /positions/ - отображение списка
	@RequestMapping("/")
	public String listPositions(Model model,
								@RequestParam(defaultValue = "name") String sortField,
								@RequestParam(defaultValue = "asc") String direction) {
		log.info("get all positions");
		log.info("sortField {}", sortField);
		log.info("direction {}", direction);
//		List<Position> positions = positionRepository.findAll();
		Sort sort;
		if(direction.equals("desc")) {
			sort = Sort.by(sortField).descending();
		} else {
			sort = Sort.by(sortField).ascending();
		}

		List<Position> positions = positionRepository.findByAndSort(sort);
		if(positions == null || positions.isEmpty()) {
			positions = new ArrayList<>();
		}
		positions.forEach(p-> log.info(p.toString()));

		//ModelAndView mv = new ModelAndView();
		//mv.setViewName(NamesView.POSITIONS);
		model.addAttribute("positions", positions);
		model.addAttribute("sortField", sortField);
		model.addAttribute("direction", direction);
		return "show_positions";
		// return mv;
	}

	// GET /positions/new - форма создания
	@RequestMapping("/new")
	public String showCreateForm(Model model) {
		Position position = new Position();
		position.setName(""); // значение по умолчанию
		model.addAttribute("position", position);
		return NamesView.CREATE_POSITION;
	}

	// POST /positions/ - обработка создания
	@PostMapping("/")
	public String createPosition(@Valid @ModelAttribute Position position,
								 BindingResult bindingResult,  Model model) {
		log.info("Create position: {}", position);
		if (bindingResult.hasErrors()) { // в bindingResult результаты валидации
			log.info("Binding result: {}", bindingResult);
			StringBuilder errors = new StringBuilder();
			for(ObjectError error :  bindingResult.getAllErrors()) {
				errors.append(error.getDefaultMessage()).append("\n");
				log.error(error.getDefaultMessage());
			}
			// find in create_position.ftlh by name="name" (<input ... name="name")
			model.addAttribute("name", position.getName());
			model.addAttribute("error", errors.toString()); // <p class="text-red-600 text-xs mt-1">${error}</p>
			return NamesView.CREATE_POSITION;
		}
		if (positionRepository.existsByName(position.getName())) {
			log.error(String.format("Должность с названием '%s' уже существует.", position.getName()));
			model.addAttribute("position", position);
			model.addAttribute("error", "Должность с таким названием уже существует.");
			return NamesView.CREATE_POSITION;
		}
		position.setId(positionRepository.getNextId());
		positionRepository.save(position);
		return "redirect:/positions/";
	}

	// GET /positions/edit/{id} - вызов формы для редактирования
	@RequestMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		Position position = positionRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid position ID: " + id));
		model.addAttribute("position", position);
		return NamesView.EDIT_POSITION;
	}

	// POST /positions/update/{id} - обработка обновления
	@PostMapping("/update/{id}")
	public String updatePosition(@PathVariable Long id,
								 @Valid @ModelAttribute Position position,
								 BindingResult bindingResult, Model model) {
		// Обработка ошибок @Valid
		if (bindingResult.hasErrors()) {
			log.info("Binding result: {}", bindingResult);
			String errors = "";
			for (ObjectError error : bindingResult.getAllErrors()) {
				errors += error.getDefaultMessage() + "\n";
				log.error(error.getDefaultMessage());
			}
			model.addAttribute("name", position.getName());
			model.addAttribute("error", errors);
			position.setId(id); // Восстановление id для формы
			return NamesView.EDIT_POSITION;
		}

		if (!positionRepository.existsByName(position.getName())) {
			bindingResult.rejectValue("name", "error.position", "Должность с таким ID и названием НЕ существует.");
			position.setId(id); // Восстанавливаю id для формы
			return NamesView.EDIT_POSITION;
		}
		if (bindingResult.hasErrors()) {
			position.setId(id); // Восстановление id для формы
			return NamesView.EDIT_POSITION;
		}
		position.setId(id);
		positionRepository.save(position);
		return "redirect:/" + NamesView.POSITIONS + "/";
	}

	// GET /positions/delete/{id} - удаление

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deletePosition(@PathVariable Long id) {
		// Проверка на возможность удаления уже сделана в форме и в PositionRestController и здесь не делается
		// через const response = await fetch("${springMacroRequestContext.contextPath}/rest/positions/can_delete/" + id);
		// PositionRestController это REST контроллер, выделен в отдельный класс, т.к. он не связан со Spring MVC
		positionRepository.sqlDeleteById(id);

		// Вместо простого редиректа, добавляем уникальный параметр для отключения кеша браузера
		// Здесь не используется, т.к. сделано через "redirect", но оставил как напоминание
		// UUID.randomUUID().toString() создаст случайную строку типа "a1b2-c3d4..."
		//String redirectUrl = "/" + NamesView.POSITIONS + "/?v==" + UUID.randomUUID().toString();
		//return redirectUrl;
		//mv.addreturn listPositions(model);
		//mv.setViewName("redirect:/" +NamesView.POSITIONS +"/");
		log.info("get all positions");
//		List<Position> positions = positionRepository.findAll();
//		positions.forEach(p-> log.info(p.toString()));
		// ModelAndView mv = new ModelAndView();
//		model.addAttribute("positions", positions);
		//mv.setViewName(NamesView.POSITIONS);
		//mv.addObject("positions", positions);
		//return NamesView.POSITIONS;
		 return "redirect:/positions/";
//		return NamesView.POSITIONS;
		//return mv;

//		return "redirect:" + redirectUrl;
	}
}