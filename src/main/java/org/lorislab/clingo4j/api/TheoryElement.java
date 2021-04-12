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

import org.lorislab.clingo4j.util.PointerList;
import java.util.List;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import static org.lorislab.clingo4j.api.Clingo.handleRuntimeError;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_theory_atoms;
import org.lorislab.clingo4j.util.PointerObject;

/**
 *
 * @author Andrej Petras
 */
public class TheoryElement extends PointerObject<clingo_theory_atoms> {

    private final int id;

    public TheoryElement(Pointer<clingo_theory_atoms> atoms, int id) {
        super(atoms);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<TheoryTerm> getTuple() throws ClingoException {
        Pointer<Pointer<Integer>> ret = Pointer.allocatePointer(Integer.class);
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleError(LIB.clingo_theory_atoms_element_tuple(pointer, id, ret, size), "Error reading theory element tuple!");
        return TheoryTerm.list(pointer, ret.get(), size.getInt());
    }

    public List<Integer> getCondition() throws ClingoException {
        Pointer<Pointer<Integer>> ret = Pointer.allocatePointer(Integer.class);
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleError(LIB.clingo_theory_atoms_element_condition(pointer, id, ret, size), "Error reading the theory elements condition!");
        return Pointer.allocateList(ret.get().getIO(), size.getInt());
    }

    public int getConditionId() throws ClingoException {
        Pointer<Integer> ret = Pointer.allocateInt();
        handleError(LIB.clingo_theory_atoms_element_condition_id(pointer, id, ret), "Error reading the theory element condition id!");
        return ret.get();
    }

    @Override
    public String toString() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleRuntimeError(LIB.clingo_theory_atoms_element_to_string_size(pointer, id, size), "Error reading to string size!");
        Pointer<Byte> string = Pointer.allocateByte();
        handleRuntimeError(LIB.clingo_theory_atoms_element_to_string(pointer, id, string, size.getLong()), "Error reading the theory element string");
        return string.getCString();
    }

    public static List<TheoryElement> list(final Pointer<clingo_theory_atoms> atoms, Pointer<Integer> pointer, long size) { 
        if (pointer == null) {
            return null;
        }
        return new PointerList<TheoryElement, Integer>(pointer, size) {
            @Override
            protected TheoryElement getItem(Integer p) {
                return new TheoryElement(atoms, p);
            }
        };
    }
   
}
