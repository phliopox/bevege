package Jeong.jdbcRefactoring.Store;

import lombok.Data;

import java.util.List;

@Data
public class UploadFile {
    private String uploadFileName ;
    private String storeFileName;

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
