package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PositionController.class)
public class PositionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PositionRepository positionRepository;

	@Test
	public void shouldListPositions() throws Exception {
		// Given
		Position dev = new Position(1L, "Developer");
		given(positionRepository.findAll()).willReturn(Collections.singletonList(dev));

		// When & Then
		mockMvc.perform(get("/positions"))
				.andExpect(status().isOk())
				.andExpect(view().name("positions"))
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
		mockMvc.perform(post("/positions")
						.param("name", "Manager"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/positions/"));

		verify(positionRepository).save(argThat(p -> "Manager".equals(p.getName())));
	}

	@Test
	public void shouldNotCreateDuplicatePosition() throws Exception {
		// Given
		given(positionRepository.existsByName("Developer")).willReturn(true);

		// When & Then
		mockMvc.perform(post("/positions")
						.param("name", "Developer"))
				.andExpect(status().isOk())
				.andExpect(view().name("create_position"))
				.andExpect(model().hasErrors())
				.andExpect(model().attributeHasFieldErrors("position", "name"));
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
		// Given
		given(positionRepository.findById(999L)).willReturn(Optional.empty());

		// When & Then
		try {
			mockMvc.perform(get("/positions/edit/999"));
		} catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo("Request processing failed: java.lang.IllegalArgumentException: Invalid position ID: 999");
		}
	}

	@Test
	public void shouldUpdatePositionAndRedirect() throws Exception {
		// Given
		Position updated = new Position(null, "Senior Developer");
		Position existing = new Position(1L, "Developer");

		given(positionRepository.existsByNameAndIdNot("Senior Developer", 1L)).willReturn(false);
		given(positionRepository.findById(1L)).willReturn(Optional.of(existing));

		// When & Then
		mockMvc.perform(post("/positions/update/1")
						.param("name", "Senior Developer"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/positions/"));

		verify(positionRepository).save(argThat(p ->
				p.getId().equals(1L) &&
						"Senior Developer".equals(p.getName())
		));
	}

	@Test
	public void shouldNotUpdateWithDuplicateName() throws Exception {
		// Given
		Position existing = new Position(1L, "Developer");
		given(positionRepository.findById(1L)).willReturn(Optional.of(existing));
		given(positionRepository.existsByNameAndIdNot("Developer", 1L)).willReturn(true);

		// When & Then
		mockMvc.perform(post("/positions/update/1")
						.param("name", "Developer"))
				.andExpect(status().isOk())
				.andExpect(view().name("edit_position"))
				.andExpect(model().hasErrors())
				.andExpect(model().attributeHasFieldErrors("position", "name"));
	}

	@Test
	public void shouldDeletePositionAndRedirect() throws Exception {
		// When & Then
		mockMvc.perform(get("/positions/delete/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/positions/"));

		verify(positionRepository).deleteById(1L);
	}
}