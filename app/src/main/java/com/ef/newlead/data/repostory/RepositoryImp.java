package com.ef.newlead.data.repostory;

import com.ef.newlead.data.model.DataBean.ResourceBean;
import com.ef.newlead.data.model.DataBean.UserBean;
import com.ef.newlead.data.model.Response;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;

public class RepositoryImp implements Repository {

    private RestfulDataSource restfulData;
    private LocalDataSource localData;
    private static RepositoryImp instance;

    private RepositoryImp(RestfulDataSource restfulData, LocalDataSource LocalData) {
        this.restfulData = restfulData;
        this.localData = LocalData;
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new RepositoryImp(new RestfulDataSource(), new LocalDataSource());
        }

        return instance;
    }

    private String getMethodName() {
        return Thread.currentThread().getStackTrace()[3].getMethodName();
    }

    @Override
    public Observable<ResponseBody> downloadFile(String url) {
        return this.<ResponseBody>execute(getMethodName(), url);
    }

    @Override
    public Observable<Response<ResourceBean>> resourceInfo() {
        return this.<Response<ResourceBean>>execute(getMethodName());
    }

    @Override
    public Observable<Response<UserBean>> getUserInfo(String device, String campaign,
                                                      String source, String appStore) {
        return this.<Response<UserBean>>execute(getMethodName(), device, campaign, source, appStore);
    }

    @SuppressWarnings("unchecked")
    private <T> Observable<T> execute(String methodName, Object... args) {
        try {
            List<Class> parameterTypes = new ArrayList<>();

            for (Object arg : args) {
                parameterTypes.add(arg.getClass());
            }

            Class[] types = parameterTypes.toArray(new Class[parameterTypes.size()]);

            Method localMethod = localData.getClass().getMethod(methodName, types);
            Method restfulMethod = restfulData.getClass().getMethod(methodName, types);

            return Observable.concat(
                        (Observable<T>)localMethod.invoke(localData, args),
                        (Observable<T>)restfulMethod.invoke(restfulData, args))
                    .filter(response -> response != null)
                    .first();

        } catch (Exception e) {
            return Observable.empty();
        }
    }

}
