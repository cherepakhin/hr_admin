package ru.perm.v.hr_admin.dto;

import jakarta.validation.constraints.Size;
import ru.perm.v.hr_admin.model.Position;

import java.util.Objects;

/**
 * DTO for {@link Position}
 */
public class PositionDto {
    private final Long id;
    @Size(message = "Название должности должно быть от 3 to 15 символов.", min = 3, max = 15)
    private final String name;

    public PositionDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionDto entity = (PositionDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ")";
    }
}