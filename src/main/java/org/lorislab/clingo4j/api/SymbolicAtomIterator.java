/*
 * Copyright 2017 Andrej Petras.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lorislab.clingo4j.api;

import java.util.Iterator;
import org.bridj.Pointer;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import static org.lorislab.clingo4j.api.Clingo.handleRuntimeError;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_symbolic_atoms;

/**
 *
 * @author Andrej Petras
 */
public class SymbolicAtomIterator implements Iterator<SymbolicAtom> {

    private final Pointer<clingo_symbolic_atoms> atoms;

    private long iter;

    private final long end;

    public SymbolicAtomIterator(Pointer<clingo_symbolic_atoms> atoms, long begin, long end) {
        this.atoms = atoms;
        this.iter = begin;
        this.end = end;
    }

    @Override
    public boolean hasNext() {
        try {
            return !isEqualIterator(iter, end);
        } catch (ClingoException ex) {
            handleRuntimeError(ex);
        }
        return false;
    }

    @Override
    public SymbolicAtom next() {
        try {
            if (isValidIterator()) {
                SymbolicAtom tmp = get();
                nextIterator();
                return tmp;
            }
        } catch (ClingoException ex) {
            handleRuntimeError(ex);
        }
        return null;
    }

    public SymbolicAtom get() {
        return new SymbolicAtom(atoms, iter);
    }
    
    public SymbolicAtomIterator nextIterator() throws ClingoException {
        Pointer<Long> next = Pointer.allocateLong();
        handleError(LIB.clingo_symbolic_atoms_next(atoms, iter, next), "Error reading the next atoms!");
        iter = next.get();
        return this;
    }

    public boolean isValidIterator() throws ClingoException {
        Pointer<Boolean> ret = Pointer.allocateBoolean();
        handleError(LIB.clingo_symbolic_atoms_is_valid(atoms, iter, ret), "Error check the atoms is valid function!");
        return ret.getBoolean();
    }

    public boolean isNotEqual(SymbolicAtomIterator it) throws ClingoException {
        return !isEqual(it);
    }

    public boolean isEqual(SymbolicAtomIterator it) throws ClingoException {
        boolean ret = atoms.equals(it.atoms);
        if (ret) {
            return isEqualIterator(iter, it.iter);
        }
        return ret;
    }

    private boolean isEqualIterator(long iterator, long end) throws ClingoException {
        Pointer<Boolean> value = Pointer.allocateBoolean();
        handleError(LIB.clingo_symbolic_atoms_iterator_is_equal_to(atoms, iterator, end, value), "Error check atoms iterator is equal!");
        return value.get();
    }

}
