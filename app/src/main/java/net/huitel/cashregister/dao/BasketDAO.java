package net.huitel.cashregister.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import net.huitel.cashregister.MainApp;
import net.huitel.cashregister.dao.beans.BasketItem;
import net.huitel.cashregister.dao.beans.Product;

import java.util.ArrayList;

/**
 * Created by Alan on 09/04/2016.
 * Data Access Object to reach the basket table
 */
public class BasketDAO extends AbstractDAO {
    public static final String BASKET_TABLE = "basket";
    public static final String ID = "id";
    public static final String PRODUCT_ID = "product_id";
    public static final String BASKET_QUANTITY = "basket_quantity";

    /**
     * Query to create the basket table
     */
    public static final String CREATE_TABLE = "CREATE TABLE " + BASKET_TABLE + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PRODUCT_ID + " INTEGER, " +
            BASKET_QUANTITY + " INTEGER" +
            ");";

    /**
     * A String table containing the names of all the columns in the table.
     * It will be used for sending requests to the database.
     */
    public static String[] columns = new String[]{ID, PRODUCT_ID, BASKET_QUANTITY};

    /**
     * @return true if there is no element into the basket table, false otherwise
     */
    public boolean isEmpty() {
        open();
        Cursor cursor = this.db.query(BASKET_TABLE, columns, null, null, null, null, null);
        cursor.close();
        close();
        return cursor.getCount() == 0;
    }

    /**
     * Constructor of a DAO to access basket table
     *
     * @param context App context
     * @see AbstractDAO#AbstractDAO(Context)
     */
    public BasketDAO(Context context) {
        super(context);
    }

    public void insert(BasketItem basketItem) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_ID, basketItem.getProduct().getProductId());
        contentValues.put(BASKET_QUANTITY, basketItem.getWantedQuantity());

        long id = db.insert(BASKET_TABLE, null, contentValues);
        basketItem.setId(id);

        close();
    }

    /**
     * @param basketItem BasketItem to remove from the basket table
     */
    public void delete(BasketItem basketItem) {
        open();
        db.delete(BASKET_TABLE, PRODUCT_ID + " = ?", new String[]{String.valueOf(basketItem.getProduct().getProductId())});
        close();
    }


    /**
     * @return ArrayList of Product objects containing all the products in database
     */
    public ArrayList<BasketItem> getBasketItems() {
        open();
        ArrayList<BasketItem> basket = new ArrayList<>();
        BasketItem basketItem;
        Cursor cursor = this.db.query(BASKET_TABLE, columns, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() >= 1) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    basketItem = cursorToBasketItem(cursor);
                    basket.add(basketItem);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        close();
        return basket;
    }

    /**
     * @param productId long
     * @return BasketItem found in database with the right productId
     */
    public BasketItem getBasketItemByProductId(long productId) {
        open();
        BasketItem basketItem = null;
        Cursor cursor = this.db.query(BASKET_TABLE, columns, PRODUCT_ID + " =? ", new String[]{String.valueOf(productId)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            basketItem = cursorToBasketItem(cursor);
            cursor.close();
        }
        close();
        return basketItem;
    }

    /**
     * Converts a Cursor object into a BasketItem object.
     * The methode searches into the products table to define the BasketItem's Product field
     *
     * @param cursor Cursor to convert
     * @return BasketItem initialized with the given Cursor's content
     */
    public static BasketItem cursorToBasketItem(Cursor cursor) {
        BasketItem basketItem = new BasketItem();
        basketItem.setId(cursor.getLong(0));
        ProductsDAO productsDAO = new ProductsDAO(MainApp.getContext());
        basketItem.setProduct(productsDAO.getProduct(cursor.getLong(1)));
        basketItem.setWantedQuantity(cursor.getInt(2));
        return basketItem;
    }

    /**
     * Increments a product's quantity
     *
     * @param productId Product's id
     */
    public void incrementQuantity(long productId) {
        open();
        int oldQuantity = getBasketItemByProductId(productId).getWantedQuantity();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BASKET_QUANTITY, ++oldQuantity);
        this.db.update(BASKET_TABLE, contentValues, PRODUCT_ID + " =? ", new String[]{String.valueOf(productId)});
        close();
    }

    /**
     * Decrements a product's quantity
     *
     * @param productId Product's id
     */
    public void decrementQuantity(long productId) {
        open();
        int oldQuantity = getBasketItemByProductId(productId).getWantedQuantity();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BASKET_QUANTITY, --oldQuantity);
        this.db.update(BASKET_TABLE, contentValues, PRODUCT_ID + " =? ", new String[]{String.valueOf(productId)});
        close();
    }

    /**
     * Deletes all rows in the table
     */
    public void deleteBasket() {
        open();
        this.db.delete(BASKET_TABLE, null, null);
        close();
    }

}
