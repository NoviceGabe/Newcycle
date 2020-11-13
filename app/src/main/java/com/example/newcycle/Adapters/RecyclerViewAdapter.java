package com.example.newcycle.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.newcycle.Activities.CartActivity;
import com.example.newcycle.Activities.ProductViewActivity;
import com.example.newcycle.DB.AppDatabase;
import com.example.newcycle.DB.DbHelper;
import com.example.newcycle.Interface.OnReadClient;
import com.example.newcycle.Interface.OnWriteClient;
import com.example.newcycle.Model.Cart;
import com.example.newcycle.Model.Data;
import com.example.newcycle.Model.Product;
import com.example.newcycle.Model.Total;
import com.example.newcycle.R;
import com.example.newcycle.Utils.User;
import com.example.newcycle.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.newcycle.Utils.Utility.URL;
import static com.example.newcycle.Utils.Utility.currencyFormatter;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> {
    private Context context;
    private List<Product> items;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private SparseBooleanArray selectedItems;
    public static boolean isPressed = false;
    private AppDatabase appDb;
    private int viewType;
    private TextView ttotal;

    public RecyclerViewAdapter(Context context, List<Product> items, int viewType){
        this.context = context;
        this.items = items;
        this.viewType = viewType;
        selectedItems = new SparseBooleanArray();
        appDb = AppDatabase.getAppDatabase(context);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM && this.viewType == 1){
            View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,  parent, false);
            return new ProductViewHolder(view);
        }else if(viewType == VIEW_TYPE_ITEM && this.viewType == 2){
            View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_cart,  parent, false);
            return new CartViewHolder(view);
        }else if(viewType == VIEW_TYPE_ITEM && this.viewType == 3){
            View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_checkout,  parent, false);
            return new CheckoutViewHolder(view);
        }else if(viewType == VIEW_TYPE_ITEM && this.viewType == 4){
            View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_order,  parent, false);
            return new OrderViewHolder(view);
        }else if(viewType == VIEW_TYPE_ITEM && this.viewType == 5){
            View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_favorite,  parent, false);
            return new FavoriteViewHolder(view);
        }else{
            View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,  parent, false);
            return new ProgressViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if(holder instanceof ProductViewHolder){
            populateProductRows((ProductViewHolder) holder, position);
        }else if(holder instanceof  CartViewHolder){
            populateCartRows((CartViewHolder) holder, position);
        }else if(holder instanceof  CheckoutViewHolder){
            populateCheckoutRows((CheckoutViewHolder) holder, position);
        }else if(holder instanceof  OrderViewHolder){
            populateOrderRows((OrderViewHolder) holder, position);
        }else if(holder instanceof  FavoriteViewHolder){
            populateFavoriteRows((FavoriteViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public Product getItem(int pos){
        return items.get(pos);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    public class ProductViewHolder extends CustomViewHolder{
        private ImageView image;
        private TextView name, price, description;
        private RatingBar ratingBar;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            description = itemView.findViewById(R.id.description);
            ratingBar = itemView.findViewById(R.id.rating_bar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        ProductViewActivity.setProduct((Product)items.get(pos));
                        Intent intent = new Intent(context, ProductViewActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public class CartViewHolder extends CustomViewHolder{
        private ImageView image;
        private TextView name, price, total;
        private ElegantNumberButton quantity;
        private CheckBox checkBox;
        private CardView row;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            checkBox = itemView.findViewById(R.id.checkbox);
            total = itemView.findViewById(R.id.total);
            row = itemView.findViewById(R.id.row);
        }
    }
    
    public class CheckoutViewHolder extends CustomViewHolder{
        private ImageView image;
        private TextView name, price, quantity, packageCount;
        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            packageCount = itemView.findViewById(R.id.package_count);
        }
    }

    public class OrderViewHolder extends CustomViewHolder{
        private ImageView image;
        private TextView name, price, quantity, orderNo, date, paymentMethod, orderStatus, action;
        private RatingBar ratingBar;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            orderNo = itemView.findViewById(R.id.order_no);
            date = itemView.findViewById(R.id.date);
            paymentMethod = itemView.findViewById(R.id.payment_method);
            orderStatus = itemView.findViewById(R.id.order_status);
            action = itemView.findViewById(R.id.action);
            ratingBar = itemView.findViewById(R.id.rating_bar);
        }
    }

    public class FavoriteViewHolder extends CustomViewHolder{
        private ImageView image, favorite;
        private TextView name, price;
        private RatingBar ratingBar;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            favorite = itemView.findViewById(R.id.favorite);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        ProductViewActivity.setProduct((Product)items.get(pos));
                        Intent intent = new Intent(context, ProductViewActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public class ProgressViewHolder extends CustomViewHolder{
        public ProgressBar progressBar;
        public ProgressViewHolder(@NonNull View itemView){
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_circular);
        }
    }

    private void populateProductRows(final ProductViewHolder viewHolder, final int position){
        final Product currentItem = (Product) getItem(position);
        String price = "\u20B10.00";
        viewHolder.name.setText(currentItem.getName());

        if(currentItem.getPrice() > 0){
            price = "\u20B1"+currencyFormatter(currentItem.getPrice());
        }
        viewHolder.price.setText(price);
        if(viewHolder.description != null){
            viewHolder.description.setText(currentItem.getDescription());
        }

        if(viewHolder.ratingBar != null){
            viewHolder.ratingBar.setRating(currentItem.getRating());
        }

        if(viewHolder.image != null && !currentItem.getImage().isEmpty()){
            Picasso.get().load(currentItem.getImage())
                    .error(R.drawable.dummy_image)
                    .placeholder(R.drawable.progress_animation)
                    .into(viewHolder.image);
        }else{
            Picasso.get().load(R.drawable.dummy_image)
                    .placeholder(R.drawable.progress_animation)
                    .into(viewHolder.image);
        }
    }

    public void populateCartRows(final CartViewHolder viewHolder, final int position){
        User user = User.getInstance();
        user.init(context);
        final Data data = user.loadDataFromSession();
        final Product currentItem = (Product) getItem(position);
        String price = "\u20B10.00";
        currentItem.setPosition(position);
        viewHolder.name.setText(currentItem.getName());

        if(currentItem.getPrice() > 0){
            price = "\u20B1"+currencyFormatter(currentItem.getPrice());
        }
        viewHolder.price.setText(price);

        if(viewHolder.image != null && !currentItem.getImage().isEmpty()){
            Picasso.get().load(currentItem.getImage())
                    .error(R.drawable.dummy_image)
                    .into(viewHolder.image);
        }else{
            Picasso.get().load(R.drawable.dummy_image)
                    .into(viewHolder.image);
        }

        if(viewHolder.total != null){
            viewHolder.total.setText("Stocks("+currentItem.getTotal()+")");
        }

        if(viewHolder.quantity != null){

            final int total = currentItem.getTotal();
            final Cart cart = appDb.cartDao().get(data.getId(), currentItem.getId());

            viewHolder.quantity.setNumber(String.valueOf(cart.getQuantity()));

            viewHolder.quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, final int oldValue, final int newValue) {

                    if(newValue <= total && newValue > 0){
                        currentItem.setQuantity(newValue);
                        appDb.cartDao().updateQuantity(newValue, currentItem.getId());

                        if(currentItem.isSelected()){
                            int price = currentItem.getPrice() * newValue;
                            appDb.totalDao().update(price, currentItem.getId());

                            if(ttotal != null){
                                int total = appDb.totalDao().sum();
                                if(total == 0){
                                    ttotal.setText("\u20B10.00");
                                }else{
                                    ttotal.setText("\u20B1"+currencyFormatter(total));
                                }
                            }
                        }

                    }else{
                        viewHolder.quantity.setNumber(String.valueOf(oldValue));
                    }


                }
            });

        }

        if(viewHolder.checkBox != null){

            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                    currentItem.setSelected(isChecked);

                    Total totalItem = appDb.totalDao().get(currentItem.getId());
                    if(isChecked){
                        if(totalItem == null){
                            appDb.totalDao().insert(new Total(currentItem.getId(), currentItem.getPrice()));
                            //Product product = appDb.productDao().get(currentItem.getId());
                            Cart cart = appDb.cartDao().get(data.getId(), currentItem.getId());
                            int price = currentItem.getPrice() * cart.getQuantity();
                            appDb.totalDao().update(price, currentItem.getId());
                        }
                        selectedItems.put(position, true);
                        currentItem.setSelected(true);
                        viewHolder.itemView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                        viewHolder.checkBox.setVisibility(View.VISIBLE);
                    }else{
                        if(totalItem != null){
                            appDb.totalDao().delete(currentItem.getId());
                        }
                        selectedItems.delete(position);
                        currentItem.setSelected(false);
                        viewHolder.itemView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                        viewHolder.checkBox.setVisibility(View.GONE);
                    }
                    if(selectedItems.size() == 0){
                        isPressed = false;
                    }
                    if(ttotal != null){
                        int total = appDb.totalDao().sum();
                        if(total == 0){
                            ttotal.setText("\u20B10.00");
                        }else{
                            ttotal.setText("\u20B1"+currencyFormatter(total));
                        }
                    }

                }
            });
        }

        if(viewHolder.row != null){
            viewHolder.row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(!isPressed){
                        isPressed = true;
                        selectedItems.put(position, true);
                        currentItem.setSelected(true);
                        viewHolder.row.setSelected(true);
                        viewHolder.checkBox.setVisibility(View.VISIBLE);
                        viewHolder.checkBox.setChecked(true);
                        v.setBackgroundColor(Color.parseColor("#d3d3d3"));
                    }

                    return true;
                }
            });

            viewHolder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isPressed){
                        if(selectedItems.get(position, false)){
                            selectedItems.delete(position);
                            currentItem.setSelected(false);
                            viewHolder.row.setSelected(false);
                            viewHolder.checkBox.setChecked(false);
                            viewHolder.checkBox.setVisibility(View.GONE);
                            v.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                        }else{
                            selectedItems.put(position, true);
                            currentItem.setSelected(true);
                            viewHolder.row.setSelected(true);
                            viewHolder.checkBox.setVisibility(View.VISIBLE);
                            viewHolder.checkBox.setChecked(true);
                            v.setBackgroundColor(Color.parseColor("#d3d3d3"));

                        }

                        if(selectedItems.size() == 0){
                            isPressed = false;
                        }
                    }else{
                        if(position != RecyclerView.NO_POSITION){

                            ProductViewActivity.setProduct((Product)items.get(position));
                            Intent intent = new Intent(context, ProductViewActivity.class);
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }

    }
    
    public void populateCheckoutRows(final CheckoutViewHolder viewHolder, final int position){
        final Product currentItem = (Product) getItem(position);
        currentItem.setPosition(position);
        viewHolder.packageCount.setText("Package "+(position+1)+" of "+items.size());
        String price = "\u20B10.00";
        viewHolder.name.setText(currentItem.getName());
        if(currentItem.getPrice() > 0){
            price = "\u20B1"+currencyFormatter(currentItem.getPrice());
        }
        viewHolder.price.setText(price);
        viewHolder.quantity.setText("Qty: "+currentItem.getQuantity());

        if(viewHolder.image != null && !currentItem.getImage().isEmpty()){
            Picasso.get().load(currentItem.getImage())
                    .error(R.drawable.dummy_image)
                    .into(viewHolder.image);
        }else{
            Picasso.get().load(R.drawable.dummy_image)
                    .into(viewHolder.image);
        }

    }

    public void populateOrderRows(final OrderViewHolder viewHolder, final int position){
        User user = User.getInstance();
        user.init(context);
        final Data data = user.loadDataFromSession();
        final Product currentItem = (Product) getItem(position);
        currentItem.setPosition(position);
        String price = "\u20B10.00";
        viewHolder.name.setText(currentItem.getName());
        if(currentItem.getPrice() > 0){
            price = "\u20B1"+currencyFormatter(currentItem.getPrice());
        }
        viewHolder.price.setText(price);
        viewHolder.quantity.setText("Qty: "+currentItem.getQuantity());
        viewHolder.ratingBar.setRating(currentItem.getRating());
        viewHolder.orderNo.setText("#"+currentItem.getOrderNo());
        viewHolder.date.setText(Utility.getDate(currentItem.getDateOrdered()));
        viewHolder.paymentMethod.setText(currentItem.getPaymentMethod());

        if(viewHolder.image != null && !currentItem.getImage().isEmpty()){
            Picasso.get().load(currentItem.getImage())
                    .error(R.drawable.dummy_image)
                    .into(viewHolder.image);
        }else{
            Picasso.get().load(R.drawable.dummy_image)
                    .into(viewHolder.image);
        }
        String status = currentItem.getOrderStatus();
        if(status.contains("_")){
            status = status.replace("_", " ");
        }
        viewHolder.orderStatus.setText(status);

        String action = "";
        switch(currentItem.getOrderStatus()){
            case "PENDING":
            case "AWAITING_PAYMENT":
            case "AWAITING_SHIPMENT":
            case "AWAITING_PICKUP":
            case "PARTIALLY_PICKUP":
            case "SHIPPED":
            case "VERIFICATION_REQUIRED":
                action = "CANCEL";
                break;
            case "CANCELLED":
            case "DECLINED":
            case "REFUNDED":
            case "DISPUTED":
            case "PARTIALLY_REFUNDED":
                action = "BUY AGAIN";
                break;
            default:
                action = "TO REVIEW";
        }
        SpannableString content = new SpannableString(action);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        viewHolder.action.setText(content);

        final String orderStatus = action;
        viewHolder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DbHelper db = new DbHelper(context);
                db.setURL(URL,"update_order.php");
                db.table("`order`");
                db.param("code", currentItem.getOrderNo());
                db.param("user_id", String.valueOf(data.getId()));

                switch(orderStatus){
                    case "CANCEL":
                        db.param("order_status", "CANCELLED");
                        break;
                    case "BUY AGAIN":
                        db.param("order_status", "PENDING");
                        break;
                }

                db.write(new OnWriteClient() {
                    @Override
                    public void onSuccess() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                db.setURL(URL,"get.php");
                                db.table("`order`");
                                db.param("code", currentItem.getOrderNo());
                                db.param("user_id", String.valueOf(data.getId()));
                                db.read(new OnReadClient<Product>() {
                                    @Override
                                    public void onSuccess(List<Product> results) {
                                        Toast.makeText(context, "size: "+results.size(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(String message) {
                                        switch (message){
                                            case "connect_error":
                                                Toast.makeText(context, "Database connection error", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "no_table":
                                                Toast.makeText(context, "No table", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "query_error":
                                                Toast.makeText(context, "Query error", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "request_timeout":
                                                Toast.makeText(context, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "no_internet_connection":
                                                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "internal_server_error":
                                                Toast.makeText(context, "Internal Server Error", Toast.LENGTH_SHORT).show();
                                                break;
                                            default:
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }, 1000);
                    }

                    @Override
                    public void onError(String message) {
                        switch (message){
                            case "connect_error":
                                Toast.makeText(context, "Database connection error", Toast.LENGTH_SHORT).show();
                                break;
                            case "no_table":
                                Toast.makeText(context, "No table", Toast.LENGTH_SHORT).show();
                                break;
                            case "query_error":
                                Toast.makeText(context, "Query error", Toast.LENGTH_SHORT).show();
                                break;
                            case "update_error":
                                Toast.makeText(context, "Unable to update account", Toast.LENGTH_SHORT).show();
                                break;
                            case "request_timeout":
                                Toast.makeText(context, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
                                break;
                            case "no_internet_connection":
                                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                break;
                            case "internal_server_error":
                                Toast.makeText(context, "Internal Server Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }


    public void populateFavoriteRows(final FavoriteViewHolder viewHolder, final int position){
        final Product currentItem = (Product) getItem(position);
        currentItem.setPosition(position);
        String price = "\u20B10.00";
        viewHolder.name.setText(currentItem.getName());
        if(currentItem.getPrice() > 0){
            price = "\u20B1"+currencyFormatter(currentItem.getPrice());
        }
        viewHolder.price.setText(price);
        viewHolder.ratingBar.setRating(currentItem.getRating());

        viewHolder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               appDb.favoriteDao().delete(currentItem.getId());
               removeAt(viewHolder.getAdapterPosition());
                ttotal.setText("Item ("+items.size()+")");
            }
        });

        if(viewHolder.image != null && !currentItem.getImage().isEmpty()){
            Picasso.get().load(currentItem.getImage())
                    .error(R.drawable.dummy_image)
                    .into(viewHolder.image);
        }else{
            Picasso.get().load(R.drawable.dummy_image)
                    .into(viewHolder.image);
        }

    }

    public void addNuLLData(){
        items.add(null);
        notifyItemInserted(items.size()-1);
    }

    public void removeNull(){
        items.remove(items.size()-1);
        notifyItemRemoved(items.size());
    }

    public  void addData(List<Product> items){
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void setTotal(TextView total){
        ttotal = total;
    }

    public void removeAt(int position){
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

}
