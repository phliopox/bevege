package Jeong.jdbcRefactoring.Store.Order;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<OrderForm> getOrderInfo(String memberId) {
//        String sql = "select*from member_order where member_id=?";
        String sql ="select A.*, B.main_image_file,B.item_name from member_order A join item B on A.item_id=B.item_id where A.member_id=?  order by A.order_id desc;";
        List<OrderForm> formList = jdbcTemplate.query(sql, (rs, rowNum) -> {
            OrderForm of = new OrderForm(
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
            return of;
        }, memberId);
        return formList.isEmpty() ? null : formList;
    }

    public void cancelOrder(Integer orderId){
        String sql="update member_order set order_status='주문취소' where order_id=? ";
        jdbcTemplate.update(sql,orderId);
    }
    public void insertOrderInfo(OrderForm orderForm){
        String sql="insert into member_order values(?,?,?,?,?,ifnull(((select max(order_id) from member_order N)+1),1),?,?,now())";
        jdbcTemplate.update(sql,
                orderForm.getMemberId(),
                orderForm.getItemId(),
                orderForm.getOrderPrice(),
                orderForm.getOrderAmount(),
                orderForm.getOrderStatus(),
                orderForm.getReceiverName(),
                orderForm.getAddress());
    }

}
