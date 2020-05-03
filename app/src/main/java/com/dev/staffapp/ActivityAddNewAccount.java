package com.dev.staffapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.staffapp.Model.Barber;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityAddNewAccount extends AppCompatActivity {
    EditText edt_infor_name;
    EditText edt_infor_user;
    EditText edt_infor_pass;
    DocumentReference barnerRefD;
    CollectionReference barberRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Barber> barbers = new ArrayList<>();
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_account);
        edt_infor_name = findViewById(R.id.edt_add_name);
        edt_infor_user = findViewById(R.id.edt_add_user);
        edt_infor_pass = findViewById(R.id.edt_add_pass);

        Intent intent = getIntent();
//        String city = intent.getStringExtra("city");
//        String salonID = intent.getStringExtra("salonID");
//        String username = edt_infor_user.getText().toString().trim();
//        Log.e("data", "onCreate: " +city+salonID);
        String city = intent.getStringExtra("city");
        String salonID = intent.getStringExtra("salonID");
        String name = edt_infor_name.getText().toString().trim();
        String username = edt_infor_user.getText().toString().trim();
        String password = edt_infor_pass.getText().toString().trim();
        barberRef = FirebaseFirestore.getInstance()
                .collection("AllSalon")
                .document(city)
                .collection("Branch")
                .document(salonID)
                .collection("Barbers");

        barberRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        ArrayList<Barber> barbers = new ArrayList<>();
                        for (QueryDocumentSnapshot barberSnapshot : task.getResult()) {
                            Barber barber = barberSnapshot.toObject(Barber.class);
//                                barber.setPassword(""); //Remove Password
                            barbers.clear();
                            barbers.add(barber);

                            Log.e("barbersss", "onComplete: " + barbers.size());
                            int i = 0;
//                            for (i = 0; i < barbers.size(); i++) {
//                                if (barbers != null) {
//                                    if (username.equalsIgnoreCase(barbers.get(i).getUsername())) {
//
//                                    } else if (name.equalsIgnoreCase(barbers.get(i).getName())) {
//
//                                    }
//                                }
//                            }
                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void checkUser() {

        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        String salonID = intent.getStringExtra("salonID");
        String name = edt_infor_name.getText().toString().trim();
        String username = edt_infor_user.getText().toString().trim();
        String password = edt_infor_pass.getText().toString().trim();
        Double rating = 0.0;
        Double ratingTimes = 0.0;
        barberRef = FirebaseFirestore.getInstance()
                .collection("AllSalon")
                .document(city)
                .collection("Branch")
                .document(salonID)
                .collection("Barbers");

        barberRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Barber> barbers = new ArrayList<>();
                        for (QueryDocumentSnapshot barberSnapshot : task.getResult()) {
                            Barber barber = barberSnapshot.toObject(Barber.class);
//                                barber.setPassword(""); //Remove Password
                            barbers.clear();
                            barbers.add(barber);

                            Log.e("barbersss", "onComplete: " + barbers.size());
                            int i = 0;
                            for (i = 0; i < barbers.size(); i++) {
                                if (barbers != null) {
                                    if (username.equalsIgnoreCase(barbers.get(i).getUsername())) {

                                    } else if (name.equalsIgnoreCase(barbers.get(i).getName())) {

                                    }
                                }
                            }
                        }
                    }
                });

    }

    public void addAccount(View view) {


        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        String salonID = intent.getStringExtra("salonID");
        String name = edt_infor_name.getText().toString().trim();
        String username = edt_infor_user.getText().toString().trim();
        String password = edt_infor_pass.getText().toString().trim();
        Double rating = 0.0;
        Double ratingTimes = 0.0;


        if (city != "" && salonID != "") {
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
                edt_infor_name.setError("this field is too long!(<13)");
                return;
            } else if (username.length() > 13) {
                edt_infor_user.setError("this field is too long!(<13)");
                return;
            } else if (password.length() > 13) {
                edt_infor_pass.setError("this field is too long!(<13)");
                return;
            }
//            for (i = 0; i < barbers.size(); i++) {
//                if (barbers != null) {
//                    if (username.equalsIgnoreCase(barbers.get(i).getUsername())) {
//                        edt_infor_user.setError("This name is already taken!");
//                        return;
//
//                    } else if (name.equalsIgnoreCase(barbers.get(i).getName())) {
//                        edt_infor_name.setError("This name is already taken!");
//                        return;
//                    }
//                }
//            }


            Map<String, Object> account = new HashMap<>();
            account.put("name", name);
            account.put("username", username);
            account.put("password", password);
            account.put("rating", rating);
            account.put("ratingTimes", ratingTimes);
            account.put("city", city);
            account.put("salon", salonID);
            db.collection("AllSalon")
                    .document(city)
                    .collection("Branch")
                    .document(salonID)
                    .collection("Barbers")
                    .add(account)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(ActivityAddNewAccount.this, "Success", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(ActivityAddNewAccount.this, AdminActivity.class);
//                                                            System.exit(0);
//                                                            startActivity(intent);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ActivityAddNewAccount.this, "Faild", Toast.LENGTH_SHORT).show();
                            Log.e("err", "onFailure: " + e.getMessage());
                        }
                    });
        }
    }
}
