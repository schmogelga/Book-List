package misc.util;

public class Handler {

    public static void handle( Exception e ) {

        //TODO: here you can add some kind of error display or create diferent treatments based on the given error
        e.printStackTrace();
        System.exit( 1 );
    }
}
