package com.fa.ims.service.impl;

import com.fa.ims.dto.UserDto;
import com.fa.ims.entity.User;
import com.fa.ims.repository.DepartRepository;
import com.fa.ims.repository.RoleRepository;
import com.fa.ims.repository.UserListRepository;
import com.fa.ims.service.MailService;
import com.fa.ims.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.fa.ims.constant.CommonConstants.VietnameseSigns;

@Service
public class UserServiceImpl implements UserService {

    private static final int count = 0;

    @Autowired
    private MailService mailService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserListRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartRepository departRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findListUser() {
        return repository.findAll();
    }

    @Override
    public List<User> findAllByDeletedIsFalse() {
        return repository.findAllByDeletedIsFalse();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Boolean isEmailExists(String email) {
        return repository.findAllByUserEmail(email.trim()).size() > 0;
    }

    @Override
    public Integer countUserStartWith(String username) {
        return repository.countUserByUserNameStartsWith(username);
    }


    //Create map from userDto to User
    @Override
    public User mapperUserFromUserDto(UserDto userDto) {
        userDto.setUserFullname(userDto.getUserFullname().trim());
        userDto.setUserEmail(userDto.getUserEmail().trim());
        User user = modelMapper.map(userDto, User.class);
        user.setUserRole(roleRepository.findById(userDto.getUserRole()).get());
        user.setUserDepartment(departRepository.findById(userDto.getUserDepartment()).get());
        return user;
    }

    @Override
    @Transactional
    public Boolean saveNewUser(UserDto userDto) {
        User user = mapperUserFromUserDto(userDto);
        String username = getUsername(user.getUserFullname());
        String password = generateSecurePass();
        user.setUserPassword(passwordEncoder.encode(password));
        user.setUserName(username);
        if (repository.save(user) != null) {
            mailService.sendEmailPassword(username, password, user.getUserEmail(), "Create ");
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean resetPassWord(String email) {
        User user = repository.findUserByUserEmail(email).orElseThrow();

        String password = generateSecurePass();
        user.setUserPassword(passwordEncoder.encode(password));
        if (repository.save(user) != null) {
            mailService.sendEmailPassword(user.getUserName(), password, email, "Reset ");
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public User saveUpdatedUser(UserDto userDto, Long id) {
        User user = mapperUserFromUserDto(userDto);
        User user1 = this.findUserById(id).orElseThrow();
        user.setUserName(user1.getUserName());
        user.setUserPassword(user1.getUserPassword());
        user.setId(id);
        return repository.save(user);
    }

    @Override
    public UserDto mapperUserDtoFromUser(User user) {
        TypeMap<User, UserDto> typeMap = modelMapper.getTypeMap(User.class, UserDto.class);
        if (typeMap == null) {
            typeMap = this.modelMapper.createTypeMap(User.class, UserDto.class);
            typeMap.addMappings(modelMapper -> modelMapper.skip(UserDto::setUserRole))
                    .addMappings(modelMapper -> modelMapper.skip(UserDto::setUserDepartment));
        }
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setUserDepartment(user.getUserDepartment().getDepartmentId());
        userDto.setUserRole(user.getUserRole().getRoleId());

        return userDto;
    }

    @Override
    @Transactional
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public Page<User> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.repository.findAllByDeletedIsFalse(pageable);
    }

    @Override
    public List<User> findUserByRole(String role) {
        return repository.findAllByUserRole_RoleDesc(role);
    }

    @Override
    public Boolean isEmailExists(String email, Long id) {
        if(email.equals(repository.findUserById(id).getUserEmail())) {
            return false;
        }
        return this.isEmailExists(email);
    }

    @Override
    @Transactional
    public User delete(Long id) {
        User user = this.findUserById(id).orElseThrow();
        user.setDeleted(true);
        return this.save(user);
    }

    @Override
    public Page<User> findUserByKeyword(int pageNo, int pageSize, String keyword, String role) {
        Pageable pageable = PageRequest.of(pageNo -1 , pageSize);
        if(keyword.equals("") && role.equals("")) {
            return findPaginated(pageNo, pageSize);
        }
        return this.repository.search(keyword, role, pageable);
    }


    //Get username from fullname to save db
    public String getUsername(String fullname) {
        String ans = "";
        fullname = VietnameseToASCII(fullname);
        String[] arrayList = fullname.split(" ");

        for (int i = 0; i < arrayList.length - 1; i++) {
            ans += arrayList[i].charAt(0);
        }

        ans = arrayList[arrayList.length - 1].toLowerCase() + ans.toUpperCase();
        Integer a = countUserStartWith(ans);

        return (a > 0) ? ans + countUserStartWith(ans) : ans;
    }

    //Create random securepass for user account
    public String generateSecurePass() {
        CharacterRule LCR = new CharacterRule(EnglishCharacterData.LowerCase);
        LCR.setNumberOfCharacters(2);

        CharacterRule UCR = new CharacterRule(EnglishCharacterData.UpperCase);
        UCR.setNumberOfCharacters(2);

        CharacterRule DCR = new CharacterRule(EnglishCharacterData.Digit);
        DCR.setNumberOfCharacters(2);

        PasswordGenerator passGen = new PasswordGenerator();

        String password = passGen.generatePassword(6, UCR, LCR, DCR);

        System.out.println(password);

        return password;
    }

    //Conver from Vietnamese to English
    public String VietnameseToASCII(String str) {
        for (int i = 1; i < VietnameseSigns.size(); i++) {
            for (int j = 0; j < VietnameseSigns.get(i).length(); j++)
                str = str.replace(VietnameseSigns.get(i).charAt(j), VietnameseSigns.get(0).charAt(i - 1));
        }
        return str;
    }

}
