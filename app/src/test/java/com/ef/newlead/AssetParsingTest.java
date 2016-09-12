package com.ef.newlead;

import android.graphics.Color;

import com.ef.newlead.data.model.GradientColor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Type;

/**
 * Created by seanzhou on 8/15/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class AssetParsingTest extends TestCase {

    @Test
    public void testParsingColor() throws Exception {
        String raw = "{\n" +
                "    \"top_gradient\": {\n" +
                "      \"r\": 139,\n" +
                "      \"g\": 109,\n" +
                "      \"b\": 178,\n" +
                "      \"a\": 1\n" +
                "    },\n" +
                "    \"bottom_gradient\": {\n" +
                "      \"r\": 200,\n" +
                "      \"g\": 109,\n" +
                "      \"b\": 215,\n" +
                "      \"a\": 1\n" +
                "    }\n" +
                "  }";
        Gson gson = new Gson();
        Type type = new TypeToken<GradientColor>() {
        }.getType();
        GradientColor background = gson.fromJson(raw, type);

        assertNotNull(background);


        int a = background.getTopGradient().getA();
        int r = background.getTopGradient().getR();
        int g = background.getTopGradient().getG();
        int b = background.getTopGradient().getB();

        int value = Color.argb(a, r, g, b);
        assertEquals(value, background.getTopGradient().toHex());

        assertEquals(255, a);
        assertEquals(139, r);
        assertEquals(109, g);
        assertEquals(178, b);

    }
}
