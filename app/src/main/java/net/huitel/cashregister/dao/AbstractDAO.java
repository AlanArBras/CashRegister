package net.huitel.cashregister.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.huitel.cashregister.managers.DatabaseManager;
import net.huitel.cashregister.managers.SqlHelper;

/**
 * Created by Alan on 09/04/2016.
 * Abstract class for DAO classes
 */
public abstract class AbstractDAO {

    protected SQLiteDatabase db;
    protected SqlHelper sqlHelper;
    /** The app Context, so DAOs can be used anywhere in the app*/
    protected Context mContext;


    public AbstractDAO(Context context) {
        this.mContext = context;
        sqlHelper = SqlHelper.getHelper(this.mContext);
        DatabaseManager.initializeInstance(sqlHelper);
    }

    /**
     * Opens the database
     */
    protected void open() {
        if (sqlHelper == null) {
            sqlHelper = SqlHelper.getHelper(mContext);
            DatabaseManager.initializeInstance(sqlHelper);
        }
        db = DatabaseManager.getInstance().openDatabase();
    }

    /**
     * Closes the database instance.
     */
    protected void close() {
        DatabaseManager.getInstance().closeDatabase();
    }

}
