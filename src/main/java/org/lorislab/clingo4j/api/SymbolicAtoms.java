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
import java.util.List;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import static org.lorislab.clingo4j.api.Clingo.handleRuntimeError;
import org.lorislab.clingo4j.api.ast.Signature;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_symbolic_atoms;
import org.lorislab.clingo4j.util.PointerObject;

/**
 *
 * @author Andrej Petras
 */
public class SymbolicAtoms extends PointerObject<clingo_symbolic_atoms> implements Iterable<SymbolicAtom> {

    public SymbolicAtoms(Pointer<clingo_symbolic_atoms> pointer) {
        super(pointer);
    }

    public int size() throws ClingoException {
        Pointer<SizeT> ret = Pointer.allocateSizeT();
        handleError(LIB.clingo_symbolic_atoms_size(pointer, ret), "Error reading the symbolic atoms size!");
        return ret.getInt();
    }

    public List<Signature> getSignatures() throws ClingoException {
        Pointer<SizeT> n = Pointer.allocateSizeT();
        handleError(LIB.clingo_symbolic_atoms_signatures_size(pointer, n), "Error reading the symbolic atoms signatures size!");
        Pointer<Long> signatures = Pointer.allocateLongs(n.getLong());
        handleError(LIB.clingo_symbolic_atoms_signatures(pointer, signatures, n.getLong()), "Error reading the symbolic atoms signatures!");
        return Signature.list(signatures, n.getLong());
    }

    public SymbolicAtom find(Symbol atom) throws ClingoException {
        Pointer<Long> iter = Pointer.allocateLong();
        handleError(LIB.clingo_symbolic_atoms_find(pointer, atom.getStructObject(), iter), "Error reading the symbolic atoms by symbol");
        SymbolicAtomIterator iterator = new SymbolicAtomIterator(pointer, iter.get(), 0);
        if (iterator.isValidIterator()) {
            return iterator.get();
        }
        return null;
    }

    public Iterator<SymbolicAtom> iterator(Symbol atom) throws ClingoException {
        Pointer<Long> iter = Pointer.allocateLong();
        handleError(LIB.clingo_symbolic_atoms_find(pointer, atom.getStructObject(), iter), "Error reading the symbolic atoms by symbol");
        Pointer<Long> end = Pointer.allocateLong();
        handleError(LIB.clingo_symbolic_atoms_end(pointer, end), "Error reading the symbolic atoms iterator end!");
        return new SymbolicAtomIterator(pointer, iter.get(), end.get());
    }

    public Iterator<SymbolicAtom> iterator(Signature signature) throws ClingoException {

        Pointer<Long> s = null;
        if (signature != null) {
            s = Pointer.pointerToLong(signature.getPointer());
        }
        Pointer<Long> iter = Pointer.allocateLong();
        handleError(LIB.clingo_symbolic_atoms_begin(pointer, s, iter), "Error reading the symbolic atoms iterator begin!");
        Pointer<Long> end = Pointer.allocateLong();
        handleError(LIB.clingo_symbolic_atoms_end(pointer, end), "Error reading the symbolic atoms iterator end!");
        return new SymbolicAtomIterator(pointer, iter.get(), end.get());
    }

    @Override
    public Iterator<SymbolicAtom> iterator() {
        try {
            return iterator((Signature) null);
        } catch (ClingoException ex) {
            handleRuntimeError(ex);
        }
        return null;
    }
}
