package Jeong.jdbcRefactoring.Store.Item;

import Jeong.jdbcRefactoring.Board.Page.Criteria;
import Jeong.jdbcRefactoring.Store.form.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Repository
public class ItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public ItemRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveItem(Item item){
        String sql="insert into item values(ifnull(((select max(item_id) from item N)+1),1),?,?,?,?,?)";

        jdbcTemplate.update(sql
                , item.getItemName()
                , item.getPrice()
                , item.getStoreMainImageFileName()
                , item.getStoreContentFileName()
                , item.getItemDescription());

    }

    public List<Item> AllItem(Criteria criteria){
        String sql="select R1.*from(select*from item order by item_id desc)R1 limit ?,?";
        List<Item> allItems = jdbcTemplate.query(sql, ItemMapper(), criteria.getSkip(), criteria.getAmount());

        return allItems.isEmpty()?null:allItems;
    }
    public Integer CountItem(){
        String sql="select count(item_id) from item";
        Integer totalCount=jdbcTemplate.queryForObject(sql,Integer.class);
        return totalCount;
    }

    public List<Item> searchItem(String search){
        String sql = "select * from item where item_name like ? order by item_id desc";
        String keyword="%"+search+"%";
        log.info(keyword);
        List<Item> resultList = jdbcTemplate.query(sql, ItemMapper(), keyword);
        log.info("리포지토리={}",resultList);
        return resultList;
    }

    public List<Item> NewItemInfo(){
        String sql="select*from item order by item_id desc limit 4";
        List<Item> itemList = jdbcTemplate.query(sql, ItemMapper());
        return itemList;
    }

    public void editItemInfoOnlyText(Item item){
        log.info("텍스트수정리포 ={}",item);
        String sql="update item set item_name=?,price=?,item_description=? where item_id=?";
        jdbcTemplate.update(sql,
                item.getItemName(),
                item.getPrice(),
                item.getItemDescription(),
                item.getItemId());
    }

    public void editItemAllInfo(Item item) {
        log.info("전체수정리포 ={}",item);
        String sql = "update item set item_name=?,price=?,item_description=?,main_image_file=?,content_image_file=? where item_id=?";
        jdbcTemplate.update(sql,
                item.getItemName(),
                item.getPrice(),
                item.getItemDescription(),
                item.getStoreMainImageFileName(),
                item.getStoreContentFileName(),
                item.getItemId());
    }

    public void deleteItem(Integer itemId){
        String sql="delete from item where item_id=?";
        jdbcTemplate.update(sql,itemId);
    }
    public Integer getRowNum(Integer itemId){
        String sql="select  rownumber from ( (select @rownum := @rownum + 1 rownumber, t.* from " +
                "(select*from item order by item_id desc) as t  , " +
                "(select @rownum := 0 ) rownum)) as A  " +
                "where item_id=?;";
        Integer rowNum = jdbcTemplate.queryForObject(sql, Integer.class, itemId);
        return rowNum;
    }
    public Item findById(Integer ItemId){
        String sql="select *from Item where item_id=?";
        List<Item> ItemList = jdbcTemplate.query(sql, ItemMapper(), ItemId);

        return ItemList.isEmpty()?null:ItemList.get(0);
    }
    public Item findByItemName(String ItemName){
        String sql="select *from Item where item_name=?";
        List<Item> ItemList = jdbcTemplate.query(sql, ItemMapper(), ItemName);

        return ItemList.isEmpty()?null:ItemList.get(0);
    }

    public RowMapper<Item> ItemMapper(){
        return ((rs, rowNum) -> {
            Item item = new Item(
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getString("item_description"),
                    rs.getInt("price"),
                    rs.getString("main_image_file"),
                    rs.getString("content_image_file"));
            return item;
        });
    }
}
