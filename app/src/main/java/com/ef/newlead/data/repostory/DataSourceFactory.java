package com.ef.newlead.data.repostory;

public class DataSourceFactory {

    private RestfulDataSource restfulData;
    private LocalDataSource localData;

    public DataSourceFactory() {
        this.localData = new LocalDataSource();
        this.restfulData = new RestfulDataSource();
    }

    public Repository getLocalSource() {
        return localData;
    }

    public Repository getRestfulSource() {
        return restfulData;
    }

    public Repository getPreferedSource() {
        return null;
    }

}
