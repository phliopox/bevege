package Jeong.jdbcRefactoring.Store.Order;

import Jeong.jdbcRefactoring.Store.Item.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static Jeong.jdbcRefactoring.SessionConst.MEMBER_LOGIN_SESSION;

@Slf4j
@Controller
public class OrderController {

    private final OrderService orderService;


    public OrderController(OrderService orderService, ItemService itemService) {
        this.orderService = orderService;
    }


    @ResponseBody
    @GetMapping("/my_page/order/payment")
    public PaymentInfo paymentInfo(HttpServletRequest request){
        String memberIdBySession = memberIdBySession(request);
        PaymentInfo paymentInfo = orderService.getPaymentInfo(memberIdBySession);
        return paymentInfo;
    }

    @ResponseBody
    @PostMapping("/my_page/order")
    public Integer order(@RequestBody Map<String,Object> param, HttpServletRequest request){
        String memberIdBySession = memberIdBySession(request);
        log.info("controller param={}",param);
        orderService.orderItem(param,memberIdBySession);

        return 1;
    }
    @GetMapping("/my_page/my_order")
    public String order(HttpServletRequest request, Model model){
        String memberIdBySession = memberIdBySession(request);
        List<OrderForm> myOrderInfo = orderService.getMyOrderInfo(memberIdBySession);
        model.addAttribute("orderInfoList", myOrderInfo);
        return "refactor/myPage/my_order";
    }
    @ResponseBody
    @GetMapping("/my_page/my_order/{itemId}")
    public Integer moveToItemPage(@PathVariable Integer itemId){
        Integer itemPageNum = orderService.getItemPageNum(itemId);
        return itemPageNum;
    }

    @ResponseBody
    @PostMapping("/my_page/cancel_order")
    public Integer cancelOrder(@RequestBody Map<String, Object> param) {
        orderService.cancelOrder(param);
        return 1;
    }

    private String memberIdBySession(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        return (String)session.getAttribute(MEMBER_LOGIN_SESSION);
    }
}
