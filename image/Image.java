package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * A package-private class of the package image.
 * @author Dan Nirel
 */
public class Image {

    private final Color[][] pixelArray;
    private final int width;
    private final int height;

    /**
     * Constructor
     * @param filename name
     * @throws IOException exception
     */
    public Image(String filename) throws IOException {
        BufferedImage im = ImageIO.read(new File(filename));
        width = im.getWidth();
        height = im.getHeight();


        pixelArray = new Color[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelArray[i][j]=new Color(im.getRGB(j, i));
            }
        }
    }

    /**
     * constructor
     * @param pixelArray 2 dimensional color array
     * @param width int
     * @param height int
     */
    public Image(Color[][] pixelArray, int width, int height) {
        this.pixelArray = pixelArray;
        this.width = width;
        this.height = height;
    }

    /**
     * getter
     * @return int width
     */
    public int getWidth() {
        return width;
    }

    /**
     * getter
     * @return int height
     */
    public int getHeight() {
        return height;
    }

    /**
     * getter
     * @param x row
     * @param y col
     * @return pixel in row X col
     */
    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }

    /**
     * saving image
     * @param fileName name of the file
     */
    public void saveImage(String fileName){
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(pixelArray[0].length, pixelArray.length,
                BufferedImage.TYPE_INT_RGB);
        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < pixelArray.length; x++) {
            for (int y = 0; y < pixelArray[x].length; y++) {
                bufferedImage.setRGB(y, x, pixelArray[x][y].getRGB());
            }
        }
        File outputfile = new File(fileName+".jpeg");
        try {
            ImageIO.write(bufferedImage, "jpeg", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * equals
     * @param obj object to compare
     * @return true if equal objects
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Image)){
            return false;
        }
        Image otherImage= (Image)obj;
        if(otherImage.getWidth()!= width || otherImage.getHeight()!=height){
            return false;
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(!otherImage.getPixel(i,j).equals(pixelArray[i][j])){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Hash code override
     * @return a hash number
     */
    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(pixelArray),width,height);
    }
}
