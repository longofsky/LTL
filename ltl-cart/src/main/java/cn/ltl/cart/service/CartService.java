package cn.ltl.cart.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import cn.ltl.cart.bean.Item;
import cn.ltl.cart.mapper.CartMapper;
import cn.ltl.cart.pojo.Cart;
import cn.ltl.cart.threadlocal.UserThreadLocal;
import cn.ltl.sso.query.bean.User;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ItemService itemService;

    /**
     * 添加商品到购物车
     * 
     * 逻辑：判断加入的商品在原有购物车中是否存在，如果存在数量相加，如果不存在，直接写入即可
     * 
     * @param itemId
     */
    public void addItemToCart(Long itemId) {
        User user = UserThreadLocal.get();
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(user.getId());
        Cart cart = this.cartMapper.selectOne(record);
        if (null == cart) {
            // 不存在
            cart = new Cart();
            cart.setUserId(user.getId());
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            // 商品的基本数据需要通过后台系统查询
            Item item = this.itemService.queryById(itemId);
            cart.setItemId(itemId);
            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());
            cart.setItemImage(StringUtils.split(item.getImage(), ',')[0]);
            cart.setNum(1); // TODO

            // 保存到数据库
            this.cartMapper.insert(cart);
        } else {
            // 存在,数量相加，默认数量为1 TODO
            cart.setNum(cart.getNum() + 1);
            cart.setUpdated(new Date());
            this.cartMapper.updateByPrimaryKey(cart);
        }
    }

    public List<Cart> queryCartlist(Long userId) {
        Example example = new Example(Cart.class);

        // 设置排序条件
        example.setOrderByClause("created DESC");

        // 设置查询条件
        example.createCriteria().andEqualTo("userId", userId);

        return this.cartMapper.selectByExample(example);
    }

    public List<Cart> queryCartlist() {
        return this.queryCartlist(UserThreadLocal.get().getId());
    }

    public void updateNum(Long itemId, Integer num) {

        // 更新的数据
        Cart record = new Cart();
        record.setNum(num);
        record.setUpdated(new Date());

        // 更新的条件
        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("itemId", itemId)
                .andEqualTo("userId", UserThreadLocal.get().getId());

        this.cartMapper.updateByExampleSelective(record, example);
    }

    public void delete(Long itemId) {
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(UserThreadLocal.get().getId());
        this.cartMapper.delete(record);
    }

}
