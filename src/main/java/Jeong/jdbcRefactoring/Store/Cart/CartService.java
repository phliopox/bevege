package Jeong.jdbcRefactoring.Store.Cart;

import Jeong.jdbcRefactoring.Store.form.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public MyPageForm CreateMyPageForm(String memberId){

        MyPageForm myPageForm;

        Map map = cartRepository.pointAndMoney(memberId);
        Integer cartId = cartRepository.checkHasCart(memberId);

        if (cartId == null) {
            myPageForm=new MyPageForm(memberId,
                    0,
                    Integer.parseInt(String.valueOf(map.get("point"))),
                    Integer.parseInt(String.valueOf(map.get("money"))));
        }else {
            Integer cartItemAmount = cartRepository.getAllItemAmount(cartId);

            myPageForm = new MyPageForm(
                    memberId,
                    cartItemAmount,
                    Integer.parseInt(String.valueOf(map.get("point"))),
                    Integer.parseInt(String.valueOf(map.get("money"))));
        }
        return myPageForm;

    }

    public void addItemToCart(String memberId,Map<String,Object> cartItem){
        cartRepository.addItemToCart(memberId,cartItem);
    }

    public void editItemAmount(String memberId, Map<String,Object> param){
        int editAmount = Integer.parseInt(String.valueOf(param.get("editAmount")));
        int itemId = Integer.parseInt(String.valueOf(param.get("itemId")));
        cartRepository.updateItemAmount(editAmount,itemId,memberId);
    }
    public List<Item> cartItemByMemberId(String memberId){
        List<Item> itemList = cartRepository.cartItem(memberId);
        return itemList;
    }

    public void deleteOneItem(String memberId, Map<String,Object> param){
        cartRepository.deleteItem(memberId, Integer.parseInt(String.valueOf(param.get("itemId"))));
    }
    public void deleteOneItem(String memberId, Integer itemId){
        cartRepository.deleteItem(memberId, itemId);
    }

}
