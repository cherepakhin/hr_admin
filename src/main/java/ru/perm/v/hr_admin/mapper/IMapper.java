package ru.perm.v.hr_admin.mapper;

public interface IMapper <DTO, Entity>{
    DTO toDTO(Entity entity);
    Entity toEntity(DTO dto);
}
