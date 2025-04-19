package org.csu.gameshopms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.gameshopms.entity.Cart;
import org.csu.gameshopms.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class CartService {
    @Autowired
    private CartMapper cartMapper;

    /**
     * 添加商品到购物车：若已存在记录则 ++addCount、更新 price 并设 in_cart=1；
     * 否则新插入一条记录，in_cart=1、add_count=1、is_selected=1。
     */
    public void addToCart(int userId, int itemId, int editionId, double price) {
        QueryWrapper<Cart> qw = new QueryWrapper<>();
        qw.eq("user_id", userId)
                .eq("item_id", itemId)
                .eq("edition_id", editionId);

        Cart existing = cartMapper.selectOne(qw);
        if (existing != null) {
            existing.setIn_cart(1);
            existing.setAdd_count(existing.getAdd_count() + 1);
            existing.setPrice(price);
            cartMapper.updateById(existing);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(itemId);
            cart.setEditionId(editionId);
            cart.setPrice(price);
            cart.setIn_cart(1);
            cart.setAdd_count(1);
            cart.setIs_selected(1);
            cartMapper.insert(cart);
        }
    }
}
