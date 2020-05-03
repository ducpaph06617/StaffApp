package com.dev.staffapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.staffapp.Adapter.MyBarberAdapter;
import com.dev.staffapp.Common.SpacesItemDecoration;
import com.dev.staffapp.Model.EventBus.BarberDoneEvent;
import com.dev.staffapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentAccountLoaded extends Fragment {
    Unbinder unbinder;
//    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_acc)
    RecyclerView recycler_barber;

//    private BroadcastReceiver barberDoneReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            ArrayList<Barber> barberArrayList = intent.getParcelableArrayListExtra(Common.KEY_BARBER_LOAD_DONE);
//
//            //Create Adapter
//            MyBarberAdapter adapter = new MyBarberAdapter(getContext(), barberArrayList);
//            recycler_barber.setAdapter(adapter);
//        }
//    };

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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void setBarberAdapter(BarberDoneEvent event)
    {
        MyBarberAdapter adapter = new MyBarberAdapter(getContext(), event.getBarberList());
        recycler_barber.setAdapter(adapter);
    }

    //================================================================

    static FragmentAccountLoaded instance;

    public static FragmentAccountLoaded getInstance() {
        if(instance == null)
            instance = new FragmentAccountLoaded();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
//        localBroadcastManager.registerReceiver(barberDoneReceiver, new IntentFilter(Common.KEY_BARBER_LOAD_DONE));


    }

//    @Override
//    public void onDestroy() {
//        localBroadcastManager.unregisterReceiver(barberDoneReceiver);
//        super.onDestroy();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_account_loaded,container, false);

        unbinder = ButterKnife.bind(this, itemView);

        initView();
        return itemView;
    }

    private void initView() {
        recycler_barber.setHasFixedSize(true);
        recycler_barber.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycler_barber.addItemDecoration(new SpacesItemDecoration(4));
    }
}
