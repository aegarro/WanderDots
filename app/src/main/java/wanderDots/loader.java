package wanderDots;

import java.util.ArrayList;

public interface loader<T> {

    ArrayList<T> getData() ;
    boolean hasError() ;
    String getError() ;
    void reload() ;
}
