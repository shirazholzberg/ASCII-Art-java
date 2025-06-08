package image;

import java.awt.*;

/**
 * the class is responsible for padding a given image so its height and width is a power of 2.
 */
public class PaddingImage{
    private final Color[][] pixelPaddedArray;
    private final int paddedWidth;
    private final int paddedHeight;
    private final Image image; // the original image
    private static final Color WHITE_COLOR = new Color(255,255,255);

    /**
     * the constructor of the class - gets the original image
     * @param image the original image
     */
    public PaddingImage(Image image){
        paddedWidth = convertPowerOfTwo(image.getWidth());
        paddedHeight = convertPowerOfTwo(image.getHeight());
        pixelPaddedArray = new Color[paddedHeight][paddedWidth];
        this.image = image;
    }

    /**
     * the function gets the old dimensions
     * @param num a given number - the height / width of the original image
     * @return a new height / width - the closest number that is a power of 2.
     */
    private int convertPowerOfTwo(int num){
        double logBaseTwo = Math.log(num) / Math.log(2);
        if(Math.round(logBaseTwo) == logBaseTwo){
            return num;
        }
        return (int)Math.pow(2,Math.ceil(logBaseTwo));
    }

    /**
     * the function pads the original image with white color in the added pixels.
     * @return the new padded image
     */
    public Image padImage(){
        int deltaHeight = (int)Math.ceil((paddedHeight - image.getHeight())/2);
        int deltaWidth = (int)Math.ceil((paddedWidth - image.getWidth())/2);
        for( int row = 0; row <paddedHeight; row ++){
            for( int col = 0; col <paddedWidth; col ++){
                boolean isRowWhite = deltaHeight!=0 &&
                        ((row< deltaHeight)||(row>paddedHeight-1-deltaHeight));
                boolean isColWhite = deltaWidth!=0 &&
                        ((col< deltaWidth)||(col>paddedWidth-1-deltaWidth));
                if(isRowWhite||isColWhite){
                    pixelPaddedArray[row][col] = WHITE_COLOR;
                }
                else {
                    pixelPaddedArray[row][col] = image.getPixel(row-deltaHeight, col-deltaWidth);
                }
            }
        }
        return new Image(pixelPaddedArray, paddedWidth, paddedHeight);
    }
}