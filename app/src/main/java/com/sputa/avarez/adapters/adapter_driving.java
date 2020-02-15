package com.sputa.avarez.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sputa.avarez.Functions;
import com.sputa.avarez.R;
import com.sputa.avarez.model.ItemDriving;
import com.sputa.avarez.model.item_tablo;

import java.util.List;


public class adapter_driving extends RecyclerView.Adapter<adapter_driving.my_view_holder> {
    private Context context;
    private List<ItemDriving> item;
    View ItemView;
    private boolean is_requested;
    Functions fun;
    String
            font_name = "";
    Typeface tf;

    public adapter_driving(Context context, List<ItemDriving> item) {
        this.context = context;
        this.item = item;
        fun = new Functions();


    }

    @Override
    public my_view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_driving_item, parent, false);

        return new my_view_holder(view);
    }

    @Override
    public void onBindViewHolder(final my_view_holder holder, final int position) {
        holder.txt_price.setText(item.get(position).getAmount());
        holder.txt_city.setText(item.get(position).getCity());
        holder.txt_location.setText(item.get(position).getLocation());
        holder.txt_type.setText(item.get(position).getType());
        holder.txt_delivery.setText(item.get(position).getDelivery());
        holder.txt_date.setText(item.get(position).getDateTime());

        if (Integer.valueOf(item.get(position).getAmount()) <= 0 || item.get(position).getBillId().length() <= 1) {
            holder.btn_pay.setVisibility(View.GONE);
        }


        holder.crd1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (Integer.valueOf(item.get(position).getAmount()) > 0 && item.get(position).getBillId().length()>1) {
//                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(item.get(position).getPaymentUrl()));
//                    context.startActivity(i);

                    Intent intent = new Intent (Intent.ACTION_VIEW, Uri.parse(item.get(position).getPaymentUrl()));
                    view.getContext().startActivity(intent);
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return item.size();
    }


    class my_view_holder extends RecyclerView.ViewHolder {

        private TextView txt_city;
        private TextView txt_location;
        private TextView txt_type;
        private TextView txt_date;
        private TextView txt_delivery;
        private TextView txt_price;
        private TextView btn_pay;


        private CardView crd1;

        public my_view_holder(View itemView) {
            super(itemView);

            txt_city = itemView.findViewById(R.id.txt_city);
            txt_location = itemView.findViewById(R.id.txt_location);
            txt_type = itemView.findViewById(R.id.txt_type);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_delivery = itemView.findViewById(R.id.txt_delivery);
            txt_price = itemView.findViewById(R.id.txt_price);
            btn_pay = itemView.findViewById(R.id.btn_pay_driving);

            crd1 = itemView.findViewById(R.id.crd1);


//            TextView txt_year =  itemView.findViewById(R.id.txt_title);


        }
    }


}
