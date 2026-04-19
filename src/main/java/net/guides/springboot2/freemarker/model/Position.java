package net.guides.springboot2.freemarker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "positions")
public class Position {
    @Id
    private Long id;

	@Size(min = 3, max = 15, message = "Имя должности должно быть от 3 to 15 символов.")
    @Column(nullable = false, unique = true, length = 15)
    private String name;

    public Position() {}

	public Position(Long id, String name) {
		this.id = id;
		this.name = name;
	}

    public Position(String name) {
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Position position)) return false;
		return Objects.equals(id, position.id) && Objects.equals(name, position.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
}