package com.ef.cat.data.repostory;

import com.ran.delta.data.Repository;

import javax.inject.Inject;

public class CatRepository implements Repository {

    private final RestfulService restfulService;

    @Inject
    public CatRepository(RestfulService service) {
        restfulService = service;
    }

    public RestfulService getRestfulService() {
        return restfulService;
    }
}
