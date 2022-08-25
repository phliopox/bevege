package Jeong.jdbcRefactoring.Store.Item;

import Jeong.jdbcRefactoring.Board.Page.Criteria;
import Jeong.jdbcRefactoring.Store.ItemFileStore;
import Jeong.jdbcRefactoring.Store.Order.OrderForm;
import Jeong.jdbcRefactoring.Store.Order.OrderService;
import Jeong.jdbcRefactoring.Store.form.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.util.List;

import static Jeong.jdbcRefactoring.SessionConst.MEMBER_LOGIN_SESSION;


@Slf4j
@Controller
public class ItemController {

    private final ItemFileStore fileStore;
    private final ItemService itemService;
    private final OrderService orderService;

    public ItemController(ItemFileStore fileStore, ItemService itemService, OrderService orderService) {
        this.fileStore = fileStore;
        this.itemService=itemService;

        this.orderService = orderService;
    }


    @GetMapping("/store")
    public String storeHome(@RequestParam(required = false,value="pageIndex") String param, Model model){
       Integer pageNum;
       Criteria criteria;
       if(param==null){
           pageNum=1;
           criteria=Criteria.changePageNum(pageNum,8);
       }else{
           pageNum=Integer.parseInt(param);
           criteria=Criteria.changePageNum(pageNum,8);
       }

        model.addAttribute("pageList",itemService.page(criteria));
        model.addAttribute("items",itemService.itemList(criteria));
        return "refactor/store/store";
    }



    @GetMapping("/items/{pageNum}/{id}")
    public String items(@PathVariable Integer pageNum,@PathVariable Integer id, Model model,HttpServletRequest request){
        Item item=itemService.findByItemId(id);
        model.addAttribute("item", item);
        model.addAttribute("pageNum",pageNum);

        /*주문고객 확인 -> 리뷰작성버튼 노출*/
        String memberIdBySession = getMemberIdBySession(request);
        if(memberIdBySession!=null){
            List<OrderForm> myOrderInfo = orderService.getMyOrderInfo(memberIdBySession);
            if(myOrderInfo!=null){
            for (OrderForm of : myOrderInfo) {
                if (of.getItemId() == id) {
                    model.addAttribute("orderedMember", true);
                }
            }            }
        }

        List<Item> items = itemService.NewItemInfo();
        model.addAttribute("newItems",items);
        return "refactor/store/item-view";
    }


    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:"+fileStore.getFullPathToItem(filename));
    }

    /*검색 url*/
    @GetMapping("/items/search")
    public String searchItem(HttpServletRequest request,Model model) {

        String keyword = request.getParameter("q");
        model.addAttribute("items",
                itemService.searchItem(keyword));

        model.addAttribute("search",true);

        return "refactor/store/store";
    }


    private String getMemberIdBySession(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        return session!=null?(String) session.getAttribute(MEMBER_LOGIN_SESSION):null;
    }
}
