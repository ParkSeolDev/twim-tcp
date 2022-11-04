package jpabook.jpashop.api.controller;

import jpabook.jpashop.api.request.UserLoginPostReq;
import jpabook.jpashop.api.response.UserLoginPostRes;
import jpabook.jpashop.api.service.UserService;
import jpabook.jpashop.common.model.response.BaseResponseBody;
import jpabook.jpashop.common.util.JwtTokenUtil;
import jpabook.jpashop.common.util.SHA256;
import jpabook.jpashop.db.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

/**
 * 인증 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "인증 API", tags = {"Auth"})
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    public static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    private final UserService userService;


    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "<strong>아이디와 패스워드</strong>를 통해 로그인 한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = UserLoginPostRes.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<UserLoginPostRes> login(@RequestBody @ApiParam(value="로그인 정보", required = true) UserLoginPostReq loginInfo) {
        String userId = loginInfo.getUserId();
        String password = loginInfo.getPassword();

        User user = userService.getUserByUserId(userId);
        // 로그인 요청한 유저로부터 입력된 패스워드 와 디비에 저장된 유저의 암호화된 패스워드가 같은지 확인.(유효한 패스워드인지 여부 확인)
        if(passwordEncoder.matches(password, user.getPassword())) {
            // 유효한 패스워드가 맞는 경우, 로그인 성공으로 응답.(액세스 토큰을 포함하여 응답값 전달)
            return ResponseEntity.ok(UserLoginPostRes.of(200, "Success", JwtTokenUtil.getToken(userId)));
        }
        // 유효하지 않는 패스워드인 경우, 로그인 실패로 응답.
        return ResponseEntity.status(401).body(UserLoginPostRes.of(401, "Invalid Password", null));
    }

    @PostMapping("/login_sha256")
    @ApiOperation(value = "로그인_sha256", notes = "<strong>아이디와 패스워드</strong>를 통해 로그인 한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = UserLoginPostRes.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<UserLoginPostRes> login_sha256(@RequestBody @ApiParam(value="로그인 정보", required = true) UserLoginPostReq loginInfo) {
        String userId = loginInfo.getUserId();
        String password = loginInfo.getPassword();

        SHA256 sha256 = new SHA256();
        //SHA256으로 암호화된 비밀번호
        String cryptogram = sha256.encrypt(password);

        User user = userService.getUserByUserId(userId);
        // 로그인 요청한 유저로부터 입력된 패스워드 와 디비에 저장된 유저의 암호화된 패스워드가 같은지 확인.(유효한 패스워드인지 여부 확인)
        if(sha256.matches(cryptogram, user.getPassword())) {
            // 유효한 패스워드가 맞는 경우, 로그인 성공으로 응답.(액세스 토큰을 포함하여 응답값 전달)
            return ResponseEntity.ok(UserLoginPostRes.of(200, "Success", JwtTokenUtil.getToken(userId)));
        }
        // 유효하지 않는 패스워드인 경우, 로그인 실패로 응답.
        return ResponseEntity.status(401).body(UserLoginPostRes.of(401, "Invalid Password", null));
    }

    @ApiOperation(value = "회원가입시 아이디 중복 확인한다. 사용 가능 여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
    @GetMapping("/idcheck")
    public ResponseEntity<String> idCheck(@RequestParam("userId") String checkId) throws Exception{
        logger.debug("idCheck - 호출");
        if(userService.idCheck(checkId)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
    }

}
