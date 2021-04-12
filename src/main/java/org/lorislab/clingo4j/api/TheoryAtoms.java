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
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleRuntimeError;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_theory_atoms;
import org.lorislab.clingo4j.util.PointerObject;
import org.lorislab.clingo4j.util.UnmodifiableList;

/**
 *
 * @author Andrej Petras
 */
public class TheoryAtoms extends PointerObject<clingo_theory_atoms> implements UnmodifiableList<TheoryAtom> {

    public TheoryAtoms(Pointer<clingo_theory_atoms> pointer) {
        super(pointer);
    }

    @Override
    public Iterator<TheoryAtom> iterator() {
        return new Iterator<TheoryAtom>() {
            private int index = 0;
            private final int size = size();

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public TheoryAtom next() {
                TheoryAtom tmp = new TheoryAtom(pointer, index);
                index = index + 1;
                return tmp;
            }

        };
    }

    @Override
    public int size() {
        Pointer<SizeT> ret = Pointer.allocateSizeT();
        handleRuntimeError(LIB.clingo_theory_atoms_size(pointer, ret), "Error reading the theory atoms size!");
        return ret.getInt();
    }

    @Override
    public TheoryAtom get(int index) {
        if (0 <= index && index < size()) {
            return new TheoryAtom(pointer, index);
        }
        throw new IndexOutOfBoundsException();
    }
    
}
