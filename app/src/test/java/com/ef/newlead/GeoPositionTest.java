package com.ef.newlead;

import com.ef.newlead.domain.location.GeoPosition;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created by seanzhou on 9/12/16.
 */
public class GeoPositionTest {
    @Test
    public void testEqual() throws Exception {
        GeoPosition gp = new GeoPosition(121.522778, 31.235551);
        GeoPosition gpCopy = new GeoPosition(121.522778, 31.235551);
        HashMap<GeoPosition, Integer> geoMap = new HashMap<>();
        geoMap.put(gpCopy, 0);
        geoMap.put(gp, 1);

        Assert.assertEquals(1, geoMap.size());
        Assert.assertTrue(geoMap.containsKey(gp));
        Assert.assertEquals(1, geoMap.get(gp).intValue());

    }
}
