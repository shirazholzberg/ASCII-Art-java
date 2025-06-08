package ascii_art;


import ascii_art.Exceptions.EmptyCharsetException;
import ascii_art.Exceptions.ExceedingBoundariesException;
import ascii_art.Exceptions.IncorrectCommandException;
import ascii_art.Exceptions.InvalidCommandExceptions;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.*;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * a constructor to the user interface to the ascii art algorithm
 */
public class Shell {
    private SubImgCharMatcher asciiConvertor;
    private AsciiArtAlgorithm asciiArtAlgorithm;
    private KeyboardInput keyboardInput = KeyboardInput.getObject();
    private String input;
    private Image image;
    private int resolution;
    private PaddingImage paddingImage;
    private AsciiOutput output;
    private char[][] result;

    /**
     * Constructor - creates a PaddingImage, SubImgCharMatcher and AsciiArtAlgorithm according to the
     * default values
     * @throws IOException if there is an issue with the image object
     */
    public Shell() throws IOException {
        paddingImage = new PaddingImage (new Image("cat.jpeg"));
        image = paddingImage.padImage();
        char[] charSet = {'0','1','2','3','4','5','6','7','8','9'};
        resolution = 128;
        this.asciiConvertor = new SubImgCharMatcher(charSet);
        this.asciiArtAlgorithm = new AsciiArtAlgorithm(image, asciiConvertor, resolution);
        output = new ConsoleAsciiOutput();
        result = asciiArtAlgorithm.run();
    }

    /**
     * Changes the resolution according to the given command
     * @throws IncorrectCommandException if there is an issue with the command is not legal
     * @throws ExceedingBoundariesException if the resolution exceeds boundaries
     */
    private void setResolution() throws IncorrectCommandException, ExceedingBoundariesException {
        int minCharsInRow = Math.max(1, image.getWidth()/image.getHeight());
        int newResolution;
        if(input.equals("res up")){
            newResolution= resolution*2;
        }
        else if (input.equals("res down")){
            newResolution = resolution/2;
        }
        else{
            throw new IncorrectCommandException("res");
        }
        if((newResolution < minCharsInRow)|| (newResolution > image.getWidth())) {
            throw new ExceedingBoundariesException();
        }
        else{
            resolution = newResolution;
            asciiArtAlgorithm = new AsciiArtAlgorithm(image, asciiConvertor, resolution);
            System.out.println(String.format("Resolution set to %s", resolution));
        }
    }

    /**
     * Changes the image to an image the user selects
     * @throws IOException if there is an issue with the image file
     * @throws InvalidCommandExceptions if there is an issue with the command is not legal
     */
    private void setImage() throws IOException, InvalidCommandExceptions {
        String[] inputParts = input.split(" ");
        if (inputParts.length != 2){
            throw new InvalidCommandExceptions();
        }
        try {
            paddingImage = new PaddingImage(new Image(inputParts[1]));
            image = paddingImage.padImage();
            asciiArtAlgorithm = new AsciiArtAlgorithm(image, asciiConvertor, resolution);
        }
        catch(IOException e){
            System.out.println("Did not execute due to problem with image file.");
        }
    }

    /**
     * Changes the way the output will be delivered to the user when selecting the asciiArt command
     * @throws IncorrectCommandException if there is an issue with the command is not legal
     */
    private void setOutput() throws IncorrectCommandException{
        if(input.equals("output html")){
            output = new HtmlAsciiOutput("out.html","New Courier");
        }
        else if (input.equals("output console")){
            output = new ConsoleAsciiOutput();
        }
        else {
            throw new IncorrectCommandException("output");
        }
    }

    /**
     * Prints the ascii char set the algorithm is currently using
     */
    private void chars(){
        char[] charSet = asciiConvertor.getCharset();
        for (char asciiChar : charSet){
            System.out.print(asciiChar + " ");
        }
        System.out.println();
    }

