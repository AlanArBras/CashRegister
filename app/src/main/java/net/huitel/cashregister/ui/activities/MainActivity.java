package net.huitel.cashregister.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.huitel.cashregister.MainApp;
import net.huitel.cashregister.R;
import net.huitel.cashregister.dao.BasketDAO;
import net.huitel.cashregister.dao.ProductsDAO;
import net.huitel.cashregister.dao.beans.Product;
import net.huitel.cashregister.ui.fragments.HomeFragment;
import net.huitel.cashregister.ui.interfaces.IFragmentInteractionListener;


public class MainActivity extends ActionBarActivity implements IFragmentInteractionListener {
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        //TODO Acquire the list from a webservice and update the database when needed
        getProducts();

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.container, new HomeFragment())
                    .commit();
        }
    }

    /**
     * Fills the database with products if it is empty
     */
    private void getProducts(){
        ProductsDAO productsDAO = new ProductsDAO(MainApp.getContext());
        if (productsDAO.isEmpty()) {
            Product product = new Product();
            product.setProductId(1);
            product.setName("Eau de javel");
            product.setPrice(5);
            product.setQuantity(3);
            productsDAO.insert(product);

            Product product2 = new Product();
            product2.setProductId(322);
            product2.setName("Ananas");
            product2.setPrice(4);
            product2.setQuantity(10);
            productsDAO.insert(product2);

            Product product3 = new Product();
            product3.setProductId(49999);
            product3.setName("Yaourt aux fruits");
            product3.setPrice(2.50f);
            product3.setQuantity(20);
            productsDAO.insert(product3);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChangeFragment(Fragment fragment, String TAG) {
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(TAG)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BasketDAO basketDAO = new BasketDAO(MainApp.getContext());
        basketDAO.deleteBasket();
    }
}
