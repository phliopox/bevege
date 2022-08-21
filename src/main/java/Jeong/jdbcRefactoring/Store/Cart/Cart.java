package Jeong.jdbcRefactoring.Store.Cart;

import lombok.Data;

@Data
public class Cart {
    Integer cartId;
    String memberId;
    Integer itemId;
    Integer OrderAmount;

    public Cart(Integer cartId, String memberId, Integer itemId, Integer orderAmount) {
        this.cartId = cartId;
        this.memberId = memberId;
        this.itemId = itemId;
        OrderAmount = orderAmount;
    }
}
