package com.ef.cat.injector.modules;

import com.ef.cat.injector.PerActivity;
import com.ef.cat.usecase.InitializationUseCase;
import com.ran.delta.domain.usecase.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class InitializationModule {

    @Provides
    @PerActivity
    @Named("Initialization")
    UseCase provideNewsListUseCase(InitializationUseCase useCase) {
        return useCase;
    }

}