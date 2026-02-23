package sn.xoslu.tech.ebank.mappers;

import sn.xoslu.tech.ebank.dtos.UserDTO;
import sn.xoslu.tech.ebank.entities.User;

public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO dto);
}
