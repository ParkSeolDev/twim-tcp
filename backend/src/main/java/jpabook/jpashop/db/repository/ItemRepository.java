package jpabook.jpashop.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jpabook.jpashop.db.entity.item.Item;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

}