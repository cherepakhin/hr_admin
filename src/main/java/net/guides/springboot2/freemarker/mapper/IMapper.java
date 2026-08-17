package net.guides.springboot2.freemarker.mapper;

public interface IMapper <DTO, Entity>{
    DTO toDTO(Entity entity);
    Entity toEntity(DTO dto);
}
