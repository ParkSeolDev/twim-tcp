package jpabook.jpashop.api.service;


import jpabook.jpashop.api.request.UserRegisterPostReq;
import jpabook.jpashop.api.request.UserUpdateReq;
import jpabook.jpashop.common.util.SHA256;
import jpabook.jpashop.db.entity.Address;
import jpabook.jpashop.db.entity.User;
import jpabook.jpashop.db.repository.UserRepository;
import jpabook.jpashop.db.repository.UserRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final UserRepositorySupport userRepositorySupport;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserRegisterPostReq userRegisterInfo) {
        User user = new User();
        user.setUserId(userRegisterInfo.getUserId());
        // 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
        user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword()));
        user.setName(userRegisterInfo.getName());
        Address address = new Address(userRegisterInfo.getCity(), userRegisterInfo.getStreet(), userRegisterInfo.getZipcode());
        user.setAddress(address);
        userRepository.save(user);
        return user;
    }

    @Override
    public User createUser_sha256(UserRegisterPostReq userRegisterInfo) {
        User user = new User();
        user.setUserId(userRegisterInfo.getUserId());

        // 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
        SHA256 sha256 = new SHA256();
        user.setPassword(sha256.encrypt(userRegisterInfo.getPassword()));
        user.setName(userRegisterInfo.getName());
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUserByUserId(String userId) {
        // 디비에 유저 정보 조회 (userId 를 통한 조회).
        User user = userRepository.findById(userId).get();
        return user;
    }

    @Override
    public User getUserByUserName(String userName) {
        // 디비에 유저 정보 조회 (userId 를 통한 조회).
        User user = userRepository.findByName(userName);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UserUpdateReq userUpdateInfo){
        User user = new User();
        user.setUserId(userUpdateInfo.getUserId());
        // 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
        user.setPassword(passwordEncoder.encode(userUpdateInfo.getPassword()));
        user.setName(userUpdateInfo.getName());
        userRepository.updateUser(user.getName(), user.getUserId());
    }

    @Override
    public void deleteByUserId(String userId){
        userRepository.deleteById(userId);
    }

    @Override
    public boolean idCheck(String userId) {
        boolean usercheck = userRepositorySupport.idCheck(userId);
        return usercheck;
    }

}
