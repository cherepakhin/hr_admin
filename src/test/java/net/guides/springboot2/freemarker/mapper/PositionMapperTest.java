package net.guides.springboot2.freemarker.mapper;

import net.guides.springboot2.freemarker.dto.PositionDto;
import net.guides.springboot2.freemarker.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionMapperTest {

    private PositionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PositionMapper();
    }

    @Test
    void toDTO_withValidPosition_returnsCorrectDto() {
        // Given
        Position position = new Position(1L, "Developer");

        // When
        PositionDto dto = mapper.toDTO(position);

        // Then
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Developer", dto.getName());
    }

    @Test
    void toDTO_withZeroId_returnsDtoWithZeroId() {
        // Given
        Position position = new Position(0L, "Tester");

        // When
        PositionDto dto = mapper.toDTO(position);

        // Then
        assertNotNull(dto);
        assertEquals(0L, dto.getId());
        assertEquals("Tester", dto.getName());
    }

    @Test
    void toDTO_withEmptyName_returnsDtoWithEmptyName() {
        // Given
        Position position = new Position(1L, "");

        // When
        PositionDto dto = mapper.toDTO(position);

        // Then
        assertNotNull(dto);
        assertEquals("", dto.getName());
    }

    @Test
    void toDTO_withNullName_returnsDtoWithNullName() {
        // Given
        Position position = new Position(1L, null);

        // When
        PositionDto dto = mapper.toDTO(position);

        // Then
        assertNotNull(dto);
        assertNull(dto.getName());
    }

    @Test
    void toDTO_withNullPosition_returnsNull() {
        // When & Then
        assertNull(mapper.toDTO(null));
    }

    @Test
    void toEntity_withValidDto_returnsCorrectPosition() {
        // Given
        PositionDto dto = new PositionDto(1L, "Manager");

        // When
        Position position = mapper.toEntity(dto);

        // Then
        assertNotNull(position);
        assertEquals(1L, position.getId());
        assertEquals("Manager", position.getName());
    }

    @Test
    void toEntity_withNullId_returnsPositionWithNullId() {
        // Given
        PositionDto dto = new PositionDto(null, "Architect");

        // When
        Position position = mapper.toEntity(dto);

        // Then
        assertNotNull(position);
        assertNull(position.getId());
        assertEquals("Architect", position.getName());
    }

    @Test
    void toEntity_withZeroId_returnsPositionWithZeroId() {
        // Given
        PositionDto dto = new PositionDto(0L, "Intern");

        // When
        Position position = mapper.toEntity(dto);

        // Then
        assertNotNull(position);
        assertEquals(0L, position.getId());
        assertEquals("Intern", position.getName());
    }

    @Test
    void toEntity_withEmptyName_returnsPositionWithEmptyName() {
        // Given
        PositionDto dto = new PositionDto(1L, "");

        // When
        Position position = mapper.toEntity(dto);

        // Then
        assertNotNull(position);
        assertEquals("", position.getName());
    }
}