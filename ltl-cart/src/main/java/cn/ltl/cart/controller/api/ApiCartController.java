package cn.ltl.cart.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.ltl.cart.pojo.Cart;
import cn.ltl.cart.service.CartService;

@RequestMapping("api/cart")
@Controller
public class ApiCartController {

    @Autowired
    private CartService cartService;

    /**
     * 对外提供接口服务，根据用户id查询购物车列表
     * 
     * @param userId
     * @return
     */
    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<Cart>> queryCartListByUserId(@PathVariable("userId") Long userId) {
        try {
            List<Cart> carts = this.cartService.queryCartlist(userId);
            if (null == carts || carts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(carts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
