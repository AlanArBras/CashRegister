package net.huitel.cashregister.ui.interfaces;

import net.huitel.cashregister.dao.beans.Product;

/**
 * Created by Alan on 16/04/2016.
 */
public interface IQuantityListener {

    /**
     * Called when the user confirms he wants to delete an item from his basket
     *
     * @param productId long, Item to be deleted's productId
     */
    void onRemoveRequired(long productId);

    /**
     * Called when the user wants to add a product into his basket
     *
     * @param position Position into the productList
     */
    void onAddition(int position);

    /**
     * Called when the user increases the wanted quantity of a BasketItem,
     * clicking on "+" in the BasketItem view
     * @param product
     */
    void onIncrementRequired(Product product);

    /**
     * Called when the user reduces the wanted quantity of a BasketItem,
     * clicking on "-" in the BasketItem view
     *
     * @param product
     */
    void onDecrementRequired(Product product);

}
