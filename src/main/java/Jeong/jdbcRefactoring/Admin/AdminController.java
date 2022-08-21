package Jeong.jdbcRefactoring.Admin;



import Jeong.jdbcRefactoring.Store.Order.OrderForm;
import Jeong.jdbcRefactoring.Store.form.Item;
import Jeong.jdbcRefactoring.Store.form.ItemForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional
@Controller
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/not_admin")
    public String notAdmin(){
        return "refactor/admin/not_admin";
    }

    @GetMapping("/admin/product-enroll")
    public String product(){
       return "refactor/admin/item-save-form";
    }

    @PostMapping("/admin/product-enroll")
    public String productPost(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {

        Item newItem = adminService.uploadFile(form);

        Integer itemId = adminService.enrollItem(newItem);
        Integer itemPageNum = adminService.getItemPageNum(itemId);
        redirectAttributes.addAttribute("pageNum", itemPageNum);
        redirectAttributes.addAttribute("itemId", itemId);
        return "redirect:/items/{pageNum}/{itemId}";
    }



    @GetMapping("/admin/product-edit/{itemId}")
    public String productEdit(@PathVariable Integer itemId,Model model){
        Item item = adminService.findByItemId(itemId);
        model.addAttribute("item",item);
        return "refactor/admin/item-edit-form";
    }

    @PostMapping("/admin/product-edit/{itemId}")
    public String productEdit(@PathVariable Integer itemId,@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {

        adminService.itemInfoEdit(form,itemId);

        log.info("path itemID !! ={}",itemId);
        Integer itemPageNum = adminService.getItemPageNum(itemId);
        redirectAttributes.addAttribute("pageNum", itemPageNum);
        redirectAttributes.addAttribute("itemId", itemId);
        return "redirect:/items/{pageNum}/{itemId}";
    }

    @ResponseBody
    @PostMapping("/admin/delete-product")
    public Integer productDelete(@RequestBody Map<String,Object> param){
        adminService.deleteItem(param);
        return 1;
    }



    @GetMapping("/admin/customer-order")
    public  String getAllOrders(Model model){
        List<OrderForm> orderFormList = adminService.getAllOrders();
        model.addAttribute("orders",orderFormList);
        return "refactor/admin/customer_order";
    }

    @ResponseBody
    @PostMapping("/admin/customer-order")
    public Integer editOrderStatus(@RequestBody Map<String, Object> param) {
        adminService.editOrderStatus(param);
        return 1;
    }

    @GetMapping("/admin/customer-order/search")
    public String searchOrder(HttpServletRequest request, Model model) {
        String category = request.getParameter("category");
        String keyword = request.getParameter("q");
        log.info("카테고리={}",category);
        log.info("keyword={}",keyword);
        List<OrderForm> searchResult = adminService.getSearchResult(category, keyword);
        model.addAttribute("orders",searchResult);
        model.addAttribute("search", true);
        return "refactor/admin/customer_order";
    }


}
