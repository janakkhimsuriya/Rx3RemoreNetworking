package com.janak.rxandroidnetworking;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.janak.androidnetworking.networking.Rx3RemoteNetworking;
import com.janak.rxandroidnetworking.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DownloadImageActivity extends AppCompatActivity {

    private static final String TAG = DownloadImageActivity.class.getSimpleName();
    private static final String URL = "http://i.imgur.com/AtbX9iX.png";
    private String dirPath;
    private String fileName = "imgurimage.png";
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_image);
        dirPath = Utils.getRootDirPath(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    public Completable getCompletable() {
        return Rx3RemoteNetworking.download(URL, dirPath, fileName)
                .build()
                .getDownloadCompletable();
    }

    private DisposableCompletableObserver getDisposableObserver() {

        return new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.d(TAG, "onError " + throwable.getMessage());
            }
        };

    }

    public void downloadFile(View view) {
        disposables.add(getCompletable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getDisposableObserver()));
    }
}