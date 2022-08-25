package Jeong.jdbcRefactoring.Store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
public class ItemFileStore {

    @Value("${file.dir}")
    private String ItemFileDir;

    @Value("${imgFile.dir}")
    private String reviewImgDir;

    public String getFullPathToItem(String filename){
        return ItemFileDir+filename;
    }
    public String getFullPathToReviewImg(String filename){
        return reviewImgDir+filename;
    }

    public UploadFile createStoreName(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()){
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();

        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);

        String uuid= UUID.randomUUID().toString();
        String storeImageName= uuid+"."+ext;
        multipartFile.transferTo(new File(getFullPathToItem(storeImageName)));


        return new UploadFile(originalFilename,storeImageName);
    }

    private int id=0;


    public Map<String,Object> returnTypeMap(MultipartFile multipartFile) throws IOException{
        if(multipartFile.isEmpty()){
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();

        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);

        String uuid= UUID.randomUUID().toString();
        String storeImageName= uuid+"."+ext;

        multipartFile.transferTo(new File(getFullPathToReviewImg(storeImageName)));

        Map<String, Object> map = new HashMap<>();
        map.put("uploaded", 1);
        map.put("fileName", storeImageName);
        map.put("url","/reviews/image/"+storeImageName);
        return map;

    }
}
