package Dao;

import java.io.Serializable;

/**
 * The interface of identifiable objects.
 */
public interface Identified<PK extends Serializable> {

    /** Returns the identifier of the object */
    public PK getId();
}
