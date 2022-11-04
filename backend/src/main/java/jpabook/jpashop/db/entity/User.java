package jpabook.jpashop.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 유저 모델 정의.
 */
@Entity
@Getter
@Setter
@Builder
public class User implements Serializable{

    String name;
    @Id
    String userId;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    public User() {
    }

    public User(String name, String userId, String password, Address address, List<Order> orders){
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.address = address;
        this.orders = orders;

    }

    public User(String name, String userId){
        this.name = name;
        this.userId = userId;

    }

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("유저명: ");
        sb.append(name);
        sb.append("주소: ");
        sb.append(address);
        sb.append("주문: ");
        sb.append(orders);

        return sb.toString();
    }

}
