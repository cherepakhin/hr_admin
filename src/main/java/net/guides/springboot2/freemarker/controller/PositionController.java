package net.guides.springboot2.freemarker.controller;

import jakarta.validation.Valid;
import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/positions")
public class PositionController {

	private static final Logger log = LoggerFactory.getLogger(PositionController.class);

	private final PositionRepository positionRepository;

	// Используем внедрение через конструктор (рекомендуется)
	@Autowired
	public PositionController(PositionRepository positionRepository) {
		this.positionRepository = positionRepository;
	}

	// GET /positions/ - отображение списка
	@GetMapping("/")
	public ModelAndView listPositions(Model model) {
		log.info("get all positions");
		List<Position> positions = positionRepository.findAll();
		positions.forEach(p-> log.info(p.toString()));
		ModelAndView mv= new ModelAndView();
		mv.setViewName(NamesView.POSITIONS);
		mv.addObject("positions", positions);
		//model.addAttribute("positions", positions);
		//model.addAttribute("v", UUID.randomUUID().toString());
		// Вместо простого редиректа, добавляем уникальный параметр
		// UUID.randomUUID().toString() создаст случайную строку типа "a1b2-c3d4..."
		log.info("Redirect to view");
		return mv;
//		return NamesView.POSITIONS;
		//return NamesView.POSITIONS  + "/?v==" + UUID.randomUUID().toString();
	}

	// GET /positions/new - форма создания
	@GetMapping("/new")
	public String showCreateForm(Model model) {
		model.addAttribute("position", new Position());
		return NamesView.CREATE_POSITION;
	}

	// POST /positions/ - обработка создания
	@PostMapping("/")
	public String createPosition(@Valid @ModelAttribute Position position,
								 BindingResult result) {
		if (positionRepository.existsByName(position.getName())) {
			result.rejectValue("name", "error.position", "Должность с таким названием уже существует.");
		}
		if (result.hasErrors()) {
			return NamesView.CREATE_POSITION;
		}
		position.setId(positionRepository.getNextId());
		positionRepository.save(position);
		return "redirect:/" + NamesView.POSITIONS + "/";
	}

	// GET /positions/edit/{id} - форма редактирования
	@GetMapping("/edit/{id}")
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
								 BindingResult result) {
		if (positionRepository.existsByNameAndIdNot(position.getName(), id)) {
			result.rejectValue("name", "error.position", "Должность с таким названием уже существует.");
		}
		if (result.hasErrors()) {
			position.setId(id); // Восстанавливаем id для формы
			return NamesView.EDIT_POSITION;
		}
		position.setId(id);
		positionRepository.save(position);
		return "redirect:/" + NamesView.POSITIONS + "/";
	}

	// GET /positions/delete/{id} - удаление
	@DeleteMapping("/delete/{id}")
	public ModelAndView deletePosition(@PathVariable Long id, Model model) {
		log.info("Delete position id: {}", id);
		ModelAndView mv = new ModelAndView(NamesView.POSITIONS);
		mv.clear();

		// TODO: Проверить, используется ли позиция где-то ещё (например, у сотрудников)

		positionRepository.deleteById(id);
		// Вместо простого редиректа, добавляем уникальный параметр
		// UUID.randomUUID().toString() создаст случайную строку типа "a1b2-c3d4..."
		//String redirectUrl = "/" + NamesView.POSITIONS + "/?v==" + UUID.randomUUID().toString();
		//return redirectUrl;
		//mv.addreturn listPositions(model);
		mv.setViewName("redirect:/" +NamesView.POSITIONS +"/");
		return mv;

//		return "redirect:" + redirectUrl;
	}
}