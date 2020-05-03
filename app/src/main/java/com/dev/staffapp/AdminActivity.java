package com.dev.staffapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dev.staffapp.Adapter.MyViewPagerAdapter;
import com.dev.staffapp.Common.Common;
import com.dev.staffapp.Fragments.FragmentAccount;
import com.dev.staffapp.Model.Barber;
import com.dev.staffapp.Model.EventBus.BarberDoneEvent;
import com.dev.staffapp.Model.EventBus.EnableNextButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

import com.dev.staffapp.Common.NonSwipeViewPager;

public class AdminActivity extends AppCompatActivity {

    private static final String TAG = "AdminActivity";

    //    @BindView(R.id.bottom_navigation)
//    BottomNavigationView bottomNavigationView;
    AlertDialog dialog;
    CollectionReference barberRef;

    //    @BindView(R.id.step_view)
//    StepView stepView;
    @BindView(R.id.view_pager)
    NonSwipeViewPager viewPager;
    @BindView(R.id.btn_next_step)
    Button btn_next_step;
    @BindView(R.id.btn_edit)
    Button btn_edit;
    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;
    @BindView(R.id.btn_add_new)
    Button btn_add_new;



    BottomSheetDialog bottomSheetDialog;

    @OnClick(R.id.btn_previous_step)
    void PreviousStep() {
        if (Common.step == 2 || Common.step > 0) {
            Common.step--;
            viewPager.setCurrentItem(Common.step);
            if (Common.step < 2) //Always Enable Next when step <  3
            {
                btn_next_step.setEnabled(true);
                btn_edit.setVisibility(View.GONE);
                btn_next_step.setVisibility(View.VISIBLE);
                setColorButton();
            }


        }
    }
    @OnClick(R.id.btn_add_new)
    void setBtn_add_new() {
        Intent intent = new Intent(this,ActivityAddNewAccount.class);
        intent.putExtra("salonID",Common.currentSalon.getSalonId());
        intent.putExtra("city",Common.city);
        startActivity(intent);
        Log.e("cityName", "setBtn_add_new: "+Common.city );

    }


    @OnClick(R.id.btn_edit)
    void setBtn_edit() {
        Intent intent = new Intent(AdminActivity.this, ActivityEditInfor.class);
        intent.putExtra("barberID", Common.currentBarber.getBarberId());
        intent.putExtra("barberName", Common.currentBarber.getName());
        intent.putExtra("barberUsername", Common.currentBarber.getUsername());
        intent.putExtra("barberPassword", Common.currentBarber.getPassword());
        intent.putExtra("city", Common.currentBarber.getCity());
        intent.putExtra("salon", Common.currentBarber.getSalon());
        startActivity(intent);

    }

    @OnClick(R.id.btn_next_step)
    void nextClick() {

        if (Common.step < 3 || Common.step == 0) {
            Common.step++; //Increase

            if (Common.step == 1)//After Choose Salon
            {
                if (Common.currentSalon != null)

                    loadBarberBySalon(Common.currentSalon.getSalonId());
                btn_next_step.setEnabled(true);
                btn_edit.setVisibility(View.GONE);
                btn_next_step.setVisibility(View.VISIBLE);

            } else if (Common.step == 2) //Pick Time Slot
            {
                if (Common.currentBarber != null)

                    loadBarberBySalon(Common.currentSalon.getSalonId());
                btn_next_step.setEnabled(false);
                btn_edit.setVisibility(View.VISIBLE);
                btn_next_step.setVisibility(View.GONE);

//                        Toast.makeText(this, ""+Common.currentBarber.getBarberId(), Toast.LENGTH_SHORT).show();

            }
            viewPager.setCurrentItem(Common.step);
        }


//        Toast.makeText(this, ""+Common.currentSalon.getSalonId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Check Rating Dialog
    }


    //    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        btn_next_step = findViewById(R.id.btn_next_step);
//        btn_next_step.setEnabled(true);
        ButterKnife.bind(AdminActivity.this);

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
//        Fragment fragment = null;
//                    btn_next_step.setVisibility(View.VISIBLE);
//                    btn_previous_step.setVisibility(View.VISIBLE);
//                    btn_edit.setVisibility(View.GONE);
//        fragment = new FragmentAccount();
//        loadFragment(fragment);
//        setupStepView();
        setColorButton();

