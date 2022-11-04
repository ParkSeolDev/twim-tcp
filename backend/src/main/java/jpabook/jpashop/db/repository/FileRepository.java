package jpabook.jpashop.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jpabook.jpashop.db.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {
    
}
