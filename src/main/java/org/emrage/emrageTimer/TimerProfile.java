package org.emrage.emrageTimer;

public class TimerProfile {
    private String name;
    private String gradientColor1;
    private String gradientColor2;

    public TimerProfile(String name, String gradientColor1, String gradientColor2) {
        this.name = name;
        this.gradientColor1 = gradientColor1;
        this.gradientColor2 = gradientColor2;
    }

    public String getName() {
        return name;
    }

    public String getGradientColor1() {
        return gradientColor1;
    }

    public String getGradientColor2() {
        return gradientColor2;
    }
}