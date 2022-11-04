package jpabook.jpashop.api.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jpabook.jpashop.db.entity.File;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jpabook.jpashop.api.request.FileStore;
import jpabook.jpashop.api.request.UploadFile;
import jpabook.jpashop.common.model.response.BasicResponse;
import jpabook.jpashop.common.util.ExcelUtil;
import jpabook.jpashop.db.dto.FileDTO;
import jpabook.jpashop.db.dto.UserDTO;
import jpabook.jpashop.db.mapper.FileMapper;
import jpabook.jpashop.db.mapper.UserMapper;
import jpabook.jpashop.db.repository.FileRepository;
import jpabook.jpashop.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {
    private final FileStore fileStore;
    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    private final ExcelUtil excelUtil;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final String ContentType = "XLSX";

    @Transactional
    public FileDTO createFile(FileDTO fileDto) {
        File file = fileRepository.save(fileMapper.toEntity(fileDto));
        return fileMapper.toDto(file);
    }

    @Transactional
    public String uploadFile(MultipartFile file) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(file);
        log.info("full path: {}", uploadFile.getFullPath());

        FileDTO fileDto = new FileDTO();
        fileDto.setName(uploadFile.getStoreFileName());
        fileDto.setFilePath(uploadFile.getFullPath());
        createFile(fileDto);

        return uploadFile.getFullPath();
    }

    public byte[] downloadFile(long fileId) throws IOException {
        String fullPath = fileRepository.findById(fileId).get().getFilePath();
        byte[] file = fileStore.retrieveFile(fullPath);
        return file;
    }

    public FileDTO getFile(long fileId) {
        return fileMapper.toDto(fileRepository.findById(fileId).get());
    }

    public BasicResponse addExcel(MultipartFile file) throws Exception {
        BasicResponse res = new BasicResponse();
    
        // 파일 존재하지 않는 경우
        if (file.isEmpty()) {
            res.setEmpty();
            res.setMessage("Excel 파일을 선택해주세요.");
            return res;
        }
    

        String contentType = file.getContentType();

        if(!contentType.equals(ContentType)) {
            res.setEmpty();
            res.setMessage("Excel 파일을 선택해주세요.");
            return res;
        }
    
        List<UserDTO> listUser = new ArrayList<UserDTO>();
    
        // 엑셀의 셀데이터를 가져와서 VO에 담기
        List<Map<String, Object>> listMap = excelUtil.getListData(file, 1, 3);
    
        for (Map<String, Object> map : listMap) {
            UserDTO userInfo = new UserDTO();
        
            // 각 셀의 데이터를 VO에 set한다.
            userInfo.setUserId(map.get("1").toString());
            userInfo.setPassword(map.get("2").toString());
            userInfo.setName(map.get("3").toString());
    
            listUser.add(userInfo);
        }
    
        // 리스트에 담은 VO를 DB에 저장
        for (UserDTO oneUser : listUser){
            userRepository.save(userMapper.toEntity(oneUser));
        }

        return res;
    }

}
