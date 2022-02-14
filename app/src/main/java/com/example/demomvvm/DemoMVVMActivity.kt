package com.example.demomvvm

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.common.quick.mvvm.QuickBaseActivity
import com.common.quick.mvvm.QuickBaseModel
import com.common.quick.mvvm.QuickBaseViewModel
import com.example.demomvvm.databinding.DemoActLayoutBinding

//M V VM

class DemoModel(application: Application?) : QuickBaseModel(application) {
}

class DemoViewModel(app: Application?) : QuickBaseViewModel<DemoModel>(app) {

    val demoText: MutableLiveData<String> = MutableLiveData<String>("Demo Text");

    fun updateMonitor() {
        Log.e("Fish", "updateMonitor")
        demoText.value = System.currentTimeMillis().toString();
    }
}

class DemoMVVMActivity : QuickBaseActivity<DemoActLayoutBinding, DemoViewModel>() {
}