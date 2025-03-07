package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ShoppingCartService {
    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查询当前用户全部购物车内容
     * @return
     */
    List<ShoppingCart> showShoppingCart();

    /**
     * 清空购物车
     */
    void cleanSHoppingCart();

    /**
     * 删除购物车菜品
     * @param shoppingCartDTO
     */
    void sub(ShoppingCartDTO shoppingCartDTO);
}
