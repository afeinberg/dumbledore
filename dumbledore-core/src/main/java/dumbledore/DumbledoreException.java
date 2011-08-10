package dumbledore;

/**
 *  Base exception for all other Dumbledore exceptions.
 *
 */
public class DumbledoreException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DumbledoreException() {
        super();
    }

    public DumbledoreException(String s, Throwable t) {
        super(s, t);
    }

    public DumbledoreException(String s) {
        super(s);
    }

    public DumbledoreException(Throwable t) {
        super(t);
    }

}
