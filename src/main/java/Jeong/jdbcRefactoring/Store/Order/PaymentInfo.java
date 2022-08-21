package Jeong.jdbcRefactoring.Store.Order;

import lombok.Data;

@Data
public class PaymentInfo {
    Integer money;
    Integer point;

    public PaymentInfo(Integer money, Integer point) {
        this.money = money;
        this.point = point;
    }
}
