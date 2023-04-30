package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight {
    private Color intensity;
    static Color NONE = Color.BLACK;

    //-----------------------
    private Color Ia;
    private Double3 Ka = Double3.ZERO;
    //-----------------------

    public AmbientLight(Color ia, Double3 ka) {
        Ia = ia;
        Ka = ka;
    }

    public AmbientLight(Color intensity, Color ia, Double3 ka) {
        this.intensity = intensity;
        Ia = ia;
        Ka = ka;
    }

    public AmbientLight(Double3 ka) {
        Ka = ka;
    }
}
