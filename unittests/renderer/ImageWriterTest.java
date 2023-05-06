package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static java.awt.Color.RED;
import static java.awt.Color.YELLOW;

public class ImageWriterTest {

    /**
     * Test method for {@link ImageWriter#writeToImage()} .
     * Make red grid with yellow background
     */
    @Test
    void testWriteToImage() {
        ImageWriter imageWriter = new ImageWriter("RedGridTest", 800, 500);

        for (int i = 0; i < 800; i++)
            for (int j = 0; j < 500; j++)
                if (i % 50 == 0 || j % 50 == 0)
                    imageWriter.writePixel(i, j, new Color(RED));
                else
                    imageWriter.writePixel(i, j, new Color(YELLOW));

        imageWriter.writeToImage();
    }
}
