package com.ef.newlead.ui.view;

public interface VerificationView extends View {

    void afterNumberSubmit(boolean isSucceed);

    void afterCodeVerified(boolean isSucceed);
}
