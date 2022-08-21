package Jeong.jdbcRefactoring.Admin;

import Jeong.jdbcRefactoring.Store.Order.OrderForm;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdminRepository {

    private final JdbcTemplate jdbcTemplate;

    public AdminRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }

    public List<OrderForm> getCustomerOrderList(){
        String sql = "select A.*, B.main_image_file,B.item_name from member_order A join item B on A.item_id=B.item_id order by A.order_id desc;";
        List<OrderForm> orderFormList  = jdbcTemplate.query(sql, orderFormMapper());
        return orderFormList;
    }
    public void updateOrderStatus(Integer orderId,String orderStatus){
        String sql="update member_order set order_status=? where order_id=?";
        jdbcTemplate.update(sql,orderStatus,orderId);
    }

    public List<OrderForm> getSearchResult(String category,String keyword){
      String sql= "select A.*, B.main_image_file,B.item_name from member_order A join item B on A.item_id=B.item_id " +
              "where "+category+" like ? order by A.order_id desc;";
      String q="%"+keyword+"%";

      List<OrderForm> orderFormList = jdbcTemplate.query(sql, orderFormMapper(), q);
      return orderFormList;
    }
    public RowMapper<OrderForm> orderFormMapper(){
        return (rs, rowNum) ->  new OrderForm(
                rs.getString("member_id"),
                rs.getInt("item_id"),
                rs.getInt("order_price"),
                rs.getInt("order_amount"),
                rs.getString("order_status"),
                rs.getInt("order_id"),
                rs.getString("receiver_name"),
                rs.getString("address"),
                rs.getDate("reg_date"),
                rs.getString("main_image_file"),
                rs.getString("item_name")
        );
    }
}
