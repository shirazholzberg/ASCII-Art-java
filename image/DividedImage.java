package image;

import java.awt.*;

/**
 * a class that take care of dividing the image according to the resolution.
 * the class members are the wanted resolution, the original picture and an array of the divided image.
 */
public class DividedImage {
    private final int resolution;// number of sub-pictures in a row
    private final Image image;
    private Image[][] subPictures;

    /**
     * the constructor
     * @param resolution the given resolution
     * @param image the original image
     */
    public DividedImage(int resolution, Image image) {
        this.resolution = resolution;
        this.image = image;
        this.subPictures = null;
        makeSubPictures();
    }

    /**
     * the function fills the array subPictures by dividing the original image according to the resolution
     * and saves each parted image in subPictures.
     */
    private void makeSubPictures(){
        int subSize = image.getWidth()/resolution;
        subPictures = new Image[image.getHeight()/subSize][resolution];
        for (int row = 0; row < image.getHeight(); row+=subSize) {
            for (int col = 0; col < image.getWidth(); col+=subSize) {
                Color[][] subPixelsArray = new Color[subSize][subSize];
                for (int i = 0; i < subSize; i++) {
                    for (int j = 0; j < subSize; j++) {
                        subPixelsArray[i][j] = image.getPixel(row+i, col+j);
                    }
                }
                Image subImage = new Image(subPixelsArray, subSize, subSize);
                subPictures[row/subSize][col/subSize] = subImage;
            }
        }
    }

    /**
     * the function returns a sub-image from the array subPictures according to the given row and col
     * @param row row number
     * @param col col number
     * @return the sub-image in the place (row, col)
     */
    public Image getSubImage(int row, int col){
        if(subPictures!= null && row >=0 && row<subPictures.length && col >=0 && col<subPictures[0].length) {
            return subPictures[row][col];
        }
        return null;
    }

    /**
     * getter of height
     * @return the function returns the height of the array subPictures
     */
    public int getHeight(){
        if(subPictures == null){
            return 0;
        }
        return subPictures.length;
    }

    /**
     * getter of width
     * @return the function returns the width of the array subPictures
     */
    public int getWidth(){
        if(subPictures == null){
            return 0;
        }
        return subPictures[0].length;
    }
}