// package jpabook.jpashop.db.repository;

// import jpabook.jpashop.db.entity.User;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Repository;
// import org.springframework.transaction.annotation.Transactional;

// import javax.persistence.EntityManager;
// import java.util.List;

// @Repository
// @RequiredArgsConstructor
// public class UserFindRepository {

//     private final EntityManager em;

//     @Transactional(readOnly = true)
//     public User findUserByUserId(String userId) {
//         User user = em.createQuery("select u from User u where userId = :userId", User.class)
//                 .setParameter("userId", userId)
//                 .getSingleResult();
//         return user;
//     }

//     @Transactional(readOnly = true)
//     public User findByUsername(String userName) {
//         User user = em.createQuery("select u from User u where name = :userName", User.class)
//                 .setParameter("userName", userName)
//                 .getSingleResult();
//         return user;
//     }

//     @Transactional(readOnly = true)
//     public List<User> findAll() {
//         return em.createQuery("select u from User u", User.class)
//                 .getResultList();
//     }

// }