        //View
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2); //4 Fragment with 4 Page
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {

                //Show Step
//                stepView.go(i, true);
                if (i == 0) {
                    btn_previous_step.setEnabled(false);
                    btn_add_new.setEnabled(false);
                    btn_edit.setVisibility(View.GONE);
                }else {
                    btn_previous_step.setEnabled(true);

                    //Set Disable button Next Here
                    btn_next_step.setEnabled(false);
                    btn_add_new.setVisibility(View.VISIBLE);
                    btn_add_new.setEnabled(true);
                }
                setColorButton();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        if (getIntent() != null) {

//            bottomNavigationView.setSelectedItemId(R.id.action_home);

        }


//View
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            Fragment fragment = null;
//
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                if (menuItem.getItemId() == R.id.action_home) {
//                    fragment = new FragmentAccount();
//                    btn_next_step.setVisibility(View.VISIBLE);
//                    btn_previous_step.setVisibility(View.VISIBLE);
//                    btn_edit.setVisibility(View.GONE);
//
//
//                }
//
//
//                return loadFragment(fragment);
//            }
//        });


    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void loadBarberBySalon(String salonId) {
        Log.e("slID", "loadBarberBySalon: " + salonId);
        dialog.show();
        //Now, Select all Barbor of Salon
        ///AlalSalon/NewYork/Branch/q4Uw4qSsI64PxcP3Szdf/Barbers
        if (!TextUtils.isEmpty(Common.city)) {
            barberRef = FirebaseFirestore.getInstance()
                    .collection("AllSalon")
                    .document(Common.city)
                    .collection("Branch")
                    .document(salonId)
                    .collection("Barbers");

            barberRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Barber> barbers = new ArrayList<>();
                            for (QueryDocumentSnapshot barberSnapshot : task.getResult()) {
                                Barber barber = barberSnapshot.toObject(Barber.class);
//                                barber.setPassword(""); //Remove Password
                                barber.setBarberId(barberSnapshot.getId());

                                barbers.add(barber);
                            }

                            //Send Broadcast to BookingStep2Fragment to Load Recycler
//                        Intent intent = new Intent(Common.KEY_BARBER_LOAD_DONE);
//                        intent.putParcelableArrayListExtra(Common.KEY_BARBER_LOAD_DONE, barbers);
//                        localBroadcastManager.sendBroadcast(intent);

                            EventBus.getDefault().postSticky(new BarberDoneEvent(barbers));

                            dialog.dismiss();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                        }
                    });

        }


    }

    //Event Bus Convert
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void buttonNextReceiver(EnableNextButton event) {
        int step = event.getStep();


        if (step == 1) {
            Common.currentSalon = event.getSalon();
            btn_next_step.setEnabled(true);
            btn_edit.setVisibility(View.GONE);
            btn_next_step.setVisibility(View.VISIBLE);
            btn_add_new.setEnabled(true);
            btn_add_new.setVisibility(View.VISIBLE);
        } else if (step == 2) {
            Common.currentBarber = event.getBarber();
            btn_next_step.setVisibility(View.GONE);
            btn_edit.setVisibility(View.VISIBLE);
            btn_add_new.setVisibility(View.GONE);
        }

        setColorButton();
    }

    private void setColorButton() {
        if (btn_next_step.isEnabled()) {
            btn_next_step.setBackgroundResource(R.color.colorButton);
        } else {
            btn_next_step.setBackgroundResource(android.R.color.darker_gray);
        }
        if (btn_previous_step.isEnabled()) {
            btn_previous_step.setBackgroundResource(R.color.colorButton);
        } else {
            btn_previous_step.setBackgroundResource(android.R.color.darker_gray);
        }
    }

//    private void setupStepView() {
//        List<String> stepList = new ArrayList();
//        stepList.add("Cilinic");
//        stepList.add("Doctor");
//        stepView.setSteps(stepList);
//    }

    //================================================================
    //EVENT BUS START

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


}


