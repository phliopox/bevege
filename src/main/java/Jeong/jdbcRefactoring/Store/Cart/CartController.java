package Jeong.jdbcRefactoring.Store.Cart;

import Jeong.jdbcRefactoring.Store.form.Item;
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
public class CartController {
    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @ResponseBody
    @PostMapping("/my_page/add_cart")
    public Integer addCart(@RequestBody Map<String,Object> param,HttpServletRequest request){
        String memberIdBySession = memberIdBySession(request);
        cartService.addItemToCart(memberIdBySession,param);
        return 1;
    }


    @GetMapping("/my_page/cart")
    public String cart(HttpServletRequest request, Model model){
        String memberId = memberIdBySession(request);
        List<Item> itemList = cartService.cartItemByMemberId(memberId);
        model.addAttribute("itemList",itemList);
        return "refactor/myPage/cart";
    }

    @ResponseBody
    @PostMapping("/my_page/cart/amount_edit")
    public Integer editAmount(@RequestBody Map<String,Object> param,HttpServletRequest request){
        String memberIdBySession = memberIdBySession(request);
        cartService.editItemAmount(memberIdBySession,param);
        return 1;
    }

    @ResponseBody
    @PostMapping("/my_page/cart/delete_item")
    public Integer deleteItem(@RequestBody Map<String,Object> param,HttpServletRequest request){
        String memberIdBySession = memberIdBySession(request);
        cartService.deleteOneItem(memberIdBySession,param);
        return 1;
    }



    private String memberIdBySession(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        return (String)session.getAttribute(MEMBER_LOGIN_SESSION);
    }
}
