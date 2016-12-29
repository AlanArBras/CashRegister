package net.huitel.cashregister.dao.beans;

/**
 * Created by Alan on 16/04/2016.
 * Represents a product placed into the basket
 */
public class BasketItem {
    private long id;
    private Product product;
    private int wantedQuantity;

    public BasketItem(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getWantedQuantity() {
        return wantedQuantity;
    }

    public void setWantedQuantity(int wantedQuantity) {
        this.wantedQuantity = wantedQuantity;
    }
}
