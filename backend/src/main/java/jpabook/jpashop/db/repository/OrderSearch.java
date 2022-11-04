package jpabook.jpashop.db.repository;

import jpabook.jpashop.db.entity.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {

    private String userName; //회원 이름
    private OrderStatus orderStatus; //주문 상태[ORDER, CANCEL]
}
