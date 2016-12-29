package net.huitel.cashregister.ui.adapters;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import net.huitel.cashregister.R;
import net.huitel.cashregister.dao.beans.BasketItem;
import net.huitel.cashregister.dao.beans.Product;
import net.huitel.cashregister.ui.interfaces.IQuantityListener;
import net.huitel.cashregister.utils.AlertDialogBuilder;
import net.huitel.cashregister.utils.Basket;

/**
 * Created by Alan on 09/04/2016.
 * Adapter that manages the BasketList
 */
public class BasketListAdapter extends RecyclerView.Adapter<BasketListAdapter.BasketViewHolder> {
    /** List of all the items in the basket*/
    private Basket mBasket;
    /** Listener that will manage quantity's incrementation/decrementation */
    private IQuantityListener mListener;
    /** Context that will be needed to play animations*/
    private Context mContext;

    public BasketListAdapter(Context context, IQuantityListener listener, Basket basket) {
        mContext = context;
        mListener = listener;
        mBasket = basket;
    }

    @Override
    public BasketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_basket, parent, false);
        return new BasketViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(BasketViewHolder holder, int position) {
        holder.name.setText(mBasket.get(position).getProduct().getName());
        holder.price.setText(String.valueOf(mBasket.get(position).getProduct().getPrice()));
        holder.frontQuantity.setText(String.valueOf(mBasket.get(position).getWantedQuantity()));
        holder.backQuantity.setText(String.valueOf(mBasket.get(position).getWantedQuantity()));
    }

    @Override
    public int getItemCount() {
        return mBasket.size();
    }


    // **********************************************************************
    // ***************************** ViewHolder *****************************
    // **********************************************************************
    public class BasketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnFocusChangeListener{
        //ViewFlipper and animations
        final ViewFlipper flipper;
        AnimatorSet setRightOut;
        AnimatorSet setLeftIn;
        AnimatorSet setRightIn;
        AnimatorSet setLeftOut;
        //Front side
        LinearLayout front;
        private TextView name;
        private TextView price;
        private TextView frontQuantity;
        //Back side
        LinearLayout back;
        private TextView less;
        private TextView more;
        private TextView backQuantity;

        /**
         * Initializes all the holder's fields, making reference to the right views
         * @param itemView View inside the current ViewHolder
         */
        public BasketViewHolder(final View itemView) {
            super(itemView);
            //ViewFlipper and animations
            flipper = (ViewFlipper) itemView.findViewById(R.id.ib_view_flipper);
            setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.card_flip_right_out);
            setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.card_flip_left_in);
            setRightIn = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.card_flip_right_in);
            setLeftOut = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.card_flip_left_out);

            //Front side
            front = (LinearLayout) itemView.findViewById(R.id.item_basket_front);
            front.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.ibf_tv_product_name);
            price = (TextView) itemView.findViewById(R.id.ibf_tv_product_price);
            frontQuantity = (TextView) itemView.findViewById(R.id.ibf_tv_product_quantity_nb);

            //Back side
            back = (LinearLayout) itemView.findViewById(R.id.item_basket_back);
            back.setOnClickListener(this);
            less = (TextView) itemView.findViewById(R.id.ibb_tv_less);
            less.setOnClickListener(this);
            backQuantity = (TextView) itemView.findViewById(R.id.ibb_wanted_quantity);
            more = (TextView) itemView.findViewById(R.id.ibb_tv_more);
            more.setOnClickListener(this);
        }


        /**
         * Launches animations when the user clicks anywhere on the view.
         * Manages the wanted quantity when the user clicks on "+" or "-".
         * @param v View that is clicked on
         */
        @Override
        public void onClick(View v) {
            Product product = mBasket.get(getAdapterPosition()).getProduct();
            switch (v.getId()) {
                case R.id.ibb_tv_less:
                    if(mBasket.get(getAdapterPosition()).getWantedQuantity()==1)
                        showFrontSide();
                    mListener.onDecrementRequired(product);
                    break;
                case R.id.ibb_tv_more:
                    mListener.onIncrementRequired(product);
                    break;
                case R.id.item_basket_front:
                    showBackSide();
                    break;
                case R.id.item_basket_back:
                    showFrontSide();
                    break;
            }
            //Update the text fields
            updateQuantityTextView();
        }

        /**
         * Starts an animation and shows the back of the card
         */
        private void showBackSide() {
            setRightOut.setTarget(front);
            setRightIn.setTarget(back);
            setRightOut.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                /** We need to start the animations one after another to make the visual right,
                 *  so onAnimationEnd is the only method from the listener that we need */
                public void onAnimationEnd(Animator animation) {
                    flipper.setDisplayedChild(1);
                    setRightIn.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            setRightOut.start();
        }


        /**
         * Starts an animation and shows the front of the card
         */
        private void showFrontSide() {
            setLeftOut.setTarget(back);
            setLeftIn.setTarget(front);
            setLeftOut.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    flipper.setDisplayedChild(0);
                    setLeftIn.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            setLeftOut.start();
        }

        /**
         * If the BasketItem showed by the holder is still in the list, refreshes the wanted quantity value
         */
        private void updateQuantityTextView() {
            if (getAdapterPosition()>=0 && getAdapterPosition() <= mBasket.size() - 1) {
                BasketItem item = mBasket.get(getAdapterPosition());
                frontQuantity.setText(String.valueOf(item.getWantedQuantity()));
                backQuantity.setText(String.valueOf(item.getWantedQuantity()));
            }
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(v.getId()==R.id.item_basket_back && !hasFocus){
                showFrontSide();
            }
        }
    }

    // **********************************************************************
    // **********************************************************************
    // **********************************************************************
}
