package com.dev.staffapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.staffapp.Common.Common;
import com.dev.staffapp.Interface.IRecyclerItemSelectedListener2;
import com.dev.staffapp.Model.Barber;
import com.dev.staffapp.Model.EventBus.EnableNextButton;
import com.dev.staffapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MyBarberAdapter extends RecyclerView.Adapter<MyBarberAdapter.MyViewHolder> {

    Context context;
    List<Barber> barberList;
    List<CardView> cardViewList;

    LocalBroadcastManager localBroadcastManager;


    public MyBarberAdapter(Context context, List<Barber> barberList) {
        this.context = context;
        this.barberList = barberList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_account, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_barber_name.setText("Name: "+barberList.get(i).getName());
        myViewHolder.txt_user.setText("Username: "+barberList.get(i).getUsername());
        myViewHolder.txt_pass.setText("Password: "+barberList.get(i).getPassword());
        Log.e("INFOR", "onBindViewHolder: "+barberList.get(i).getPassword() );
        if (!cardViewList.contains(myViewHolder.card_barber))
            cardViewList.add(myViewHolder.card_barber);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener2() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Set Background for all item no choice
                for (CardView cardView : cardViewList) {
                    cardView.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.white));

                }

                //Set Background for Choice
                myViewHolder.card_barber.setCardBackgroundColor(context.getResources()
                        .getColor(android.R.color.holo_orange_dark));

                //send Broadcast to tell Booking Activity enable button Next
                Intent intent = new Intent(Common.KEY_ENALBE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_BARBER_SELECTED, barberList.get(pos));
                intent.putExtra(Common.KEY_STEP, 2);
                localBroadcastManager.sendBroadcast(intent);

                //==================================================
                //Event Bus
                EventBus.getDefault().postSticky(new EnableNextButton(2, barberList.get(pos)));

                //=================================================

            }
        });

    }

    @Override
    public int getItemCount() {
        return barberList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_barber_name;
        TextView txt_user;
        TextView txt_pass;

        CardView card_barber;

        IRecyclerItemSelectedListener2 iRecyclerItemSelectedListener2;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener2 iRecyclerItemSelectedListener2) {
            this.iRecyclerItemSelectedListener2 = iRecyclerItemSelectedListener2;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_barber = (CardView) itemView.findViewById(R.id.card_acc);

            txt_barber_name = (TextView) itemView.findViewById(R.id.tvName);
            txt_user = (TextView) itemView.findViewById(R.id.tvUser);
            txt_pass = (TextView) itemView.findViewById(R.id.tvPass);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener2.onItemSelectedListener(view, getAdapterPosition());
        }
    }
}
