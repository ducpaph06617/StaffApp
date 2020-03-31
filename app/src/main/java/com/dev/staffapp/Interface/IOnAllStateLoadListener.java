package com.dev.staffapp.Interface;

import com.dev.staffapp.Model.City;

import java.util.List;

public interface IOnAllStateLoadListener {

    void onAllStateLoadSuccess(List<City> cityList);
    void onAllStateLoadFailed(String message);
}
