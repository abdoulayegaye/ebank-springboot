package sn.xoslu.tech.ebank.mappers.impl;

import org.springframework.stereotype.Component;
import sn.xoslu.tech.ebank.dtos.UserDTO;
import sn.xoslu.tech.ebank.entities.User;
import sn.xoslu.tech.ebank.mappers.UserMapper;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDTO toDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setLastname(user.getLastname());
        dto.setFirstname(user.getFirstname());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        return dto;
    }

    @Override
    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setLastname(dto.getLastname());
        user.setFirstname(dto.getFirstname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        return user;
    }
}
