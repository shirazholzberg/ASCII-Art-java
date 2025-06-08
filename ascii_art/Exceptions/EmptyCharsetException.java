package ascii_art.Exceptions;

/**
 * A class for an empty charset error
 */
public class EmptyCharsetException extends Exception {
    /**
     * constructor
     */
    public EmptyCharsetException(){
        super("Empty Char Set.");
    }
}
