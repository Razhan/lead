package com.ef.newlead.data.model;

import com.google.gson.annotations.SerializedName;

public class GradientColor {

    @SerializedName("top_gradient")
    private GradientBean topGradient;

    @SerializedName("bottom_gradient")
    private GradientBean bottomGradient;

    private int order;

    public GradientBean getTopGradient() {
        return topGradient;
    }

    public void setTopGradient(GradientBean top_gradient) {
        this.topGradient = top_gradient;
    }

    public GradientBean getBottomGradient() {
        return bottomGradient;
    }

    public void setBottomgradient(GradientBean bottom_gradient) {
        this.bottomGradient = bottom_gradient;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public static class GradientBean {
        private int r;
        private int g;
        private int b;
        private int a;

        public int getR() {
            return r;
        }

        public void setR(int r) {
            this.r = r;
        }

        public int getG() {
            return g;
        }

        public void setG(int g) {
            this.g = g;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int toHex() {
            return (int) Long.parseLong(String.format("%02x%02x%02x%02x", a, r, g, b), 16);
        }
    }
}
