package jpabook.jpashop.db.mapper;

import org.springframework.stereotype.Component;

import jpabook.jpashop.db.dto.UserDTO;
import jpabook.jpashop.db.entity.User;

@Component
public class UserMapper {
    public UserDTO toDto(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .password(user.getPassword())
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        return User.builder()
                .userId(userDTO.getUserId())
                .name(userDTO.getName())
                .password(userDTO.getPassword())
                .build();
    }
}
