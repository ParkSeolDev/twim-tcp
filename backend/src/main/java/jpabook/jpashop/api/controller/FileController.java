package jpabook.jpashop.api.controller;

import io.swagger.annotations.*;
import jpabook.jpashop.api.service.FileService;
import jpabook.jpashop.common.model.response.ApiUtils;
import jpabook.jpashop.common.model.response.BasicResponse;
import jpabook.jpashop.db.dto.FileDTO;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin("*")
@Api(value = "파일 API", tags = {"File"})
@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @ApiOperation(value = "파일 생성", notes = "파일을 생성함", response = ApiUtils.ApiResult.class)
    @PostMapping
    public ApiUtils.ApiResult<?> createFile(@ApiParam(value = "파일 정보", required = true) @RequestBody FileDTO fileDto) {
        return ApiUtils.success(fileService.createFile(fileDto));
    }

    @ApiOperation(value = "파일 업로드", notes = "서버에 파일을 업로드", response = ApiUtils.ApiResult.class)
    @PostMapping(value = "/upload/{fileId}")
    public ApiUtils.ApiResult<?> uploadFile(@ApiParam(value = "파일", required = true) @ModelAttribute(value = "files") List<MultipartFile> files) throws IOException {
        MultipartFile file = files.get(0);
        return ApiUtils.success(fileService.uploadFile(file));
    }

    @ApiOperation(value = "파일 다운로드", notes = "서버의 파일을 다운로드", response = ApiUtils.ApiResult.class)
    @GetMapping(value = "/download/{fileId}", produces = "application/png")
    public byte[] downloadFile(@ApiParam(value = "파일ID", required = true) @PathVariable long fileId) throws IOException {
        return fileService.downloadFile(fileId);
    }

    @ApiOperation(value = "특정 파일을 반환", notes = "fileId로 특정되는 파일을 반환", response = ApiUtils.ApiResult.class)
    @GetMapping("/{fileId}")
    public ApiUtils.ApiResult<?> findFile(@ApiParam(value = "파일ID", required = true) @PathVariable long fileId) {
        return ApiUtils.success(fileService.getFile(fileId));
    }

    @ApiOperation(value = "엑셀 파일을 저장", notes = "확장자가 XLSX인 파일의 데이터를 DB에 저장", response = BasicResponse.class)
    @PostMapping(value = "/addExcel")
    public ResponseEntity<? extends BasicResponse> addExcel(HttpServletRequest request,
		HttpServletResponse response, MultipartFile file) throws Exception {

	    return ResponseEntity.ok().body(fileService.addExcel(file));
    };
}
