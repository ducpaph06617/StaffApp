package com.dev.staffapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.staffapp.Adapter.MySalonAdapter;
import com.dev.staffapp.Common.Common;
import com.dev.staffapp.Common.SpacesItemDecoration;
import com.dev.staffapp.Interface.IBranchLoadListener;
import com.dev.staffapp.Interface.IGetBarberListener;
import com.dev.staffapp.Interface.IOnLoadCountSalon;
import com.dev.staffapp.Interface.IUserLoginRememberListener;
import com.dev.staffapp.Model.Barber;
import com.dev.staffapp.Model.Salon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class SalonListActivity extends AppCompatActivity implements IOnLoadCountSalon, IBranchLoadListener, IGetBarberListener, IUserLoginRememberListener {

    @BindView(R.id.txt_salon_count)
    TextView txt_salon_count;

    IOnLoadCountSalon iOnLoadCountSalon;
    IBranchLoadListener iBranchLoadListener;

    AlertDialog dialog;

    @BindView(R.id.recycler_salon)
    RecyclerView  recycler_salon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_list);

        ButterKnife.bind(this);
        
        initView();
        init();

        loadSalonBasedOnCity(Common.state_name);

    }

    private void loadSalonBasedOnCity(String name) {
        dialog.show();

        FirebaseFirestore.getInstance().collection("AllSalon")
                .document(name)
                .collection("Branch")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        List<Salon> salons = new ArrayList<>();
                        if(task.isSuccessful()){
                            iOnLoadCountSalon.onLoadCountSalonSuccess(task.getResult().size());
                            for(DocumentSnapshot salonSnapshot :task.getResult() )
                            {
                                Salon salon = salonSnapshot.toObject(Salon.class);
                                salon.setSalonId(salonSnapshot.getId());
                                salons.add(salon);

                            }
                            iBranchLoadListener.onBranchLoadSuccess(salons);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBranchLoadListener.onBranchLoadFailed(e.getMessage());
            }
        });
    }

    private void init() {
        dialog = new SpotsDialog.Builder().setContext(this)
                .setCancelable(false).build();
        iOnLoadCountSalon=this;
        iBranchLoadListener = this;

    }

    private void initView() {
        recycler_salon.setHasFixedSize(true);
        recycler_salon.setLayoutManager(new GridLayoutManager(this, 2));
        recycler_salon.addItemDecoration(new SpacesItemDecoration(8));
    }

    @Override
    public void onLoadCountSalonSuccess(int count) {
        txt_salon_count.setText(new StringBuilder("All Salon (")
            .append(count).append(")")
        );

    }

    @Override
    public void onBranchLoadSuccess(List<Salon> branchList) {
        MySalonAdapter salonAdapter = new MySalonAdapter(this, branchList, this, this);
        recycler_salon.setAdapter(salonAdapter);

        dialog.dismiss();
    }

    @Override
    public void onBranchLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onGetBarberSuccess(Barber barber) {
        Common.currentBarber = barber;
        Paper.book().write(Common.BARBER_KEY, new Gson().toJson(barber));
    }

    @Override
    public void onUserLoginSuccess(String user) {
        //Save User
        Paper.init(this);
        Paper.book().write(Common.LOGGED_KEY, user);
        Paper.book().write(Common.STATE_KEY, Common.state_name);
        Paper.book().write(Common.SALON_KEY, new Gson().toJson(Common.selectedSalon));

    }
}
