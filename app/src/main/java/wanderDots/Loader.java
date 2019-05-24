package wanderDots;

import java.util.ArrayList;

public interface Loader <T> {

    ArrayList<T> getData() ;
    boolean hasError() ;
    String getError() ;
    void reload() ;
}
