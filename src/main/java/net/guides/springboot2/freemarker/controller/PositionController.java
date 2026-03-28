package net.guides.springboot2.freemarker.controller;

import jakarta.validation.Valid;
import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/positions")
public class PositionController {

    @Autowired
    private PositionRepository positionRepository;

    @GetMapping
    public String listPositions(Model model) {
        model.addAttribute("positions", positionRepository.findAll());
        return NamesView.POSITIONS;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("position", new Position());
        return NamesView.CREATE_POSITION;
    }

    @PostMapping
    public String createPosition(@Valid @ModelAttribute Position position,
                                 BindingResult result,
                                 Model model) {
        if (positionRepository.existsByName(position.getName())) {
            result.rejectValue("name", "error.position", "Должность с таким названием уже существует.");
        }
        if (result.hasErrors()) {
            return NamesView.CREATE_POSITION;
        }
        positionRepository.save(position);
        return "redirect:/" + NamesView.POSITIONS;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid position ID: " + id));
        model.addAttribute("position", position);
        return NamesView.EDIT_POSITION;
    }

    @PostMapping("/update/{id}")
    public String updatePosition(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute Position position,
                                 BindingResult result,
                                 Model model) {
        if (positionRepository.existsByNameAndIdNot(position.getName(), id)) {
            result.rejectValue("name", "error.position", "Должность с таким названием уже существует.");
        }
        if (result.hasErrors()) {
            position.setId(id);
            return NamesView.EDIT_POSITION;
        }
        position.setId(id);
        positionRepository.save(position);
        return "redirect:/" + NamesView.POSITIONS;
    }

    @GetMapping("/delete/{id}")
    public String deletePosition(@PathVariable("id") Long id) {
        positionRepository.deleteById(id);
        return "redirect:/" + NamesView.POSITIONS;
    }
}