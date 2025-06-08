package image_char_matching;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/**
 * a class that calculates a normalize brightness value to an ascii char
 */
public class SubImgCharMatcher {

    private char[] charset;
    private static final int DEFAULT_PIXEL_RESOLUTION = 16;

    /**
     * contains pairs of char-brightness key and value, not normalized
     */
    private static HashMap<Character, Double> staticBrightnessMap = new HashMap<Character, Double>();

    /**
     * contain pairs of normalized char-brightness key and value
     */
    private HashMap<Character, Double> asciiMap;

    /**
     * Map with key=brightness and value=Set with all the ascii chars that have that brightness norma
     */
    private HashMap<Double, Set<Character>> brightnessValuesMap;
    private double minBrightness;
    private double maxBrightness;

    /**
     * A constructor for SubImgCharMatcher object
     * @param charset a list of ascii chars
     */
    public SubImgCharMatcher(char[] charset) {
        this.charset = charset;
        this.asciiMap = new HashMap<Character, Double>();
        Arrays.sort(charset);
        initializeMap();
        updateMixMaxValues();
        normalizeMap();
        createMapByBrightnessValues();
    }

    /**
     * Initialize the asciiMap - contain pairs of normalized char-brightness key and value
     */
    private void initializeMap() {
        for (char asciiChar : charset) {
            if (!staticBrightnessMap.containsKey(asciiChar)) {
                double charBrightness = calculateBrightness(asciiChar);
                staticBrightnessMap.put(asciiChar, charBrightness);
                asciiMap.put(asciiChar, charBrightness);
            } else {
                asciiMap.put(asciiChar, staticBrightnessMap.get(asciiChar));
            }
        }
    }


    /**
     * Calculates a brightness value to a given asciiChar
     * @param asciiChar - char
     * @return - the char brightness value
     */
    private double calculateBrightness(char asciiChar) {
        boolean[][] charAsArray = CharConverter.convertToBoolArray(asciiChar);
        double whitePixels = 0;
        for (int row = 0; row < DEFAULT_PIXEL_RESOLUTION; row++) {
            for (int col = 0; col < DEFAULT_PIXEL_RESOLUTION; col++) {
                if (charAsArray[row][col]) {
                    whitePixels += 1;
                }
            }
        }
        return whitePixels / (DEFAULT_PIXEL_RESOLUTION * DEFAULT_PIXEL_RESOLUTION);
    }

    /**
     * Goes over each char-brightness pair on the asciiMap and calculates a normalized brightness value
     */
    private void normalizeMap() {
        for (char asciiChar : charset) {
            double charInitialBrightness = staticBrightnessMap.get(asciiChar);
            double newBrightness = (charInitialBrightness - minBrightness) / (maxBrightness - minBrightness);
            asciiMap.replace(asciiChar, newBrightness);
        }
    }


    /**
     * Creates the brightnessValuesMap
     */
    private void createMapByBrightnessValues() {
        this.brightnessValuesMap = new HashMap<Double, Set<Character>>();
        for (char asciiChar : charset) {
            double brightness = asciiMap.get(asciiChar);
            Set<Character> charSet = brightnessValuesMap.get(brightness);
            if (charSet == null) {
                charSet = new HashSet<Character>();
                charSet.add(asciiChar);
                brightnessValuesMap.put(brightness, charSet);
            } else {
                charSet.add(asciiChar);
            }
        }
    }

    /**
     * finds the minimal ascii char in a set of chars
     * @param charByBrightnessSet - a set of ascii chars with the same brightness value
     * @return - the smallest ascii char
     */
    private char getMinChar(Set<Character> charByBrightnessSet) {
        Character minChar = null;
        for (char asciiChar : charByBrightnessSet) {
            if (minChar == null || asciiChar < minChar) {
                minChar = asciiChar;
            }
        }
        return minChar;
    }

    /**
     * Gets a char that has the given brightness value, or one with the closest value if a match does not
     * exist
     * @param brightness - the wanted brightness a char should have
     * @return - an ascii char with the closest value to brightness received
     */
    public char getCharByImageBrightness(double brightness) {
        Set<Character> charByBrightnessSet = brightnessValuesMap.get(brightness);
        if (charByBrightnessSet != null) {
            return getMinChar(charByBrightnessSet);
        }
        double closestKey = -1;
        double minDistance = 0;
        double minCharDistance = 0;
        char minChar = '\0';
        for (Double brightnessKey : brightnessValuesMap.keySet()) {
            double distance = Math.abs(brightnessKey - brightness);
            if (closestKey == -1 || distance <= minDistance) {
                closestKey = brightnessKey;
                minDistance = distance;
                boolean equalToPreviousMin = (minCharDistance == minDistance);
                if (minChar == '\0' || (equalToPreviousMin &&
                                getMinChar(brightnessValuesMap.get(closestKey)) < minChar) ||
                                            !equalToPreviousMin){
                    minChar = getMinChar(brightnessValuesMap.get(closestKey));
                    minCharDistance = distance;
                }
            }
        }
        return minChar;
    }

    /**
     * Goes over all the asciiMap brightness values and updates the maximum and minimum brightness values
     */
    private void updateMixMaxValues() {
        minBrightness = -1;
        maxBrightness = -1;
        for (char asciiChar : charset) {
            double brightness = staticBrightnessMap.get(asciiChar);
            if (minBrightness == -1 || minBrightness > brightness) {
                minBrightness = brightness;
            }
            if (maxBrightness == -1 || maxBrightness < brightness) {
                maxBrightness = brightness;
            }
        }
    }

    /**
     * Checks if an ascii char is already in the charSet
     * @param ascii char to be checked
     * @return true if char is in set, else - false
     */
    private boolean isCharInCharSet(char ascii) {
        for (char c : charset) {
            if (ascii == c) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a char to the charSet and updates the normalize brightness values accordingly
     * @param c char to be added
     */
    public void addChar(char c) {
        if (!isCharInCharSet(c)) {
            double charBrightness = calculateBrightness(c);
            char[] newCharset = new char[charset.length + 1];
            System.arraycopy(charset, 0, newCharset, 0, charset.length);
            newCharset[charset.length] = c;
            this.charset = newCharset;
            Arrays.sort(charset);
            staticBrightnessMap.put(c, charBrightness);
            asciiMap.put(c, charBrightness);
            updateMixMaxValues();
            normalizeMap();
            createMapByBrightnessValues();
        }
    }

    /**
     * Removes a char from the charSet and updates the normalize brightness values accordingly
     * @param c char to be removed
     */
    public void removeChar(char c) {
        if (isCharInCharSet(c)) {
            char[] newCharset = new char[charset.length - 1];
            int index1 = 0;
            for (char value : charset) {
                if (value != c) {
                    newCharset[index1] = value;
                    index1 += 1;
                }
            }
            this.charset = newCharset;
            Arrays.sort(charset);
            staticBrightnessMap.remove(c);
            asciiMap.remove(c);
            updateMixMaxValues();
            normalizeMap();
            createMapByBrightnessValues();
        }
    }

    /**
     * getter for the charset
     * @return the updated charset
     */
    public char[] getCharset(){
        return this.charset;
    }
}
