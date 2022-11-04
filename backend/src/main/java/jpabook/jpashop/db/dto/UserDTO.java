package jpabook.jpashop.db.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@Builder
public class UserDTO {
    String name;
    String userId;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    public UserDTO(){
        
    }

    public UserDTO(String name, String userId, String password){
        this.name = name;
        this.userId = userId;
        this.password = password;
    }
}
