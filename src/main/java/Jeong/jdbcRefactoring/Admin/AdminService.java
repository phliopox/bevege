package Jeong.jdbcRefactoring.Admin;

import Jeong.jdbcRefactoring.Store.Item.ItemService;
import Jeong.jdbcRefactoring.Store.ItemFileStore;
import Jeong.jdbcRefactoring.Store.Order.OrderForm;
import Jeong.jdbcRefactoring.Store.UploadFile;
import Jeong.jdbcRefactoring.Store.form.Item;
import Jeong.jdbcRefactoring.Store.form.ItemForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final ItemFileStore fileStore;

    private final ItemService itemService;

    public AdminService(AdminRepository adminRepository, ItemFileStore fileStore, ItemService itemService) {
        this.adminRepository = adminRepository;
        this.fileStore = fileStore;
        this.itemService = itemService;
    }

    public List<OrderForm> getAllOrders(){
        List<OrderForm> customerOrderList = adminRepository.getCustomerOrderList();
        return customerOrderList;
    }

    public void editOrderStatus(Map<String, Object> param) {
        Integer orderId = Integer.parseInt(String.valueOf(param.get("orderId")));
        String orderStatus = (String) param.get("orderStatus");
        adminRepository.updateOrderStatus(orderId,orderStatus);
    }

    public List<OrderForm> getSearchResult(String category,String keyword){
        List<OrderForm> searchResult = adminRepository.getSearchResult(category, keyword);
        return searchResult;
    }

    public Integer getItemPageNum(Integer itemId){
        Integer pageNum = itemService.getPageNum(itemId);
        return pageNum;

    }
    public Integer enrollItem(Item item){
        Integer itemId = itemService.saveAndSelectId(item);
        return itemId;
    }

    public Item findByItemId(Integer itemId){
        Item byItemId = itemService.findByItemId(itemId);
        return byItemId;
    }
    public void itemInfoEdit(ItemForm form,Integer itemId) throws IOException {
        Item item;
        if(form.getMainImage()!=null&&form.getContentImageFiles()!=null) {
            item = uploadFile(form);
            item.setItemId(itemId);
        }else{
            item=new Item(itemId,form.getItemName(),form.getItemDescription(),form.getPrice());
        }
        itemService.editItemInfo(item);
    }

    public void deleteItem(Map<String,Object> param){
        int itemId = Integer.parseInt(String.valueOf(param.get("itemId")));
        itemService.deleteItem(itemId);
    }
    public Item uploadFile(ItemForm form) throws IOException {
        UploadFile mainImageFile = fileStore.createStoreName(form.getMainImage());
        UploadFile contentsImageFile = fileStore.createStoreName(form.getContentImageFiles());


        Item newItem = new Item(
                form.getItemName(),
                form.getItemDescription(),
                form.getPrice(),
                mainImageFile.getStoreFileName(),
                contentsImageFile.getStoreFileName());
        return newItem;
    }
}
