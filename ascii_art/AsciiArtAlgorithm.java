package ascii_art;

import image.*;
import image_char_matching.SubImgCharMatcher;

/**
 * A class that is responsible for tunning the ascii Art algorithm according to the image, SubImgCharMatcher
 * and resolution received
 */
public class AsciiArtAlgorithm {

    private int resolution;
    private Image image;
    private SubImgCharMatcher imageAsciiConvertor;

    /**
     * constructor
     * @param image - an image object to be converted
     * @param imageAsciiConvertor - contains the charset and the brightness values
     * @param resolution - the wanted resolution
     */
    public AsciiArtAlgorithm(Image image, SubImgCharMatcher imageAsciiConvertor, int resolution){
        this.resolution = resolution;
        this.image = image;
        this.imageAsciiConvertor = imageAsciiConvertor;

    }

    /**
     * Runs the algorithm - devide the image to sub-images, and finds the matching ascii char to replace a
     * sub image according to the calculated brightness
     * @return - a two-dimensional array of ascii chars that represents the image
     */
    public char[][] run(){
        ImageBrightness imageBrightness = new ImageBrightness(image, resolution);
        double[][] greyImages = imageBrightness.calculateBrightness();
        int rows = greyImages.length;
        int cols = greyImages[0].length;
        char[][] asciiArt = new char[rows][cols];
        for( int row =0 ; row < rows; row++){
            for (int col = 0; col < cols ; col++){
                asciiArt[row][col] = imageAsciiConvertor.getCharByImageBrightness(greyImages[row][col]);
            }
        }
        return asciiArt;
    }
}
