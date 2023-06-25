package primitives;

/**
 * represents the material a Geometry is made of
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class Material {
    /**
     * diffuse factor
     */
    public Double3 kD = Double3.ZERO;
    /**
     * specular factor
     */
    public Double3 kS = Double3.ZERO;
    /**
     * reflection factor
     */
    public Double3 kR = Double3.ZERO;
    /**
     * transparency factor
     */
    public Double3 kT = Double3.ZERO;
    /**
     * diffusive glass coefficient
     */
    public double kDg = 0;
    /**
     * Glossy surface coefficient
     */
    public double kSg = 0;
    /**
     * shininess factor
     */
    public int nShininess = 0;

    /**
     * setter for kd
     * @param kD Double3 kd
     * @return this material
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }
    /**
     * setter for kd
     * @param kD double kd
     * @return this material
     */
    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }
    /**
     * setter for kS
     * @param kS Double3 ks
     * @return this material
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }
    /**
     * setter for ks
     * @param kS double ks
     * @return this material
     */
    public Material setKs(double kS) {
        this.kS = new Double3(kS);
        return this;
    }
    /**
     * setter for kr
     * @param kR Double3 kr
     * @return this material
     */
    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;
    }
    /**
     * setter for kr
     * @param kR double kr
     * @return this material
     */
    public Material setKr(double kR) {
        this.kR = new Double3(kR);
        return this;
    }
    /**
     * setter for kt
     * @param kT Double3 kt
     * @return this material
     */
    public Material setKt(Double3 kT) {
        this.kT = kT;
        return this;
    }
    /**
     * setter for kt
     * @param kT double kt
     * @return this material
     */
    public Material setKt(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Setter for the kDg field
     *
     * @param kDg double parameter value
     * @return The object itself
     */
    public Material setKdG(double kDg) {
        this.kDg = kDg;
        return this;
    }

    /**
     * Setter for the kSg field
     * @param kSg double parameter value
     * @return The object itself
     */
    public Material setKsG(double kSg) {
        this.kSg = kSg;
        return this;
    }

    /**
     * setter for shininess
     * @param nShininess int shininess
     * @return this material
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
