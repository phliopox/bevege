package Jeong.jdbcRefactoring;

import Jeong.jdbcRefactoring.Store.Cart.MyPageForm;
import Jeong.jdbcRefactoring.Store.Cart.CartService;
import Jeong.jdbcRefactoring.Store.Item.ItemService;
import Jeong.jdbcRefactoring.Store.form.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

import static Jeong.jdbcRefactoring.SessionConst.MEMBER_LOGIN_SESSION;

@Slf4j
@Controller
public class HomeController {

    private final CartService cartService;
    private  final ItemService itemService;
    public HomeController(CartService cartService, ItemService itemService) {
        this.cartService = cartService;
        this.itemService = itemService;
    }

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model){
        List<Item> items = itemService.NewItemInfo();
        model.addAttribute("newItems",items);

        HttpSession session = request.getSession(false);
        if (session == null) {
            return "refactor/home";
        }

        String memberId = (String) session.getAttribute(MEMBER_LOGIN_SESSION);

        if(memberId ==null){
            return "refactor/home";
        }

        if(memberId.equals("admin")){
            model.addAttribute("ADMIN",true);
        }


        MyPageForm myPageForm = cartService.CreateMyPageForm(memberId);
        model.addAttribute("memberId",memberId);
        model.addAttribute("myPageForm",myPageForm);


        return "refactor/login/loginHome";
    }


}

