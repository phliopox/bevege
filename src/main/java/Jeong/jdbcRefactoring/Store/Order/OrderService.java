package Jeong.jdbcRefactoring.Store.Order;

import Jeong.jdbcRefactoring.Account.AccountService2;
import Jeong.jdbcRefactoring.DTO.Member;
import Jeong.jdbcRefactoring.Store.Cart.CartService;
import Jeong.jdbcRefactoring.Store.Item.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final AccountService2 accountService;
    private final ItemService itemService;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, AccountService2 accountService, ItemService itemService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.accountService = accountService;
        this.itemService = itemService;
        this.cartService = cartService;
    }

    public PaymentInfo getPaymentInfo(String memberId){
        Member accountInfo = accountService.getAccountInfo(memberId);
        PaymentInfo pf = new PaymentInfo(accountInfo.getMoney(), accountInfo.getPoint());
        return pf;
    }
    public void orderItem(Map<String,Object> param,String memberId){


        String receiverName = (String) param.get("receiverName");
        String address = (String) param.get("address");

        Integer itemId = Integer.parseInt(String.valueOf(param.get("itemId")));
        Integer orderPrice = Integer.parseInt(String.valueOf(param.get("orderPrice")));
        Integer orderAmount = Integer.parseInt(String.valueOf(param.get("orderAmount")));

        OrderForm of = new OrderForm(
                memberId,
                itemId,
                orderPrice,
                orderAmount,
                "주문완료",
                receiverName,
                address
        );
        /*order db에 정보 저장*/
        orderRepository.insertOrderInfo(of);


        /*사용한 포인트와 현금 삭감*/
        Integer point = Integer.parseInt(String.valueOf(param.get("usedPoint")));
        Integer money = Integer.parseInt(String.valueOf(param.get("usedMoney")));
        accountService.PayMoneyAndPoint(money,point,memberId);

        /*장바구니에서 주문 상품 삭제*/
        cartService.deleteOneItem(memberId,itemId);
    }

    public List<OrderForm> getMyOrderInfo(String memberId){
        List<OrderForm> orderInfo = orderRepository.getOrderInfo(memberId);
        return orderInfo;
    }

    public Integer getItemPageNum(Integer itemId){
        Integer pageNum = itemService.getPageNum(itemId);
        return pageNum;
    }

    public void cancelOrder(Map<String, Object> param){
        Integer orderId = Integer.parseInt(String.valueOf(param.get("orderId")));
        orderRepository.cancelOrder(orderId);
    }

}
