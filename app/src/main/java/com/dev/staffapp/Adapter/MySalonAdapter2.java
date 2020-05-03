package com.dev.staffapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.staffapp.Common.Common;
import com.dev.staffapp.Common.CustomLoginDialog;
import com.dev.staffapp.Interface.IDialogClickListner;
import com.dev.staffapp.Interface.IGetBarberListener;
import com.dev.staffapp.Interface.IRecyclerItemSelectedListener;
import com.dev.staffapp.Interface.IRecyclerItemSelectedListener2;
import com.dev.staffapp.Interface.IUserLoginRememberListener;
import com.dev.staffapp.Model.Barber;
import com.dev.staffapp.Model.EventBus.EnableNextButton;
import com.dev.staffapp.Model.Salon;
import com.dev.staffapp.R;
import com.dev.staffapp.StaffHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MySalonAdapter2 extends RecyclerView.Adapter<MySalonAdapter2.MyViewHolder> {

    Context context;
    List<Salon> salonList;
    List<CardView> cardViewList;

    // LocalBroadcastManager localBroadcastManager;

    public MySalonAdapter2 (Context context, List<Salon> salonList) {
        this.context = context;
        this.salonList = salonList;
        cardViewList =  new ArrayList<>();
//        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_salon, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_salon_name.setText(salonList.get(i).getName());
        myViewHolder.txt_salon_address.setText(salonList.get(i).getAddress());

        if(!cardViewList.contains(myViewHolder.card_salon))
            cardViewList.add(myViewHolder.card_salon);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener2() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Set White Background for all card which are not selected
                for(CardView cardView : cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                //Set Background for selected item
                myViewHolder.card_salon.setCardBackgroundColor(context.getResources()
                        .getColor(android.R.color.holo_orange_dark));
                EventBus.getDefault().postSticky(new EnableNextButton(1,salonList.get(pos)));
            }








        });
    }

    @Override
    public int getItemCount() {
        return salonList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_salon_name, txt_salon_address;
        CardView card_salon;
        Button button;
        IRecyclerItemSelectedListener2 iRecyclerItemSelectedListener2;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener2 iRecyclerItemSelectedListener2) {
            this.iRecyclerItemSelectedListener2 = iRecyclerItemSelectedListener2;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_salon = (CardView)itemView.findViewById(R.id.card_salon);
            txt_salon_address = (TextView)itemView.findViewById(R.id.txt_salon_address);
            txt_salon_name = (TextView)itemView.findViewById(R.id.txt_salon_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener2.onItemSelectedListener(view, getAdapterPosition());
        }
    }
}
