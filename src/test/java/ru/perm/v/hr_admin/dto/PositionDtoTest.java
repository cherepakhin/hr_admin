package ru.perm.v.hr_admin.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PositionDtoTest {

    @Test
    public void testConstructorAndGetters() {
        PositionDto dto = new PositionDto(1L, "Developer");

        assertEquals(1L, dto.getId());
        assertEquals("Developer", dto.getName());
    }

    @Test
    public void testEqualsSameReference() {
        PositionDto dto = new PositionDto(1L, "Developer");

        assertEquals(dto, dto);
    }

    @Test
    public void testEqualsEqualValues() {
        PositionDto dto1 = new PositionDto(1L, "Developer");
        PositionDto dto2 = new PositionDto(1L, "Developer");

        assertEquals(dto1, dto2);
    }

    @Test
    public void testNotEqualsDifferentId() {
        PositionDto dto1 = new PositionDto(1L, "Developer");
        PositionDto dto2 = new PositionDto(2L, "Developer");

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testNotEqualsDifferentName() {
        PositionDto dto1 = new PositionDto(1L, "Developer");
        PositionDto dto2 = new PositionDto(1L, "Manager");

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testNotEqualsNull() {
        PositionDto dto = new PositionDto(1L, "Developer");

        assertNotEquals(null, dto);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        PositionDto dto = new PositionDto(1L, "Developer");

        assertNotEquals(new Object(), dto);
    }

    @Test
    public void testEqualsHashCode() {
        PositionDto dto1 = new PositionDto(1L, "Developer");
        PositionDto dto2 = new PositionDto(1L, "Developer");

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testNotEqualsHashCodeByName() {
        PositionDto dto1 = new PositionDto(1L, "Developer");
        PositionDto dto2 = new PositionDto(1L, "Manager");

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testToString() {
        PositionDto dto = new PositionDto(1L, "Developer");
        String str = dto.toString();

        org.assertj.core.api.Assertions.assertThat(str)
                .contains("id = 1")
                .contains("name = Developer");
    }
}