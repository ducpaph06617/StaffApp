package com.dev.staffapp.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.staffapp.Adapter.MyConfirmationShoppingItemAdapter;
import com.dev.staffapp.Common.Common;
import com.dev.staffapp.Model.BarberServices;
import com.dev.staffapp.Model.CartItem;
import com.dev.staffapp.Model.EventBus.DismissFromBottomSheetEvent;
import com.dev.staffapp.Model.FCMResponse;
import com.dev.staffapp.Model.FCMSendData;
import com.dev.staffapp.Model.Invoice;
import com.dev.staffapp.Model.MyToken;
import com.dev.staffapp.R;
import com.dev.staffapp.Retrofit.IFCMService;
import com.dev.staffapp.Retrofit.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TotalPriceFragment extends BottomSheetDialogFragment {
    Unbinder unbinder;

    @BindView(R.id.chip_group_services)
    ChipGroup chip_group_services;

    @BindView(R.id.recycler_view_shopping)
    RecyclerView recycler_view_shopping;

    @BindView(R.id.txt_salon_name)
    TextView txt_salon_name;

    @BindView(R.id.txt_barber_name)
    TextView txt_barber_name;

    @BindView(R.id.txt_time)
    TextView txt_time;

    @BindView(R.id.txt_customer_name)
    TextView txt_customer_name;

    @BindView(R.id.txt_customer_phone)
    TextView txt_customer_phone;

    @BindView(R.id.txt_total_price)
    TextView txt_total_price;

     @BindView(R.id.btn_confirm)
     Button btn_confirm;

     HashSet<BarberServices> servicesAdded;
//     List<ShoppingItem> shoppingItemList;

     IFCMService ifcmService;
//     IBottomSheetDialogOnDismissListener iBottomSheetDialogOnDismissListener;

     AlertDialog dialog;

     String image_url;

     private static TotalPriceFragment instance;

//    public TotalPriceFragment() {
//        this.iBottomSheetDialogOnDismissListener = iBottomSheetDialogOnDismissListener;
//    }

    public static TotalPriceFragment getInstance() {
        return instance == null ? new TotalPriceFragment() : instance;
    }

    public TotalPriceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_total_price, container, false);

