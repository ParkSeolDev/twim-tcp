package jpabook.jpashop.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jpabook.jpashop.db.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
