package jpabook.jpashop.db.repository;

import java.util.List;

import jpabook.jpashop.db.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * 유저 모델 관련 디비 쿼리 생성을 위한 구현 정의.
 */
@Repository
@RequiredArgsConstructor
public class UserRepositorySupport {

    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;



    @Transactional
    public User save(User user) {

        em.persist(user);
        return user;
    }

    @Modifying
    @Transactional
    public void updateUser(User user){
        Query query = em.createQuery("update User u set u.name = :name, u.password = :password where u.userId = :userId");
                query.setParameter("name", user.getName());
        query.setParameter("password", passwordEncoder.encode(user.getPassword()));
        query.setParameter("userId", user.getUserId());
        query.executeUpdate();
    }

    @Modifying
    @Transactional
    public void deleteByUserId(String userId){
        Query query = em.createQuery("delete from User u where u.userId = :userId");
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    @Transactional(readOnly = true)
    public boolean idCheck(String user_id) {
        User userCheck = em.createQuery("select u from User u where userId = :userId", User.class)
                .setParameter("userId", user_id)
                .getSingleResult();
        if (userCheck == null) {return false;}
        return true;
    }
}
