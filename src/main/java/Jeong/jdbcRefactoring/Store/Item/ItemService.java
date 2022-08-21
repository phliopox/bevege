package Jeong.jdbcRefactoring.Store.Item;

import Jeong.jdbcRefactoring.Board.Page.Criteria;
import Jeong.jdbcRefactoring.Board.Page.PageMarkerDTO;
import Jeong.jdbcRefactoring.Store.form.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    public ItemService(ItemRepository itemRepository){
        this.itemRepository=itemRepository;
    }


    public List<Item> itemList(Criteria criteria){
        return itemRepository.AllItem(criteria);
    }

    public PageMarkerDTO page(Criteria criteria){
        Integer total=itemRepository.CountItem();
        PageMarkerDTO Marker=new PageMarkerDTO(criteria,total);
        return Marker;
    }

    public List<Item> searchItem(String search){
        List<Item> items = itemRepository.searchItem(search);
        return items;
    }

    public Integer saveAndSelectId(Item item){
        itemRepository.saveItem(item);
        Item savedItem = itemRepository.findByItemName(item.getItemName());
        return savedItem.getItemId();
    }
    public Item findByItemId(Integer itemId){
        Item byItemId = itemRepository.findById(itemId);
        return byItemId;
    }

    public List<Item> NewItemInfo(){
        List<Item> items = itemRepository.NewItemInfo();
        return items;
    }
    public Integer getPageNum(Integer itemId){
        Integer rowNum=itemRepository.getRowNum(itemId);
        Integer pageNum=1;
        if(rowNum>=8){
            pageNum=(int)Math.ceil((rowNum/(double)8));
        }
        return pageNum;
    }

    public void editItemInfo(Item item){
        log.info("item={}",item);
        if (item.getStoreContentFileName() != null && item.getStoreMainImageFileName() != null) {
            itemRepository.editItemAllInfo(item);
        }else{
            itemRepository.editItemInfoOnlyText(item);
        }
    }

    public void deleteItem(Integer itemId){
        itemRepository.deleteItem(itemId);
    }
}
