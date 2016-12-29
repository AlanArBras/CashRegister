package net.huitel.cashregister.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import net.huitel.cashregister.dao.beans.Product;

import java.util.ArrayList;

/**
 * Created by Alan on 09/04/2016.
 * Data Access Object to reach the products table
 */
public class ProductsDAO extends AbstractDAO {

    public static final String PRODUCT_TABLE = "products";
    public static final String ID = "id";
    public static final String PRODUCT_ID = "productid";
    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String QUANTITY = "quantity";

    public static final String CREATE_TABLE = "CREATE TABLE " + PRODUCT_TABLE + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PRODUCT_ID + " INTEGER, " +
            NAME + " TEXT, " +
            PRICE + " REAL, " +
            QUANTITY + " INTEGER" +
            ");";

    /**
     * A String table containing the names of all the columns in the table.
     * It will be used for sending requests to the database.
     */
    public static String[] columns = new String[]{ID, PRODUCT_ID, NAME, PRICE, QUANTITY};

    /**
     * Constructor of a DAO to access products table
     *
     * @param context App context
     * @see AbstractDAO#AbstractDAO(android.content.Context)
     */
    public ProductsDAO(Context context) {
        super(context);
    }


    public void insert(Product product) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_ID, product.getProductId());
        contentValues.put(NAME, product.getName());
        contentValues.put(PRICE, product.getPrice());
        contentValues.put(QUANTITY, product.getQuantity());

        long id = db.insert(PRODUCT_TABLE, null, contentValues);
        product.setId(id);

        close();
    }

    public boolean isEmpty() {
        open();
        Cursor cursor = this.db.query(PRODUCT_TABLE, columns, null, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        close();
        return count == 0;
    }

    /**
     * @return ArrayList of Product objects containing all the products in the table "products"
     */
    public ArrayList<Product> getProducts() {
        open();
        ArrayList<Product> stock = new ArrayList<>();
        Product product;
        Cursor cursor = this.db.query(PRODUCT_TABLE, columns, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() >= 1) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    product = cursorToProduct(cursor);
                    stock.add(product);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        close();
        return stock;
    }

    public Product getProduct(long productId) {
        open();
        Product product = new Product();
        Cursor cursor = this.db.query(PRODUCT_TABLE, columns, PRODUCT_ID + " =? ", new String[]{String.valueOf(productId)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            product = cursorToProduct(cursor);
            cursor.close();
        }
        close();
        return product;
    }


    /**
     * Converts a Cursor object into a Product object.
     *
     * @param cursor Cursor to convert
     * @return Product initialized with the given Cursor's content
     */
    public static Product cursorToProduct(Cursor cursor) {
        Product product = new Product();
        product.setId(cursor.getLong(0));
        product.setProductId(cursor.getLong(1));
        product.setName(cursor.getString(2));
        product.setPrice(cursor.getFloat(3));
        product.setQuantity(cursor.getInt(4));
        return product;
    }
}
