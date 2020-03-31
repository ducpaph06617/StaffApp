package com.dev.staffapp.Interface;

import com.dev.staffapp.Model.BarberServices;

import java.util.List;

public interface IBarberServiceLoadListener {
    void onBarberServiceLoadSuccess(List<BarberServices> barberServicesList);
    void onBarberServiceFailed(String message);
}
