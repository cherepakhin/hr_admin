package ru.perm.v.hr_admin.repository;

import ru.perm.v.hr_admin.model.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/*
	С DataJpaTest программа будет загружена полностью
 */
@DataJpaTest
// @Sql("/positions.sql") - если нужно загрузить скрипты sql для тестирования
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
		Position position = new Position();
		position.setName("Developer");
		position = positionRepository.save(position); // Сохранение для теста

		Optional<Position> foundPosition = positionRepository.findById(position.getId());

		assertThat(foundPosition).isPresent();
		assertThat(foundPosition.get().getId().longValue() > 0);
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
		position.setName("HR123");
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
	public void shouldReturnFalseWhenNameIsNull() {
		// When
		boolean exists = positionRepository.existsByName(null);

		// Then
		assertThat(exists).isFalse();
	}

	@Test
	public void shouldReturnFalseWhenNameIsEmpty() {
		// When
		boolean exists = positionRepository.existsByName("");

		// Then
		assertThat(exists).isFalse();
	}

	@Test
	public void shouldReturnFalseWhenNameIsBlank() {
		// When
		boolean exists = positionRepository.existsByName("   ");

		// Then
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
		pos1.setName("Position 1");
		positionRepository.save(pos1);
		Position pos2 = new Position();
		pos2.setName("Position 2");
		positionRepository.save(pos2);
		Position pos3 = new Position();
		pos3.setName("Position 7");
		positionRepository.save(pos3);

		// When
		Long nextId = positionRepository.getNextId();

		// Then
		assertThat(nextId.intValue() > 0);
	}

	@Test
	public void findSortByPositionId() {
		Position positon1 = new Position(1L, "NAME_1");
		positionRepository.save(positon1);
		Position positon2 = new Position(2L, "NAME_2");
		positionRepository.save(positon2);

		List<Position> positions = positionRepository.findAllAndSort(Sort.by(Sort.Direction.ASC, "id"));
		System.out.println(positions.size());
		assertThat(positions).hasSize(2);
	}

	@Test
	public void findSortByPositionNameASC() {
		Position positon1 = new Position(1L, "NAME_200");
		positionRepository.save(positon1);
		Position positon2 = new Position(2L, "NAME_100");
		positionRepository.save(positon2);

		List<Position> positions = positionRepository.findAllAndSort(Sort.by(Sort.Direction.ASC, "name"));

		assertThat(positions).hasSize(2);
		assertThat(positions.get(0).getName()).isEqualTo("NAME_100");
		assertThat(positions.get(1).getName()).isEqualTo("NAME_200");
	}

	@Test
	public void findSortByPositionNameDESC() {
		positionRepository.save(new Position(1L, "NAME_200"));
		positionRepository.save(new Position(2L, "NAME_100"));

		List<Position> positions = positionRepository.findAllAndSort(Sort.by(Sort.Direction.DESC, "name"));

		assertThat(positions).hasSize(2);
		assertThat(positions.get(0).getName()).isEqualTo("NAME_200");
		assertThat(positions.get(1).getName()).isEqualTo("NAME_100");
	}
}