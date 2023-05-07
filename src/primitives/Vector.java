package primitives;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Vector class extending Point class,
 * represents a vector in 3D Cartesian coordinate system
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class Vector extends Point {
    /**
     * constructor
     * @param x first coordinate
     * @param y second coordinate
     * @param z third coordinate
     */
    public Vector(double x, double y, double z){
        super(x,y,z);
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("creation of Vector (0,0,0)");
    }

    /***
     * constructor creates vector from Double3
     * @param xyz coordinates of the vector
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if(this.xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("creation of Vector (0,0,0)");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return xyz.equals(vector.xyz);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "_xyz=" + xyz +
                '}';
    }

    public Vector add(Vector other)
    {
        return new Vector(xyz.add(other.xyz));
    }


    /**
     *
     * @param s is a scalar
     * @return a new Vector which is scaled
     */
    public Vector scale(double s){
        return new Vector(xyz.scale(s));
    }

    /**
     * vector length squared
     * @return returns distance
     */
    public double lengthSquared() {
        return xyz.d1 * xyz.d1 + xyz.d2* xyz.d2 + xyz.d3* xyz.d3;
    }

    /**
     * cartesian length of the vector
     * @return the length as a double
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * scalar product of two vectors
     * @param other the second vector
     * @return resulting scalar
     */
    public double dotProduct(Vector other) {
        return xyz.d1*other.xyz.d1+ xyz.d2*other.xyz.d2+ xyz.d3*other.xyz.d3;
    }

    /**
     *
     * @param other second vector
     * @return new Vector resulting from cross product
     *(ax,ay,az)x(bx,by,bz)=(aybz-azby,azbx-axbz,axby-aybx)
     */
    public Vector crossProduct(Vector other) {
        double u1 = xyz.d1;
        double u2 = xyz.d2;
        double u3 = xyz.d3;

        double v1 = other.xyz.d1;
        double v2 = other.xyz.d2;
        double v3 = other.xyz.d3;

        return new Vector(
                u2*v3 - u3*v2,
                u3*v1 - u1*v3,
                u1*v2 - u2*v1
        );

    }

    /**
     *
     * @return new vector which is the original vector normalized
     */
    public Vector normalize() {
        double size =length();

        return new Vector(
                xyz.d1/size,
                xyz.d2/size,
                xyz.d3/size
        );
    }
}
