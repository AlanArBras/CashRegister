package net.huitel.cashregister.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.huitel.cashregister.MainApp;
import net.huitel.cashregister.R;
import net.huitel.cashregister.dao.BasketDAO;
import net.huitel.cashregister.dao.ProductsDAO;
import net.huitel.cashregister.dao.beans.BasketItem;
import net.huitel.cashregister.dao.beans.Product;
import net.huitel.cashregister.ui.adapters.BasketListAdapter;
import net.huitel.cashregister.ui.adapters.ProductListAdapter;
import net.huitel.cashregister.ui.customs.DefaultFragment;
import net.huitel.cashregister.ui.interfaces.IQuantityListener;
import net.huitel.cashregister.utils.AlertDialogBuilder;
import net.huitel.cashregister.utils.Basket;

import java.util.ArrayList;

/**
 * Created by Alan on 09/04/2016.
 * Fragment that contains;
 */
public class HomeFragment extends DefaultFragment implements IQuantityListener {
    private static final String TAG = HomeFragment.class.getName();
    private ArrayList<Product> mProducts;
    private Basket mBasket;
    private ProductListAdapter mProductListAdapter;
    private BasketListAdapter mBasketListAdapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

    @Override
    public void setView() {
        View v = getView();
        if (v != null) {
            initializeProducts(v);
            initializeBasket(v);
        }
    }

    /**
     * @param v The HomeFragment's top View
     */
    private void initializeProducts(View v) {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.hpl_rv_product_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        ProductsDAO productsDAO = new ProductsDAO(MainApp.getContext());
        mProducts = productsDAO.getProducts();

        mProductListAdapter = new ProductListAdapter(this, mProducts);
        recyclerView.setAdapter(mProductListAdapter);
    }


    /**
     * @param v
     */
    private void initializeBasket(View v) {
        RecyclerView basketRecyclerView = (RecyclerView) v.findViewById(R.id.hbl_rv_basket_product_list);
        basketRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.basket_column_count));
        basketRecyclerView.setLayoutManager(layoutManager);

        BasketDAO basketDAO = new BasketDAO(MainApp.getContext());
        mBasket = new Basket(basketDAO.getBasketItems());

        mBasketListAdapter = new BasketListAdapter(getContext(), this, mBasket);
        basketRecyclerView.setAdapter(mBasketListAdapter);
    }


    @Override
    public void onAddition(int position) {
        BasketDAO basketDAO = new BasketDAO(MainApp.getContext());
        Product product = mProducts.get(position);
        if (!mBasket.isEmpty() && mBasket.contains(product)) {
            //As mBasket contains the product, basketItem cannot be null
            BasketItem basketItem = mBasket.get(product.getProductId());
            increment(basketItem);
        } else {
            BasketItem item = new BasketItem();
            item.setProduct(product);
            item.setWantedQuantity(1);
            basketDAO.insert(item);
            mBasket.add(item);
        }
        mBasketListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onIncrementRequired(Product product) {
        BasketItem item = mBasket.get(product.getProductId());
        increment(item);
    }

    private void increment(BasketItem basketItem) {
        ProductsDAO productsDAO = new ProductsDAO(MainApp.getContext());
        Product product = productsDAO.getProduct(basketItem.getProduct().getProductId());
        if(basketItem.getWantedQuantity() < product.getQuantity()) {
            basketItem.setWantedQuantity(basketItem.getWantedQuantity() + 1);

            BasketDAO basketDAO = new BasketDAO(MainApp.getContext());
            basketDAO.incrementQuantity(basketItem.getProduct().getProductId());
        } else {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            AlertDialogBuilder dialog = AlertDialogBuilder.getMaxQuantityWarning();
            dialog.setCancelable(true);
            dialog.show(fragmentManager, "Max quantity");
        }
    }


    @Override
    public void onDecrementRequired(Product product) {
        BasketItem item = mBasket.get(product.getProductId());
        if(item.getWantedQuantity() == 1){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            AlertDialogBuilder dialog = AlertDialogBuilder.getDeleteConfirmation(this, item.getProduct().getProductId());
            dialog.setCancelable(false);
            dialog.show(fragmentManager, "Delete confirmation");
        } else{
            decrement(item);
        }
    }

    private void decrement(BasketItem basketItem) {
        int wantedQuantity = basketItem.getWantedQuantity();
        BasketDAO basketDAO = new BasketDAO(MainApp.getContext());
        if (wantedQuantity > 1) {
            basketItem.setWantedQuantity(wantedQuantity - 1);
            basketDAO.decrementQuantity(basketItem.getProduct().getProductId());
            mBasketListAdapter.notifyDataSetChanged();
        } else {
            remove(basketItem);
        }
    }

    @Override
    public void onRemoveRequired(long productId) {
        BasketItem basketItem = mBasket.get(productId);
        remove(basketItem);
    }

    /**
     * Removes an item from the basket table and from mBasket
     * @param basketItem
     */
    private void remove(BasketItem basketItem){
        BasketDAO basketDAO = new BasketDAO(MainApp.getContext());
        basketDAO.delete(basketItem);
        mBasket.remove(basketItem);
        mBasketListAdapter.notifyDataSetChanged();
    }
}
