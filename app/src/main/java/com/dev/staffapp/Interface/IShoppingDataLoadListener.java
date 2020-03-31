package com.dev.staffapp.Interface;

import com.dev.staffapp.Model.ShoppingItem;

import java.util.List;

public interface IShoppingDataLoadListener {
        void onShoppingDataLoadSuccess(List<ShoppingItem> shoppingItemList);
        void onShoppingDataLoadFailed(String message);
}