//        unbinder =
//        ButterKnife.bind(this, itemView);

        unbinder = ButterKnife.bind(this, itemView);

        init();
        initView();

        getBundle(getArguments());

        setInformation();

        return itemView;
    }

    private void setInformation() {

        txt_salon_name.setText(Common.selectedSalon.getName());
        txt_barber_name.setText(Common.currentBarber.getName());
        txt_time.setText(Common.convertTimeSlotToString((int)Common.currentBookingInformation.getSlot()));
        txt_customer_name.setText(Common.currentBookingInformation.getCustomerName());
        txt_customer_phone.setText(Common.currentBookingInformation.getCustomerPhone());
        
        if(servicesAdded.size() > 0)
        {
            int i=0;
            for(BarberServices services : servicesAdded)
            {
                Chip chip = (Chip)getLayoutInflater().inflate(R.layout.chip_item, null);
                chip.setText(services.getName());
                chip.setTag(i);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        servicesAdded.remove((int)view.getTag());
                        chip_group_services.removeView(view);
                        
                        
                        calculatePrice();
                    }
                });
                
                chip_group_services.addView(chip);

                i++;
                
            }
        }

        if(Common.currentBookingInformation.getCartItemList() != null)
        {
            if(Common.currentBookingInformation.getCartItemList().size() > 0)
            {
                MyConfirmationShoppingItemAdapter adapter = new MyConfirmationShoppingItemAdapter(getContext(), Common.currentBookingInformation.getCartItemList());
                recycler_view_shopping.setAdapter(adapter);
            }

            calculatePrice();
        }
    }

    private double calculatePrice() {
        double price = Common.DEFAULT_PRICE;
        for (BarberServices services: servicesAdded)
            price+=services.getPrice();

        if(Common.currentBookingInformation.getCartItemList() != null)
        {
            for (CartItem cartItem: Common.currentBookingInformation.getCartItemList())
                price+=(cartItem.getProductPrice() * cartItem.getProductQuantity());
        }

        txt_total_price.setText(new StringBuilder(Common.MONEY_SIGN)
        .append(price));

        return price;

    }

    private void getBundle(Bundle arguments) {
        this.servicesAdded = new Gson()
                .fromJson(arguments.getString(Common.SERVICES_ADDED), new TypeToken<HashSet<BarberServices>>(){}.getType());

//        this.shoppingItemList = new Gson()
//                .fromJson(arguments.getString(Common.SHOPPING_LIST), new TypeToken<List<ShoppingItem>>(){}.getType());

        image_url = arguments.getString(Common.IMAGE_DOWNLOADABLE_URL);
}

    private void initView() {
        recycler_view_shopping.setHasFixedSize(true);
        recycler_view_shopping.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                //Update, Booking Information Set done=true
                //AllSalon/Ahmedabad/Branch/k6KHTxBG3w2SChaVtW4Y/Barbers/nHZSPpRKCAz4y2wewf8l/13_12_2019/17
                DocumentReference bookingSet = FirebaseFirestore.getInstance()
                        .collection("AllSalon")
                        .document(Common.state_name)
                        .collection("Branch")
                        .document(Common.selectedSalon.getSalonId())
                        .collection("Barbers")
                        .document(Common.currentBarber.getBarberId())
                        .collection(Common.simpleDateFormat.format(Common.bookingDate.getTime()))
                        .document(Common.currentBookingInformation.getBookingId());

                bookingSet.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    if(task.getResult().exists()){
                                        //Update
                                        Map<String, Object> dataUpdate = new HashMap<>();
                                        dataUpdate.put("done", true);
                                        bookingSet.update(dataUpdate)
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        dialog.dismiss();
                                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    //If Update is done, Create Invoice
                                                    createInvoice();
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private void createInvoice() {
        CollectionReference invoiceRef = FirebaseFirestore.getInstance()
                .collection("AllSalon")
                .document(Common.state_name)
                .collection("Branch")
                .document(Common.selectedSalon.getSalonId())
                .collection("Invoices");

        Invoice invoice = new Invoice();

        invoice.setBarberId(Common.currentBarber.getBarberId());
        invoice.setBarberName(Common.currentBarber.getName());

        invoice.setSalonId(Common.selectedSalon.getSalonId());
        invoice.setSalonName(Common.selectedSalon.getName());
        invoice.setSalonAddress(Common.selectedSalon.getAddress());

        invoice.setCustomerName(Common.currentBookingInformation.getCustomerName());
        invoice.setCustomerPhone(Common.currentBookingInformation.getCustomerPhone());

        invoice.setImageUrl(image_url);

        invoice.setBarberServices(new ArrayList<BarberServices>(servicesAdded));
        invoice.setShoppingItemList(Common.currentBookingInformation.getCartItemList());
        invoice.setFinalPrice(calculatePrice());

        invoiceRef.document()
                .set(invoice)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    sendNotificationUpdateToUser(Common.currentBookingInformation.getCustomerPhone());
                }
            }
        });


    }

    private void sendNotificationUpdateToUser(String customerPhone) {
        //Get Token of User First
        FirebaseFirestore.getInstance()
                .collection("Tokens")
                .whereEqualTo("userPhone", customerPhone)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult().size() > 0){
                            MyToken myToken = new MyToken();
                            for(DocumentSnapshot tokenSnapshot : task.getResult())
                                myToken = tokenSnapshot.toObject(MyToken.class);

                            //Create Notification to Send
                            FCMSendData fcmSendData = new FCMSendData();
                            Map<String, String> dataSend = new HashMap<>();
                            dataSend.put("update_done", "true");

                            //Information need for Rating
                            dataSend.put(Common.RATING_STATE_KEY, Common.state_name);
                            dataSend.put(Common.RATING_SALON_ID, Common.selectedSalon.getSalonId());
                            dataSend.put(Common.RATING_SALON_NAME, Common.selectedSalon.getName());
                            dataSend.put(Common.RATING_BARBER_ID, Common.currentBarber.getBarberId());


                            fcmSendData.setTo(myToken.getToken());
                            fcmSendData.setData(dataSend);

                            ifcmService.sendNotification(fcmSendData)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.newThread())
                                    .subscribe(new Consumer<FCMResponse>() {
                                        @Override
                                        public void accept(FCMResponse fcmResponse) throws Exception {
                                            dialog.dismiss();
                                            dismiss();
                                            //Here, We just Post Event
                                            EventBus.getDefault().postSticky(new DismissFromBottomSheetEvent(true));
//                                            iBottomSheetDialogOnDismissListener.onDismissBttomSheetDialog(true);

                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(Throwable throwable) throws Exception {
                                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    }
                });
    }

    private void init() {
        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        ifcmService = RetrofitClient.getInstance().create(IFCMService.class);

    }
}
