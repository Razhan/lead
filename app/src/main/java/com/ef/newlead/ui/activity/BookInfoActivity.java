package com.ef.newlead.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.RelativeLayout;

import com.ef.newlead.R;
import com.ef.newlead.domain.usecase.CollectInfoUseCase;
import com.ef.newlead.presenter.CollectInfoPresenter;
import com.ef.newlead.ui.fragment.collectInfo.AgeFragment;
import com.ef.newlead.ui.fragment.collectInfo.NameFragment;
import com.ef.newlead.ui.fragment.collectInfo.NumberFragment;
import com.ef.newlead.ui.fragment.collectInfo.VerificationFragment;
import com.ef.newlead.ui.view.CollectInfoView;

import butterknife.BindView;

public class BookInfoActivity extends BaseMVPActivity<CollectInfoPresenter>
        implements ActivityCompat.OnRequestPermissionsResultCallback, CollectInfoView,
        VerificationFragment.VerificationResultListener,
        AgeFragment.AgeSelectionListener,
        NumberFragment.PhoneNumberInputListener, NameFragment.NameInputListener {

    public static final int TYPE_AGE = 1;
    public static final int TYPE_NAME = 2;
    public static final int TYPE_PHONE = 3;
    public static final String AGE = "age";
    public static final String PHONE = "phone";
    public static final String NAME = "name";

    @BindView(R.id.personal_info_parent)
    RelativeLayout topLayout;

    private FragmentManager fragmentManager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fullScreen = true;
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int type = intent.getIntExtra("type", -1);

        fragmentManager = getSupportFragmentManager();
        switch (type) {
            case TYPE_AGE:
                AgeFragment ageFragment = AgeFragment.newInstance(true);
                ageFragment.setAgeSelectionListener(this);
                fragment = ageFragment;
                break;

            case TYPE_NAME:
                NameFragment nameFragment = new NameFragment();
                nameFragment.setNameInputListener(this);
                fragment = nameFragment;
                break;

            case TYPE_PHONE:
                NumberFragment numberFragment = NumberFragment.newInstance(true);
                numberFragment.setPhoneNumberInputListener(this);
                fragment = numberFragment;
                break;
        }

        if (savedInstanceState == null)
            fragmentManager.beginTransaction().add(R.id.personal_info_parent, fragment).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (fragment != null) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_book_info;
    }

    @NonNull
    @Override
    protected CollectInfoPresenter createPresenter() {
        return new CollectInfoPresenter(this, this, new CollectInfoUseCase());
    }

    @Override
    public void onAge(String value) {
        Intent intent = new Intent();
        intent.putExtra(AGE, value);
        setSuccessfulResult(intent);
    }

    protected void setSuccessfulResult(Intent intent) {
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void afterSubmitInfo() {
        /** none logic **/
    }

    @Override
    public void onInputComplete(String phone) {
        VerificationFragment fragment = (VerificationFragment) CollectInfoActivity.getVerificationFragment(true, phone);
        fragment.setVerificationResultListener(this);

        fragmentManager.beginTransaction().replace(R.id.personal_info_parent, fragment).commit();
    }

    @Override
    public void onPhoneNumVerified(String phoneNum) {
        Intent intent = new Intent();
        intent.putExtra(PHONE, phoneNum);
        setSuccessfulResult(intent);
    }

    @Override
    public void onBackToEditPhone(String phoneNum) {
        NumberFragment numberFragment = NumberFragment.newInstance(true, phoneNum);
        numberFragment.setPhoneNumberInputListener(this);

        fragmentManager.beginTransaction().replace(R.id.personal_info_parent, fragment).commit();
    }

    @Override
    public void onNameInput(String name) {
        Intent intent = new Intent();
        intent.putExtra(NAME, name);
        setSuccessfulResult(intent);
    }
}
