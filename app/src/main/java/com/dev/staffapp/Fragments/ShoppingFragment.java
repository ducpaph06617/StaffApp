package com.dev.staffapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.staffapp.Adapter.MyShoppingItemAdapter;
import com.dev.staffapp.Common.SpacesItemDecoration;
import com.dev.staffapp.Interface.IOnShoppingItemSelected;
import com.dev.staffapp.Interface.IShoppingDataLoadListener;
import com.dev.staffapp.Model.ShoppingItem;
import com.dev.staffapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ShoppingFragment extends BottomSheetDialogFragment implements IShoppingDataLoadListener, IOnShoppingItemSelected {

    Unbinder unbinder;

    CollectionReference shoppingItemRef;
    IShoppingDataLoadListener iShoppingDataLoadListener;

    IOnShoppingItemSelected callBackToActivity;

    @BindView(R.id.chip_group)
    ChipGroup chipGroup;
    @BindView(R.id.chip_wax)
    Chip chip_wax;
    @OnClick(R.id.chip_wax)
    void waxLoadClick()
    {
        seeSelectedChip(chip_wax);
        loadShoppingItem("Wax");
    }

    @BindView(R.id.chip_spray)
    Chip chip_spray;
    @OnClick(R.id.chip_spray)
    void chipLoadClick()
    {
        seeSelectedChip(chip_spray);
        loadShoppingItem("Spray");
    }


    @BindView(R.id.chip_hair_care)
    Chip chip_hair_care;
    @OnClick(R.id.chip_hair_care)
    void hairCareLoadClick()
    {
        seeSelectedChip(chip_hair_care);
        loadShoppingItem("HairCare");
    }

    @BindView(R.id.chip_body_care)
    Chip chip_body_care;
    @OnClick(R.id.chip_body_care)
    void bodyCareLoadClick()
    {
        seeSelectedChip(chip_body_care);
        loadShoppingItem("BodyCare");
    }

    @BindView(R.id.recycler_items)
    RecyclerView recycler_items;

    private static ShoppingFragment instance;

    public static ShoppingFragment getInstance(IOnShoppingItemSelected iOnShoppingItemSelected) {
        return instance==null ? new ShoppingFragment(iOnShoppingItemSelected) : instance;
    }

    private void loadShoppingItem(String itemMenu) {
        shoppingItemRef = FirebaseFirestore.getInstance()
                .collection("Shopping")
                .document(itemMenu)
                .collection("Items");

        //Get Data
        shoppingItemRef.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iShoppingDataLoadListener.onShoppingDataLoadFailed(e.getMessage());
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    List<ShoppingItem> shoppingItems =  new ArrayList<>();
                    for(DocumentSnapshot itemSnapShot : task.getResult())
                    {
                        ShoppingItem shoppingItem = itemSnapShot.toObject(ShoppingItem.class);
                        shoppingItem.setId(itemSnapShot.getId());
                        shoppingItems.add(shoppingItem);
                    }

                    iShoppingDataLoadListener.onShoppingDataLoadSuccess(shoppingItems);
                }
            }
        });
    }

    private void seeSelectedChip(Chip chip) {
        //Set Color
        for(int i=0; i<chipGroup.getChildCount();i++)
        {
            Chip chipItem = (Chip)chipGroup.getChildAt(i);
            if(chipItem.getId() != chip.getId()) //If-Not Selected
            {
                chipItem.setChipBackgroundColorResource(android.R.color.darker_gray);
                chipItem.setTextColor(getResources().getColor(android.R.color.white));
            }
            else
            {
                chipItem.setChipBackgroundColorResource(android.R.color.holo_orange_dark);
                chipItem.setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    public ShoppingFragment(IOnShoppingItemSelected callBackToActivity) {
        this.callBackToActivity = callBackToActivity;
    }

    public ShoppingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_shopping, container, false);
        unbinder = ButterKnife.bind(this, itemView);


        //Default Load
        loadShoppingItem("Wax");

        init();

        initView();

        return itemView;
    }

    private void initView() {
        recycler_items.setHasFixedSize(true);
        recycler_items.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycler_items.addItemDecoration(new SpacesItemDecoration(8));
    }

    private void init() {
        iShoppingDataLoadListener = this;
    }

    @Override
    public void onShoppingDataLoadSuccess(List<ShoppingItem> shoppingItemList) {
        MyShoppingItemAdapter adapter = new MyShoppingItemAdapter(getContext(), shoppingItemList, this);
        recycler_items.setAdapter(adapter);

    }

    @Override
    public void onShoppingDataLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShoppingItemSelected(ShoppingItem shoppingItem) {
        callBackToActivity.onShoppingItemSelected(shoppingItem);
    }
}
