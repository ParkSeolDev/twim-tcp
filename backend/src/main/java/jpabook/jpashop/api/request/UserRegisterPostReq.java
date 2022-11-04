package jpabook.jpashop.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 유저 회원가입 API ([POST] /api/v1/users) 요청에 필요한 리퀘스트 바디 정의.
 */
@Getter
@Setter
@ApiModel("UserRegisterPostRequest")
public class UserRegisterPostReq {
    @ApiModelProperty(name="유저 ID", example="twim_web")
    @NotBlank(message = "회원 아이디는 필수 입니다")
    private String userId;
    @ApiModelProperty(name="유저 Password", example="your_password")
    @NotBlank(message = "회원 비밀번호는 필수 입니다")
    private String password;
    @ApiModelProperty(name="유저 Name", example="your_name")
    @NotBlank(message = "회원 이름은 필수 입니다")
    @Size(min = 8, max = 20, message = "이름은 8~20자 입니다")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
