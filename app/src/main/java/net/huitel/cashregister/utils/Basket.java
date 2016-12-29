package net.huitel.cashregister.utils;

import net.huitel.cashregister.dao.beans.BasketItem;
import net.huitel.cashregister.dao.beans.Product;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Alan on 20/04/2016.
 * Class that represents a basket. It only contains an ArrayList.
 */
public class Basket {
    private ArrayList<BasketItem> mBasket;

    /**
     * Basic constructor
     */
    public Basket() {
        mBasket = new ArrayList<>();
    }

    /**
     * Create a Basket from an ArrayList
     */
    public Basket(ArrayList<BasketItem> basket) {
        mBasket = basket;
    }

    /**
     * mBasket getter
     */
    public ArrayList<BasketItem> getBasketList() {
        return mBasket;
    }

    /**
     * mBasket setter
     */
    public void setBasket(ArrayList<BasketItem> basket) {
        mBasket = basket;
    }

    /**
     * @param item BasketItem to be added to the Basket
     */
    public void add(BasketItem item) {
        mBasket.add(item);
    }

    /**
     * @param pos int
     * @return BasketItem at the pos position in the ArrayList
     */
    public BasketItem get(int pos) {
        return mBasket.get(pos);
    }

    /**
     * @return Number of elements in mBasket
     */
    public int size() {
        return mBasket.size();
    }


    /**
     * @return true if mBasket has no elements, false otherwise.
     */
    public boolean isEmpty() {
        return mBasket.isEmpty();
    }

    /**
     * @param productId long
     * @return BasketItem corresponding to the given product, null if there is no such BasketItem
     */
    public BasketItem get(long productId) {
        Iterator<BasketItem> it = mBasket.iterator();
        BasketItem current = null;
        boolean found = false;
        while (it.hasNext() && !found) {
            current = it.next();
            if (productId == current.getProduct().getProductId()) found = true;
        }
        return current;
    }

    /**
     * @param product Product to find into the basket
     * @return true if the given product is inside the basket already, false otherwise
     */
    public boolean contains(Product product) {
        boolean found = false;
        Iterator<BasketItem> it = mBasket.iterator();
        BasketItem current;
        while (it.hasNext() && !found) {
            current = it.next();
            if (product.getProductId() == current.getProduct().getProductId()) found = true;
        }
        return found;
    }

    public void remove(BasketItem basketItem){
        Iterator<BasketItem> it = mBasket.iterator();
        BasketItem current;
        boolean removed = false;
        while (it.hasNext() && !removed){
            current = it.next();
            if(basketItem.getId() == current.getId()) {
                it.remove();
                removed = true;
            }
        }
    }

}
