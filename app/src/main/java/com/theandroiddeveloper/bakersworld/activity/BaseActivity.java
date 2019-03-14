package com.theandroiddeveloper.bakersworld.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.theandroiddeveloper.bakersworld.dialog.ProgressDialog;

import io.realm.Realm;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
    }

    public void showProgressDialog() {
        progressDialog.show();
    }

    public void hideProgressDialog() {
        progressDialog.hide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onStop() {
        super.onStop();
        realm.close();
    }

    public Realm getRealmInstance() {
        return realm;
    }
}
