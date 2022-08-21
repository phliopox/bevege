package Jeong.jdbcRefactoring.Store.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/*html post 등록*/
@Data
public class ItemForm {

    private String itemName;
    private String itemDescription;
    private Integer amount;
    private Integer price;
    private MultipartFile mainImage;
    private MultipartFile contentImageFiles;
}
