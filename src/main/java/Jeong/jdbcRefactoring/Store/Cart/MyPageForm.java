package Jeong.jdbcRefactoring.Store.Cart;

import lombok.Data;

@Data
public class MyPageForm {

    String memberId;

    Integer cartItemAmount;

    Integer point;

    Integer accountMoney;

    public MyPageForm() {
    }

    public MyPageForm(String memberId, Integer cartItemAmount, Integer point, Integer accountMoney) {
        this.memberId = memberId;
        this.cartItemAmount = cartItemAmount;
        this.point = point;
        this.accountMoney = accountMoney;
    }
}
