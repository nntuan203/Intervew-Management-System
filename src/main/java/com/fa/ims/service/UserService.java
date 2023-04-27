package com.fa.ims.service;

import com.fa.ims.dto.UserDto;
import com.fa.ims.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findListUser();

    List<User> findAllByDeletedIsFalse();

    Optional<User> findUserById(Long id);

    User mapperUserFromUserDto(UserDto userDto);

    Boolean resetPassWord(String email);

    User saveUpdatedUser(UserDto userDto, Long id);

    Boolean saveNewUser(UserDto userDto);

    UserDto mapperUserDtoFromUser(User user);

    Boolean isEmailExists(String email);

    Integer countUserStartWith(String username);

    User save(User user);

    Page<User> findPaginated(int pageNo, int pageSize);

    List<User> findUserByRole(String role);

    Boolean isEmailExists(String email, Long id);

    User delete(Long id);


    Page<User> findUserByKeyword(int pageNo, int pageSize, String keyword, String role);
}
