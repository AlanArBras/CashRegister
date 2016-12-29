package net.huitel.cashregister.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.huitel.cashregister.R;
import net.huitel.cashregister.dao.beans.Product;
import net.huitel.cashregister.ui.interfaces.IQuantityListener;

import java.util.List;

/**
 * Created by Alan on 09/04/2016.
 * Adapter responsible for providing views that represent Products
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {
    private List<Product> mProductList;
    private IQuantityListener mListener;

    /**
     * Adapter constructor
     *
     * @param productList list of products to be displayed in the RecyclerView
     */
    public ProductListAdapter(IQuantityListener listener, List<Product> productList) {
        mListener = listener;
        mProductList = productList;
    }

    @Override
    public ProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_list, parent, false);
        return new ProductListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductListViewHolder holder, int position) {
        Product currentProduct = mProductList.get(position);
        // get element from the product list at the current position
        // and fill the view's fields
        String productName = currentProduct.getName();
        holder.name.setText(productName);

        float price = currentProduct.getPrice();
        holder.price.setText(String.valueOf(price));

        int quantity = currentProduct.getQuantity();
        holder.quantity.setText(String.valueOf(quantity));

    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    // **********************************************************************
    // ***************************** ViewHolder *****************************
    // **********************************************************************
    public class ProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView price;
        public TextView quantity;

        public ProductListViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.ipl_tv_product_name);
            price = (TextView) itemView.findViewById(R.id.ipl_tv_product_price);
            quantity = (TextView) itemView.findViewById(R.id.ipl_tv_product_quantity_nb);
            itemView.setOnClickListener(this);
            itemView.setBackgroundResource(R.drawable.selector_white_to_grey);
        }

        @Override
        /**
         * When the user clicks on a product,
         * if this product is already in the basket table, then increment its wanted quantity
         * if it is not, insert the product into the basket table
         */
        public void onClick(View v) {
            mListener.onAddition(getAdapterPosition());
        }
    }

    // **********************************************************************
    // **********************************************************************
    // **********************************************************************
}