    /**
     * Compare between two ascii chars
     * @param firstLetter asciiChar1
     * @param secondLetter asciiChar2
     * @return an array with the smallest char in index 0, and the
     * largest char in index 1
     */
    private char[] findFirstChar(int firstLetter, int secondLetter){
        char firstChar;
        char lastChar;
        if (input.charAt(firstLetter) < input.charAt(secondLetter)){
            firstChar = input.charAt(firstLetter);
            lastChar = input.charAt(secondLetter);
        }
        else {
            firstChar = input.charAt(secondLetter);
            lastChar = input.charAt(firstLetter);
        }
        return new char[]{firstChar, lastChar};
    }

    /**
     * add an ascii char to the charset used in SubImgCharMatcher
     * @throws IncorrectCommandException if there is an issue with the command is not legal
     */
    private void add() throws IncorrectCommandException{

            if (input.length() == 5) {
                char newChar = input.charAt(4);
                asciiConvertor.addChar(newChar);
            } else if (input.equals("add all")) {
                for (char ascii = 32; ascii <= 126; ascii++) {
                    asciiConvertor.addChar(ascii);
                }
            } else if (input.equals("add space")) {
                asciiConvertor.addChar(' ');
            } else if (input.length() == 7 && input.indexOf("-") == 5) {
                char[] firstLastArray = findFirstChar(4, 6);
                char firstChar = firstLastArray[0];
                char lastChar = firstLastArray[1];
                for (char asciiChar = firstChar; asciiChar <= lastChar; asciiChar++) {
                    asciiConvertor.addChar(asciiChar);
                }
            } else {
                throw new IncorrectCommandException("add");
            }
    }

    /**
     * removes an ascii char to the charset used in SubImgCharMatcher
     * @throws IncorrectCommandException if there is an issue with the command is not legal
     */
    private void remove() throws IncorrectCommandException{
        if (input.length() == 8){
            char charToRemove = input.charAt(7);
            asciiConvertor.removeChar(charToRemove);
        }
        else if (input.equals("remove all")){
            for (char asciiChar : asciiConvertor.getCharset()){
                asciiConvertor.removeChar(asciiChar);
            }
        }
        else if (input.equals("remove space")){
            asciiConvertor.removeChar(' ');
        }
        else if (input.length() == 10 && input.indexOf("-") == 8){
            char[] firstLastArray = findFirstChar(7, 9);
            char firstChar = firstLastArray[0];
            char lastChar = firstLastArray[1];
            for (char asciiChar = firstChar; asciiChar <= lastChar ; asciiChar++){
                asciiConvertor.removeChar(asciiChar);
            }
        }
        else {
            throw new IncorrectCommandException("remove");
        }
    }

    /**
     * runs the user interface - reads input and make actions accordingly
     * @throws IOException if there is an issue with the image file
     */
    public void run() throws IOException {
        System.out.print(">>> ");
        input = keyboardInput.readLine();
        while(!input.equals("exit")){
            try{
            if(input.contains("res")){
                setResolution();
            }
            else if(input.startsWith("image")){
                setImage();
            }
            else if (input.equals("chars")) {
                chars();
            }
            else if (input.startsWith("add ")){
                add();
            }
            else if (input.startsWith("remove ")){
                remove();
            }
            else if(input.startsWith("output ")){
                setOutput();
            }
            else if (input.equals("asciiArt")){
                if(asciiConvertor.getCharset().length == 0){
                    throw new EmptyCharsetException();
                }
                else{
                    char[][] finalPic = asciiArtAlgorithm.run();
                    output.out(finalPic);
                }
            }
            else{
                throw new InvalidCommandExceptions();
                }
            }
            catch (ExceedingBoundariesException e){
                System.out.println("Did not change resolution due to exceeding boundaries.");
            }
            catch (IncorrectCommandException e){
                System.out.println(e.toString());
            }
            catch (InvalidCommandExceptions e){
                System.out.println("Did not execute due to incorrect command.");
            }
            catch (EmptyCharsetException e){
                System.out.println("Did not execute. Charset is empty.");
            }
            System.out.print(">>> ");
            input = keyboardInput.readLine();
        }
        System.exit(0);;
    }

    /**
     * the main function - creates a Shell objects and runs the algorithm
     * @param args does not receive arguments
     * @throws IOException is there is an issue with reading the image inside the shell
     */
    public static void main(String[] args) throws IOException {
        Shell runShell = new Shell();
        runShell.run();
    }
}