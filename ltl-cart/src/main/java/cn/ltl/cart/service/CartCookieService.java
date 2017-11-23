package cn.ltl.cart.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.ltl.cart.bean.Item;
import cn.ltl.cart.pojo.Cart;
import cn.ltl.common.utils.CookieUtils;

@Service
public class CartCookieService {

    private static final String COOKIE_NAME = "TT_CART";

    private static final Integer COOKIE_TIME = 60 * 60 * 24 * 30 * 12;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private ItemService itemService;

    /**
     * 添加商品到购物车
     * 
     * 逻辑：判断加入的商品在原有购物车中是否存在，如果存在数量相加，如果不存在，直接写入即可
     * 
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * 
     */
    public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Cart> carts = this.queryCartlist(request);

        Cart cart = null;
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                // 该商品已经存在购物车中
                cart = c;
                break;
            }
        }

        if (null == cart) {
            // 不存在
            cart = new Cart();
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            // 商品的基本数据需要通过后台系统查询
            Item item = this.itemService.queryById(itemId);
            cart.setItemId(itemId);
            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());
            cart.setItemImage(StringUtils.split(item.getImage(), ',')[0]);
            cart.setNum(1); // TODO

            // 商品加入购物车列表中
            carts.add(cart);
        } else {
            // 存在
            cart.setNum(cart.getNum() + 1); // TODO 默认为1
            cart.setUpdated(new Date());
        }

        // 将购物车列表数据写入到cookie中
        CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(carts), COOKIE_TIME,
                true);
    }

    public List<Cart> queryCartlist(HttpServletRequest request) throws Exception {
        String jsonData = CookieUtils.getCookieValue(request, COOKIE_NAME, true);
        List<Cart> carts = null;
        if (StringUtils.isEmpty(jsonData)) {
            carts = new ArrayList<Cart>();
        } else {
            // 反序列化
            carts = MAPPER.readValue(jsonData,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        }
        // TODO 排序
        return carts;
    }

    public void updateNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Cart> carts = this.queryCartlist(request);

        Cart cart = null;
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                // 该商品已经存在购物车中
                cart = c;
                break;
            }
        }

        if (cart != null) {
            cart.setNum(num);
            cart.setUpdated(new Date());
        } else {
            // 参数非法
            return;
        }

        // 将购物车列表数据写入到cookie中
        CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(carts), COOKIE_TIME,
                true);
    }

    public void delete(Long itemId, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Cart> carts = this.queryCartlist(request);

        Cart cart = null;
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                cart = c;
                carts.remove(c);
                break;
            }
        }

        if (cart == null) {
            return;
        }

        // 将购物车列表数据写入到cookie中
        CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(carts), COOKIE_TIME,
                true);
    }

}
