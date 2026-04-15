package net.guides.springboot2.freemarker.repository;

import net.guides.springboot2.freemarker.model.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/*
	С DataJpaTest программа будет загружена полностью
 */
@DataJpaTest
/*
DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD
При таком режиме контекст приложения помечается как грязный после выполнения каждого
тестового метода в классе. Это означает, что после каждого тестового метода контекст
будет удалён из кэша и закрыт, а для последующих тестов с той же конфигурацией будет
создан новый контекст.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PositionRepositoryDataJpaTest {

	@Autowired
	private PositionRepository positionRepository;

	@Test
	public void whenFindById_thenReturnEmployee() {
		Long POSITION_ID = 100L;
		Position position = new Position();
		position.setId(POSITION_ID);
		position.setName("Developer");
		positionRepository.save(position); // Сохранение для теста

		Optional<Position> foundPosition = positionRepository.findById(POSITION_ID);

		assertThat(foundPosition).isPresent();
		assertThat(foundPosition.get().getId()).isEqualTo(POSITION_ID);
		assertThat(foundPosition.get().getName()).isEqualTo("Developer");
	}

	// genereated gigacode
	@Test
	public void shouldSaveAndFindPositionById() {
		// Given
		Position position = new Position();
		Long nextId = positionRepository.getNextId();
		if (nextId == null) {
			nextId = 1L;
		}
		position.setId(nextId);
		position.setName("Developer");

		// When
		positionRepository.save(position);
		Position found = positionRepository.findById(position.getId()).orElse(null);

		// Then
		assertThat(found).isNotNull();
		assertThat(found.getName()).isEqualTo("Developer");
	}

	@Test
	public void shouldReturnTrueWhenPositionExistsByName() {
		// Given
		Position position = new Position();
		Long nextId = positionRepository.getNextId();
		if (nextId == null) {
			nextId = 1L;
		}
		position.setId(nextId);
		position.setName("Manager");
		positionRepository.save(position);

		// When
		boolean exists = positionRepository.existsByName("Manager");

		// Then
		assertThat(exists).isTrue();
	}

	@Test
	public void shouldReturnFalseWhenPositionDoesNotExistByName() {
		// Given
		Position position = new Position();
		position.setName("HR");
		Long nextId = positionRepository.getNextId();
		if (nextId == null) {
			nextId = 1L;
		}
		position.setId(nextId);
		positionRepository.save(position);

		// When
		boolean exists = positionRepository.existsByName("Developer");

		// Then
		assertThat(exists).isFalse();
	}

	@Test
	public void existsByNameForNullName() {
		boolean exists = positionRepository.existsByName(null);

		assertThat(exists).isFalse();
	}

	@Test
	public void shouldReturnTrueWhenAnotherPositionHasSameNameButDifferentId() {
		// Given
		Position pos1 = new Position();
		Long nextId = positionRepository.getNextId();
		if (nextId == null) {
			nextId = 1L;
		}
		pos1.setId(nextId);
		pos1.setName("Tester");
		positionRepository.save(pos1);

		Position pos2 = new Position();
		pos2.setId(nextId + 1);
		pos2.setName("Analyst");
		positionRepository.save(pos2);

		// When
		boolean exists = positionRepository.existsByNameAndIdNot("Tester", pos2.getId());

		// Then
		assertThat(exists).isTrue();
	}

	@Test
	public void shouldReturnFalseWhenSameIdHasTheName() {
		// Given
		Long nextId = positionRepository.getNextId();
		if (nextId == null) {
			nextId = 1L;
		}
		Position pos = new Position();
		pos.setId(nextId);
		pos.setName("Designer");
		positionRepository.save(pos);

		// When
		boolean exists = positionRepository.existsByNameAndIdNot("Designer", pos.getId());

		// Then
		assertThat(exists).isFalse();
	}

	@Test
	public void shouldReturnFalseWhenNoOtherPositionHasGivenName() {
		// Given
		Position pos = new Position();
		Long nextId = positionRepository.getNextId();
		if (nextId == null) {
			nextId = 1L;
		}
		pos.setId(nextId);
		pos.setName("Architect");
		positionRepository.save(pos);

		// When
		boolean exists = positionRepository.existsByNameAndIdNot("Unknown", pos.getId());

		// Then
		assertThat(exists).isFalse();
	}

	@Test
	public void shouldReturnNextIdAsOneWhenTableIsEmpty() {
		// When
		Long nextId = positionRepository.getNextId();

		// Then
		assertThat(nextId).isEqualTo(1L);
	}

	@Test
	public void shouldReturnNextIdBasedOnMaxId() {
		// Given
		Position pos1 = new Position();
		pos1.setId(5L);
		pos1.setName("A");
		positionRepository.save(pos1);
		Position pos2 = new Position();
		pos2.setId(3L);
		pos2.setName("B");
		positionRepository.save(pos2);
		Position pos3 = new Position();
		pos3.setId(7L);
		pos3.setName("C");
		positionRepository.save(pos3);

		// When
		Long nextId = positionRepository.getNextId();

		// Then
		assertThat(nextId).isEqualTo(8L); // max(id) + 1 = 7 + 1
	}
}