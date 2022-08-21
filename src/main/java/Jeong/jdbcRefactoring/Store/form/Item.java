package Jeong.jdbcRefactoring.Store.form;

import lombok.Data;

import java.util.List;

/*리포지토리 등록용 */
@Data
public class Item {
    private Integer itemId;
    private String itemName;
    private String itemDescription;
    private Integer amount;
    private Integer price;
    private String storeMainImageFileName;
    private String storeContentFileName;


    public Item(String itemName,String itemDescription,
                Integer price,
                String storeMainImageFileName,
                String storeContentFileName) {

        this.itemName = itemName;
        this.itemDescription=itemDescription;
        this.amount = amount;
        this.price = price;
        this.storeMainImageFileName = storeMainImageFileName;
        this.storeContentFileName = storeContentFileName;
    }

    public Item(Integer itemId,
                String itemName,
                String itemDescription,
                Integer price,
                String storeMainImageFileName,
                String storeContentFileName) {

        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDescription=itemDescription;
        this.price = price;
        this.storeMainImageFileName = storeMainImageFileName;
        this.storeContentFileName = storeContentFileName;
    }

    public Item(Integer itemId,
                String itemName,
                String itemDescription,
                Integer amount,
                Integer price,
                String storeMainImageFileName,
                String storeContentFileName) {

        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDescription=itemDescription;
        this.amount = amount;
        this.price = price;
        this.storeMainImageFileName = storeMainImageFileName;
        this.storeContentFileName = storeContentFileName;
    }

    public Item(Integer itemId, String itemName, String itemDescription, Integer price) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.price = price;
    }
}
