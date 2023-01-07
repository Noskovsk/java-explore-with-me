package ru.practicum.exploreit.dto.user;

import org.modelmapper.ModelMapper;
import ru.practicum.exploreit.model.User;


public class UserMapper {
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static User toUser(UserDto userDto) {
        return MODEL_MAPPER.map(userDto, User.class);
    }

}
