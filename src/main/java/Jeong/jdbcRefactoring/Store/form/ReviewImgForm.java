package Jeong.jdbcRefactoring.Store.form;

import lombok.Data;

@Data
public class ReviewImgForm {
    /*private Integer imgIndex;*/

    private String originalImgName;

    private String uniqueImgName;

    public ReviewImgForm( String originalImgName, String uniqueImgName) {
/*
        this.imgIndex = imgIndex;
*/
        this.originalImgName = originalImgName;
        this.uniqueImgName = uniqueImgName;
    }
}
