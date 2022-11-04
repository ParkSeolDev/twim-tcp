package jpabook.jpashop.api.service;


import jpabook.jpashop.api.request.UserRegisterPostReq;
import jpabook.jpashop.api.request.UserUpdateReq;
import jpabook.jpashop.db.entity.User;

import java.util.List;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserService {
    User createUser(UserRegisterPostReq userRegisterInfo);
    User createUser_sha256(UserRegisterPostReq userRegisterInfo);
    User getUserByUserId(String userId);

    User getUserByUserName(String userName);
    List<User> getAllUsers();
    void updateUser(UserUpdateReq userUpdateInfo);
    void deleteByUserId(String userId);
    boolean idCheck(String userId);
}
