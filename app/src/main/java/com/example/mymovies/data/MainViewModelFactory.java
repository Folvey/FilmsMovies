package com.example.mymovies.data;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mymovies.data.MainViewModel;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    Application mApplication;

    public MainViewModelFactory(Application mApplication) {
        this.mApplication = mApplication;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MainViewModel(mApplication);
    }
}
