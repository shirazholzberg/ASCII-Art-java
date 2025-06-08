package ascii_art.Exceptions;

/**
 * class that creates an invalid command exception
 */
public class InvalidCommandExceptions extends Exception {

    /**
     * default constructor
     */
    public InvalidCommandExceptions() {
        super("Invalid Command");
    }
}
