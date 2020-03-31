package com.dev.staffapp.Interface;

import com.dev.staffapp.Model.Salon;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Salon> branchList);
    void onBranchLoadFailed(String message);
}
