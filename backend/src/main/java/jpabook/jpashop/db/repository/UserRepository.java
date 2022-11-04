package jpabook.jpashop.db.repository;

import jpabook.jpashop.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 유저 모델 관련 디비 쿼리 생성을 위한 JPA Query Method 인터페이스 정의.
 */

public interface UserRepository extends JpaRepository<User, String> {
    // 아래와 같이, Query Method 인터페이스(반환값, 메소드명, 인자) 정의를 하면 자동으로 Query Method 구현됨.
   User findByName(String userName);
   
   @Query("update User u set u.name = :name, u.password = :password where u.userId = :userId")
   @Modifying(clearAutomatically = true)
   void updateUser(@Param("name") String userName, @Param("userId") String userId);

}