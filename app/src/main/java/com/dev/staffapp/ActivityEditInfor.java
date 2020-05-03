package com.dev.staffapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.dev.staffapp.Common.Common;
import com.dev.staffapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.StorageReference;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class ActivityEditInfor extends AppCompatActivity {

    EditText edt_infor_name;
    EditText edt_infor_user;
    EditText edt_infor_pass;
    Button btnSaveuserinfo;
    DocumentReference barnerRefD;
    CollectionReference barberRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_infor);
        edt_infor_name = findViewById(R.id.edt_infor_name);
        edt_infor_user = findViewById(R.id.edt_infor_user);
        edt_infor_pass = findViewById(R.id.edt_infor_pass);
        btnSaveuserinfo = findViewById(R.id.btnSaveuserinfo);
        String name = edt_infor_name.getText().toString().trim();
        String username = edt_infor_user.getText().toString().trim();
        String password = edt_infor_pass.getText().toString().trim();
        method();
//        Intent intent = getIntent();
//        String barberName = intent.getStringExtra("barberName");
//        String barberUsername = intent.getStringExtra("barberUsername");
//        String barberPassword = intent.getStringExtra("barberPassword");
//        if (name.equalsIgnoreCase(barberName)||username.equalsIgnoreCase(barberUsername)||password.equalsIgnoreCase(barberPassword)){
//            btnSaveuserinfo.setEnabled(false);
//        }
//        else{
//            btnSaveuserinfo.setEnabled(true);
//        }

    }

    public void method() {

        Intent intent = getIntent();
        String barberID = intent.getStringExtra("barberID");
        String salonID = intent.getStringExtra("salonID");
        String barberName = intent.getStringExtra("barberName");
        String barberUsername = intent.getStringExtra("barberUsername");
        String barberPassword = intent.getStringExtra("barberPassword");
        String salon = intent.getStringExtra("salon");
        String city = intent.getStringExtra("city");
        Log.e("slID", "loadBarberBySalon2: " + salon + city);

        edt_infor_name.setText(barberName);
        edt_infor_user.setText(barberUsername);
        edt_infor_pass.setText(barberPassword);
    }

    public void exitEditInfor(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?!")
                .setTitle("Delete");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                Intent intent = getIntent();
                String barberID = intent.getStringExtra("barberID");
                String salon = intent.getStringExtra("salon");
                String city = intent.getStringExtra("city");
                Log.e("slID", "loadBarberBySalon2: " + salon + city);


                db.collection("AllSalon")
                        .document(city)
                        .collection("Branch")
                        .document(salon)
                        .collection("Barbers")
                        .document(barberID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ActivityEditInfor.this, "delete Success", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(ActivityEditInfor.this, AdminActivity.class);
////                                System.exit(0);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("deleteF", "onFailure: " + e.getMessage());
                    }
                });


            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void saveInfor(View view) {
        ///AllSalon/CauGiay/Branch/RxgXHkenjGeAfoHcpo6q/Barbers/01QLyCoFgpWDHMtGXEUz
        String name = edt_infor_name.getText().toString().trim();
        String username = edt_infor_user.getText().toString().trim();
        String password = edt_infor_pass.getText().toString().trim();

        Intent intent = getIntent();
        String barberID = intent.getStringExtra("barberID");
        String salon = intent.getStringExtra("salon");
        String city = intent.getStringExtra("city");
        String barberName = intent.getStringExtra("barberName");
        String barberUsername = intent.getStringExtra("barberUsername");
        String barberPassword = intent.getStringExtra("barberPassword");
        if (city != "" && salon != "") {
            if (name.equals("")) {
                edt_infor_name.setError("this field is not emty!");
                return;
            } else if (username.equals("")) {
                edt_infor_user.setError("this field is not emty!");
                return;
            } else if (password.equals("")) {
                edt_infor_pass.setError("this field is not emty!");
                return;
            } else if (name.length() > 13) {
                edt_infor_name.setError("this field is too long!");
                return;
            } else if (username.length() > 13) {
                edt_infor_user.setError("this field is too long!");
                return;
            } else if (password.length() > 13) {
                edt_infor_pass.setError("this field is too long!");
                return;
            }
            Map<String, Object> barberss = new HashMap<>();
            barberss.put("name", name);
            barberss.put("username", username);
            barberss.put("password", password);
            db.collection("AllSalon")
                    .document(city)
                    .collection("Branch")
                    .document(salon)
                    .collection("Barbers")
                    .document(barberID)
                    .update(barberss)
//                .set(barberss,SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ActivityEditInfor.this, "update Success", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(ActivityEditInfor.this, AdminActivity.class);
//                            System.exit(0);
//                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ActivityEditInfor.this, "update Faild", Toast.LENGTH_SHORT).show();
                    Log.e("Err", "onFailure: " + e.getMessage());
                }
            });


        }
    }
}
