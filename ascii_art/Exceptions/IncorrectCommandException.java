package ascii_art.Exceptions;

/**
 * A class for an incorrect command - when the command start is legal but the request is not legal
 */
public class IncorrectCommandException extends Exception{
    /**
     * the type of command that raised this exception
     */
    private String type;

    /**
     * error for incorrect res command
     */
    private String res = "Did not change resolution due to incorrect format.";

    /**
     * error for incorrect add command
     */
    private String add = "Did not add due to incorrect format.";
    /**
     * error for incorrect remove command
     */
    private String remove = "Did not remove due to incorrect format.";
    /**
     * error for incorrect output command
     */
    private String output = "Did not change output method due to incorrect format.";

    /**
     * constructor
     * @param type - the type of command that raised the error
     */
    public IncorrectCommandException(String type){
        super("Incorrect Command.");
        this.type = type;
    }

    /**
     * returns the string of the error according to the type of command that raised the error
     * @return String
     */
    @Override
    public String toString(){
        if (this.type.equals("res")){
            return this.res;
        }
        else if ( this.type.equals("add")){
            return this.add;
        }
        else if (this.type.equals("remove")){
            return this.remove;
        }
        return this.output;
    }
}
