package com.ef.newlead.domain.usecase;

import com.ef.newlead.ErrorHandler;
import com.ef.newlead.data.repostory.Repository;
import com.ef.newlead.data.repostory.RepositoryImp;
import com.ef.newlead.util.UseCaseUtils;

import java.lang.reflect.Method;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public abstract class UseCase {

    protected Repository repository;
    private String methodName;
    private Object[] methodArgs;
    private Action1 onSuccessCallback;
    private Action1<Throwable> onErrorCallback;
    private Action0 onCompleteCallback;
    private CompositeSubscription compositeSubscription;

    public UseCase() {
        repository = RepositoryImp.getInstance();
        compositeSubscription = new CompositeSubscription();
    }

    private void reset() {
        methodArgs = null;
        methodName = null;
        onSuccessCallback = Actions.empty();
        onErrorCallback = ErrorHandler::showError;
        onCompleteCallback = Actions.empty();
    }

    protected Observable buildUseCaseObservable() {
        Method methodToInvoke = UseCaseUtils.findMethod(this, methodName, methodArgs);
        try {
            return (Observable) methodToInvoke.invoke(this, methodArgs);
        } catch (Exception e) {
            return Observable.empty();
        }
    }

    @SuppressWarnings("unchecked")
    public void execute() {
        Subscription subscription = this.buildUseCaseObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccessCallback, onErrorCallback, onCompleteCallback);

        compositeSubscription.add(subscription);
    }

    public void unsubscribe() {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    public final class Builder<T> {
        public Builder() {
            reset();
        }

        public Builder<T> useCaseMethod(String name) {
            methodName = name;
            return this;
        }

        public Builder<T> useCaseArgs(Object... args) {
            methodArgs = args;
            return this;
        }

        public Builder<T> onSuccess(Action1<T> successCallback) {
            UseCase.this.onSuccessCallback = successCallback;
            return this;
        }

        public Builder<T> onComplete(Action0 completeCallback) {
            UseCase.this.onCompleteCallback = completeCallback;
            return this;
        }

        public Builder<T> onError(Action1<Throwable> errorCallback) {
            if (errorCallback == null) {
                return this;
            }

            UseCase.this.onErrorCallback = errorCallback;
            return this;
        }

        public void build() {
            UseCase.this.execute();
        }
    }

}

