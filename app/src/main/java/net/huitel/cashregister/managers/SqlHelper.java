package net.huitel.cashregister.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.huitel.cashregister.dao.BasketDAO;
import net.huitel.cashregister.dao.ProductsDAO;

/**
 * Created by Alan on 09/04/2016.
 * Manages the SQLite database (creation, upgrade and deletion)
 */
public class SqlHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "cashregister_database";

    private static SqlHelper instance;

    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * @param context Context
     * @return A unique instance of SqlHelper
     */
    public static synchronized SqlHelper getHelper(Context context) {
        if (instance == null)
            instance = new SqlHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        dropIfExists(db);
        createDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    /**
     * Creates all the tables the app needs at once in the given database
     * @param database SQLiteDatabase
     */
    private void createDatabase(SQLiteDatabase database) {
        database.execSQL(ProductsDAO.CREATE_TABLE);
        database.execSQL(BasketDAO.CREATE_TABLE);
    }

    /**
     * Drops all the tables still available in the given database
     * @param database SQLiteDatabase
     */
    private void dropIfExists(SQLiteDatabase database){
        database.execSQL("DROP TABLE IF EXISTS " + ProductsDAO.PRODUCT_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + BasketDAO.BASKET_TABLE);
    }
}
