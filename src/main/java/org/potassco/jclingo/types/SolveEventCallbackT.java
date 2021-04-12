package org.potassco.jclingo.types;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public abstract class SolveEventCallbackT implements Callback {
    public abstract boolean call(int type, Pointer event, Pointer goon);

    public boolean callback(int type, Pointer event, Pointer data, Pointer goon) {
        return call(type, event, goon);
    }
}