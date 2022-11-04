package jpabook.jpashop.api.request;

import lombok.Data;

@Data
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;
    private String fullPath;

    public UploadFile(String uploadFileName, String storeFileName, String fullPath) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.fullPath = fullPath;
    }
}
