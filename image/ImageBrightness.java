package image;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * the class in responsible for calculating the brightness of each sub-image
 * and saves it in a database by the image and resolution.
 */
public class ImageBrightness {
    private static Map<Image,Map<Integer,double[][]>> brightnessMap= new HashMap<>();
    private final Image image;
    private final int resolution;

    /**
     * the constructor - given the original image and tha wanted resolution
     * @param image the original image
     * @param resolution given resolution
     */
    public ImageBrightness(Image image, int resolution){
        this.image = image;
        this.resolution = resolution;
    }

    /**
     * the function calculates the gray scale of each pixel in each sub picture
     * @param curSubImage the current sub image to calculate
     * @return the normalized gary scale of the whole sub picture
     */
    private double grayScaleSubImage(Image curSubImage){
        double sumGraySubImage = 0;
        int numPixel = curSubImage.getHeight()*curSubImage.getWidth();
        for (int row = 0; row < curSubImage.getHeight(); row++) {
            for (int col = 0; col <curSubImage.getWidth(); col++) {
                Color currenPixel = curSubImage.getPixel(row, col);
                double greyPixel = currenPixel.getRed() * 0.2126 + currenPixel.getGreen() * 0.7152
                        + currenPixel.getBlue() * 0.0722;
                sumGraySubImage+= greyPixel;
            }
        }
        return (sumGraySubImage/(numPixel)/255);
    }

    /**
     * the function calculates the normalized gray scale of each sub-picture
     * @return an array of the sub pictures normalized gray scale
     */
    private double[][] fillBrightnessMap(){
        DividedImage dividedImage = new DividedImage(resolution, image);
        int numSub =0;
        double[][] greyImage = new double[dividedImage.getHeight()][dividedImage.getWidth()];
        double GraySubImage;
        for (int row = 0; row < dividedImage.getHeight(); row++) {
            for (int col = 0; col < dividedImage.getWidth(); col++) {
                Image curSubImage = dividedImage.getSubImage(row,col);
                numSub++;
                if(numSub>=0) {
                    GraySubImage = grayScaleSubImage(curSubImage);
                    greyImage[row][col] = GraySubImage;
                }
            }
        }
        //updating the map that saves all
        if(!brightnessMap.containsKey(image)){
            brightnessMap.put(image, new HashMap<Integer,double[][]>());
        }
        brightnessMap.get(image).put(resolution, greyImage);
        return greyImage;
    }

    /**
     * the function checks if the given resolution and image are already exists in the map- if so returns
     * the saved array of brightness else, calculates the brightness.
     * @return an array of brightness.
     */
    public double[][] calculateBrightness(){
        if(brightnessMap.containsKey(image)) {
            Map<Integer, double[][]> curResolutionMap = brightnessMap.get(image);
            if(curResolutionMap.containsKey(resolution)){
                double[][] brightnessArray = curResolutionMap.get(resolution);
                if (brightnessArray!= null) {
                    return brightnessArray;
                }
            }
        }
        return fillBrightnessMap();
    }
}