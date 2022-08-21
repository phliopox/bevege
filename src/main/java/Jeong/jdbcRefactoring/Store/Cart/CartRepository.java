package Jeong.jdbcRefactoring.Store.Cart;


import Jeong.jdbcRefactoring.Store.form.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class CartRepository {

    private final JdbcTemplate jdbcTemplate;
    public CartRepository(DataSource dataSource) {
        this.jdbcTemplate=new JdbcTemplate(dataSource);
    }

    public Integer checkHasCart(String memberId) {
        String checkCartSql="select cart_id from member_cart where member_id=?";
        List<Integer> cartIdList = jdbcTemplate.query(checkCartSql, (rs, rowNum) -> rs.getInt("cart_id"), memberId);
        return cartIdList.isEmpty()?null:cartIdList.get(0);
    }
    public void addItemToCart(String memberId,Map<String,Object> cartItem){
        Integer addItemId = Integer.parseInt(String.valueOf(cartItem.get("itemId")));
        Integer orderAmount = Integer.parseInt(String.valueOf(cartItem.get("orderAmount")));

        Integer cartId = checkHasCart(memberId);

        if(cartId==null){
            /*memberId, itemId,orderAmount*/
            String insertSql="insert into member_cart values(ifnull(((select max(cart_id) from member_cart N)+1),1),?,?,?)";
            jdbcTemplate.update(insertSql,memberId,addItemId,orderAmount);
        }else{
            Integer itemAmount = getItemAmountInCart(memberId, addItemId);

            if(itemAmount==null){
                /*cartId,memberId,itemId,itemAmount*/
                String addItemSql="insert into member_cart values(?,?,?,?)";
                jdbcTemplate.update(addItemSql,cartId,memberId,addItemId,orderAmount);
            }else{
                int sum = itemAmount + orderAmount;
                updateItemAmount(sum,addItemId,memberId);
            }

        }
    }
    public void updateItemAmount(Integer editAmount,Integer item_id,String memberId){
        String sql = "update member_cart set item_order_amount=? where member_id=? and item_id=?";
        jdbcTemplate.update(sql,editAmount,memberId,item_id);
    }

    public void deleteItem(String memberId, Integer itemId){
        String sql = "delete from member_cart where member_id=? and item_id=?";
        jdbcTemplate.update(sql,memberId,itemId);
    }
    private Integer getItemAmountInCart(String memberId, Integer addItemId) {
        String itemCheck="select item_order_amount from member_cart where member_id=? and item_id=?";

        List<Integer> itemAmountList = jdbcTemplate.query(itemCheck, (rs, rowNum) -> rs.getInt("item_order_amount"), memberId, addItemId);
        return itemAmountList.isEmpty()?null:itemAmountList.get(0);
    }


    public Integer getAllItemAmount(Integer cartId){
        String sql="select count(item_id) from member_cart where cart_id=?";
        Integer cartItemAmount = jdbcTemplate.queryForObject(sql, Integer.class,cartId);
        return cartItemAmount==null?0:cartItemAmount;
    }



    public List<Item> cartItem(String member_id){
        String sql="select * from member_cart A join item B on A.item_id=B.item_id and member_id=?";
        List<Item> itemList = jdbcTemplate.query(sql, new RowMapper<Item>() {
            @Override
            public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                Item item = new Item(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getString("item_description"),
                        rs.getInt("item_order_amount"),
                        rs.getInt("price"),
                        rs.getString("main_image_file"),
                        rs.getString("content_image_file"));
                return item;
            }
        }, member_id);
        return itemList.isEmpty()?null:itemList;
    }

    public Map pointAndMoney(String memberId){
        String sql="select point,money from account where member_id=?";

        List<Map> MapList = jdbcTemplate.query(sql, pointMoneyMapper(), memberId);
        Map ZeroMap=new HashMap();
        ZeroMap.put("point",0);
        ZeroMap.put("money",0);
        return MapList.isEmpty()?ZeroMap:MapList.get(0);
    }

    public RowMapper<Map> pointMoneyMapper(){
        return (rs , rowNum) ->{
            Map<String,Integer> map=new HashMap<>();
            map.put("point",rs.getInt("point"));
            map.put("money",rs.getInt("money"));
            return map;
        };
    }
    public RowMapper<Cart> cartRowMapper(){
        return (rs, rowNum) -> {
            Cart cart=new Cart(
                    rs.getInt("cart_id"),
                    rs.getString("member_id"),
                    rs.getInt("item_id"),
                    rs.getInt("item_order_amount")
            );
            return cart;
        };
    }
}
