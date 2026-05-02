package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PositionController.class)
public class PositionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PositionRepository positionRepository;

	@MockBean
	private EmployeeRepository employeeRepository;

	@Test
	public void shouldListPositions() throws Exception {
		// Given
		Position dev = new Position(1L, "Developer");
		given(positionRepository.findAll()).willReturn(Collections.singletonList(dev));

		// When & Then
		mockMvc.perform(get("/positions/"))
				.andExpect(status().isOk())
				.andExpect(view().name("show_positions"))
				.andExpect(model().attributeExists("positions"))
				.andExpect(model().attribute("positions", org.hamcrest.Matchers.hasSize(1)))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Developer")));
	}

	@Test
	public void shouldShowCreateForm() throws Exception {
		// When & Then
		mockMvc.perform(get("/positions/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("create_position"))
				.andExpect(model().attributeExists("position"));
	}

	@Test
	public void shouldCreatePositionAndRedirect() throws Exception {
		String NAME_POSITION = "Manager";
		when(positionRepository.existsByName(NAME_POSITION)).thenReturn(Boolean.FALSE);

		mockMvc.perform(post("/positions/")
						.param("name", NAME_POSITION))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/positions/"));

		verify(positionRepository).save(argThat(p -> NAME_POSITION.equals(p.getName())));
	}

	@Test
	public void shouldNotCreateDuplicatePosition() throws Exception {
		String NAME_POSITION = "Developer";
		// Given
		given(positionRepository.existsByName(NAME_POSITION)).willReturn(true);

		// When & Then
		mockMvc.perform(post("/positions/")
						.param("name", NAME_POSITION))
				.andExpect(status().isOk())
				.andExpect(view().name("create_position"))
				.andExpect(model().attribute("error", "Должность с таким названием уже существует."));
	}

	@Test
	public void shouldShowEditFormForExistingPosition() throws Exception {
		// Given
		Position position = new Position(1L, "Developer");
		given(positionRepository.findById(1L)).willReturn(Optional.of(position));

		// When & Then
		mockMvc.perform(get("/positions/edit/1"))
				.andExpect(status().isOk())
				.andExpect(view().name("edit_position"))
				.andExpect(model().attributeExists("position"))
				.andExpect(model().attribute("position", position));
	}

	@Test
	public void shouldReturn404WhenPositionNotFoundForEdit() {
		Long POSITION_ID = 999L;
		// Given
		given(positionRepository.findById(POSITION_ID)).willReturn(Optional.empty());

		// When & Then
		try {
			mockMvc.perform(get("/positions/edit/" + POSITION_ID));
		} catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo("Request processing failed: java.lang.IllegalArgumentException: Invalid position ID: " + POSITION_ID);
		}
	}

	@Test
	public void shouldUpdate() throws Exception {
		String NAME_POSITION = "Developer";
		Long ID_POSITION = 1L;
		// Given
		Position existing = new Position(ID_POSITION, NAME_POSITION);
		given(positionRepository.findById(ID_POSITION)).willReturn(Optional.of(existing));
		given(positionRepository.existsByName(NAME_POSITION)).willReturn(true);

		// When & Then
		mockMvc.perform(post("/positions/update/" + ID_POSITION)
						.param("name", NAME_POSITION))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/show_positions/"));
	}

	@Test
	public void shouldDeletePositionAndRedirect() throws Exception {
		mockMvc.perform(get("/positions/delete/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/positions/"));

		verify(positionRepository).sqlDeleteById(1L);
	}

	@Test
	public void updateForShortName() throws Exception {
		Long ID_POSITION = 1L;
		// "E" is short name
		String SHORT_NAME_POSITION = "E";

		MvcResult result = mockMvc.perform(post("/positions/update/" + ID_POSITION)
						.param("name", SHORT_NAME_POSITION))
				.andExpect(status().isOk())
				.andExpect(view().name("edit_position"))
				.andExpect(model().hasErrors())
				.andExpect(model().attributeHasFieldErrors("position", "name")).andReturn();

		java.util.Set<String> keysErrors = result.getModelAndView().getModel().keySet();
		System.out.println("================Keys:");
		for (String key : keysErrors) {
			System.out.println("----Key:");
			System.out.println("Name key:" + key);
			System.out.println("Value key:" + result.getModelAndView().getModel().get(key));
		}

		assertTrue(keysErrors.contains("name"));
		assertEquals("E", result.getModelAndView().getModel().get("name"));

		assertTrue(keysErrors.contains("error"));
		assertEquals("Имя должности должно быть от 3 to 15 символов.\n", result.getModelAndView().getModel().get("error"));
	}
}