package jpabook.jpashop.api.controller;


import jpabook.jpashop.api.request.UserRegisterPostReq;
import jpabook.jpashop.api.request.UserUpdateReq;
import jpabook.jpashop.api.response.UserRes;
import jpabook.jpashop.api.service.JwtService;
import jpabook.jpashop.api.service.UserService;
import jpabook.jpashop.common.auth.TwimUserDetails;
import jpabook.jpashop.db.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "유저 API", tags = {"User"})
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping()
    @ApiOperation(value = "회원 가입", notes = "<strong>아이디와 패스워드</strong>를 통해 회원가입 한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<String> register(
            @RequestBody @ApiParam(value="회원가입 정보", required = true) @Valid UserRegisterPostReq registerInfo) {

        User user = userService.createUser(registerInfo);
        return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
    }

    @PostMapping("/new_sha256")
    @ApiOperation(value = "회원 가입_sha256", notes = "<strong>아이디와 패스워드</strong>를 통해 회원가입 한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<String> register_sha256(
            @RequestBody @ApiParam(value="회원가입 정보", required = true) @Valid UserRegisterPostReq registerInfo) {

        User user = userService.createUser_sha256(registerInfo);
        return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/me")
    @ApiOperation(value = "회원 본인 정보 조회", notes = "로그인한 회원 본인의 정보를 응답한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<UserRes> getUserInfo(@ApiIgnore Authentication authentication) {
        /**
         * 요청 헤더 액세스 토큰이 포함된 경우에만 실행되는 인증 처리이후, 리턴되는 인증 정보 객체(authentication) 통해서 요청한 유저 식별.
         * 액세스 토큰이 없이 요청하는 경우, 403 에러({"error": "Forbidden", "message": "Access Denied"}) 발생.
         */
        TwimUserDetails userDetails = (TwimUserDetails)authentication.getDetails();
        String userId = userDetails.getUsername();
        User user = userService.getUserByUserId(userId);

        return ResponseEntity.status(200).body(UserRes.of(user));
    }
////////////////////////////////////확인
    @GetMapping("/me/{userId}")
    @ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token을 반환한다.", response = Map.class)
    public ResponseEntity<Map<String, Object>> getUserInfo(
            @PathVariable("userId") @ApiParam(value = "인증할 회원의 아이디.", required = true) String userId,
            HttpServletRequest request) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        if (jwtService.isUsable(request.getHeader("access-token"))) {
            logger.info("사용 가능한 토큰!!!");
            try {
                User user = userService.getUserByUserId(userId);
                resultMap.put("userInfo", user);
                resultMap.put("message", SUCCESS);
                status = HttpStatus.ACCEPTED;
            }	catch (Exception e) {
                logger.error("정보조회 실패 : {}", e);
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

        } else {
            logger.error("사용 불가능 토큰!!!");
            resultMap.put("message", FAIL);
            status = HttpStatus.ACCEPTED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);

    }


    @GetMapping("/user-list")
    @ApiOperation(value = "회원 정보 조회", notes = "모든 회원 정보를 응답한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<List<User>> getAllUsers() {

        List<User> list = userService.getAllUsers();

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PatchMapping("/{user_id}")
    @ApiOperation(value = "회원 정보 수정", notes = "회원 정보를 수정한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateReq userUpdateInfo){
        userService.updateUser(userUpdateInfo);
        return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
    }

    @DeleteMapping("/{user_id}")
    @ApiOperation(value = "회원 정보 삭제", notes = "회원 정보를 삭제한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<Map<String, Object>> deleteByUserId(@PathVariable String user_id){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            userService.deleteByUserId(user_id);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.ACCEPTED;
        } catch (Exception error) {
            resultMap.put("message", error.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }
}


