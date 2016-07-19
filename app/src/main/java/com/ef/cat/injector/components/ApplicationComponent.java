package com.ef.cat.injector.components;

import android.content.Context;

import com.ef.cat.data.repostory.RestfulService;
import com.ef.cat.injector.modules.ApplicationModule;
import com.ef.cat.utils.SystemText;
import com.ran.delta.domain.exception.ErrorMessageFactory;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Context getContext();

    RestfulService getRestfulService();

    ErrorMessageFactory getErrorMessageFactory();

    SystemText getSystemText();

}
