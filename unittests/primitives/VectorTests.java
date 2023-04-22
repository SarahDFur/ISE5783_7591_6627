package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static primitives.Util.isZero;

/**
 * Unit tests for primitives.Vector class
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */

public class VectorTests {

    /**
     * Test method for {@link primitives.Vector#add(Vector)}.
     * Checking BVA & EP between 2 vectors (additions)
     */
    @Test
    public void testAdd() {
        Vector v1 = new Vector(1,1,1);
        Vector v2 = new Vector (-1,-1,-1);
        Vector v3 = new Vector (-5,-6,-7);
        Vector v4 = new Vector(2, 4, 6);

        // =============== Boundary Values Tests ==================
        // BVA1: Addition results in vector 0
        assertThrows(IllegalArgumentException.class,()->v1.add(v2), "Addition results in vector 0");
        // BVA2: Addition with 0
        assertThrows(IllegalArgumentException.class,()->v1.add(new Vector(0,0,0)), "Addition with vector 0");

        // ============ Equivalence Partitions Tests ==============
        // EP1: Result is positive
        assertEquals(new Vector(3,5,7), v4.add(v1), "Error in addition calculation");
        // EP2: Result is negative
        assertEquals(new Vector(-3,-2,-1), v4.add(v3), "Error in addition calculation");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     * Checks scalar multiplication
     */
    @Test
    public void testScale() {
        Vector v1 = new Vector(1,1,1);
        Vector v2 = new Vector(-1,-1,-1);

        // =============== Boundary Values Tests ==================
        // BVA1: Scaled with 0
        assertThrows(IllegalArgumentException.class, ()->v1.scale(0), "Scaling with 0 failed");

        // ============ Equivalence Partitions Tests ==============
        // EP1: Scaled with positive number
        assertEquals(new Vector(3,3,3),v1.scale(3),"Scaling with positive number failed");
        // EP2: Scaled with negative number
        assertEquals(new Vector(-3,-3,-3),v1.scale(-3),"Scaling with negative number failed");
        // EP3: scale vector with negative values
        assertEquals(new Vector(-3,-3,-3),v2.scale(3),"Scaling vector with negative values failed");
    }

    /**
     * Test method for {@link Vector#lengthSquared()}.
     * Checks that the method for length (squared) works
     */
    @Test
    public void testLengthSquared() {
        Vector v1 = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        // EP1: Length squared is calculated correctly
        assertTrue(isZero(v1.lengthSquared() - 14),"ERROR: lengthSquared() wrong value");
    }

    /**
     * Test method for {@link Vector#length()}.
     * Checks that correct length is calculated
     */
    @Test
    public void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // EP1: Length is calculated correctly
        assertTrue(isZero(new Vector(0, 3, 4).length() - 5),"ERROR: length() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(Vector)}.
     * Checks if the dot product is correct
     */
    @Test
    public void testDotProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);

        // =============== Boundary Values Tests ==================
        // BVA1: dot product between orthogonal vectors
        assertTrue(isZero(v1.dotProduct(v3)),"ERROR: dotProduct() for orthogonal vectors is not zero");

        // ============ Equivalence Partitions Tests ==============
        // EP1: checking that dot product is the correct value
        assertTrue(isZero(v1.dotProduct(v2) + 28), "ERROR: dotProduct() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(Vector)}.
     * Checks that the cross product is correct
     */
    @Test
    public void testCrossProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);

        // =============== Boundary Values Tests ==================
        // BVA1: two vectors are parallel
        assertThrows(IllegalArgumentException.class, ()->v1.crossProduct(v2), "ERROR: crossProduct() for parallel vectors does not throw an exception" );

        // ============ Equivalence Partitions Tests ==============
        Vector vr = v1.crossProduct(v3);
        // EP1.1: checking correct length for vr
        assertTrue(isZero(vr.length() - v1.length() * v3.length()),"ERROR: crossProduct() wrong result length");
        // EP1.2: checking vr is orthogonal to v1 and v3
        assertTrue(isZero(vr.dotProduct(v1)) || !isZero(vr.dotProduct(v3)), "ERROR: crossProduct() result is not orthogonal to its operands");
    }

    /**
     * Test method for {@link Vector#normalize()}.
     * Checks that normalize() functions properly
     */
    @Test
    public void testNormalize() {
        Vector v = new Vector(0, 0, 9);
        Vector normalized = v.normalize();

        // =============== Boundary Values Tests ==================
        // BVA.1 checking that the length of the normalized vector is 1
        assertTrue(isZero(normalized.length() - 1),"ERROR: the normalized vector is not a unit vector");
        // BVA.2 checking that the normalized vector is parallel to the original vector
        assertThrows(IllegalArgumentException.class, ()->v.crossProduct(normalized), "ERROR: the normalized vector is not parallel to the original one");
        // BVA.3 checking that the normalized vector is in the same direction as the original vector
        assertTrue(v.dotProduct(normalized) > 0, "ERROR: the normalized vector is opposite to the original one");

        // ============ Equivalence Partitions Tests ==============
        // EP1: checking that the vector was normalized correctly
        assertEquals(new Vector (0,0,1), normalized, "Vector was normalized incorrectly");
    }
}