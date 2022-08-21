package Jeong.jdbcRefactoring.Store.Order;

import lombok.Data;

import java.util.Date;

@Data
public class OrderForm {
    String memberId;
    Integer itemId;

    Integer orderPrice;
    Integer orderAmount;
    String orderStatus;
    Integer orderId;

    String receiverName;

    String address;

    Date regDate;

    String itemImgStoreName;

    String itemName;

    public OrderForm(String memberId, Integer itemId, Integer orderPrice, Integer orderAmount, String orderStatus, String receiverName, String address) {
        this.memberId = memberId;
        this.itemId = itemId;
        this.orderPrice = orderPrice;
        this.orderAmount = orderAmount;
        this.orderStatus = orderStatus;
        this.receiverName = receiverName;
        this.address = address;

    }

    public OrderForm(String memberId, Integer itemId, Integer orderPrice, Integer orderAmount, String orderStatus,
                     Integer orderId, String receiverName, String address, Date regDate,String itemImgStoreName,String itemName) {
        this.memberId = memberId;
        this.itemId = itemId;
        this.orderPrice = orderPrice;
        this.orderAmount = orderAmount;
        this.orderStatus = orderStatus;
        this.orderId = orderId;
        this.receiverName = receiverName;
        this.address = address;
        this.regDate = regDate;
        this.itemImgStoreName = itemImgStoreName;
        this.itemName=itemName;
    }
}
