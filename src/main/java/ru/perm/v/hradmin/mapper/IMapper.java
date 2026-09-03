package ru.perm.v.hradmin.mapper;

public interface IMapper <DTO, Entity>{
    DTO toDTO(Entity entity);
    Entity toEntity(DTO dto);
}
