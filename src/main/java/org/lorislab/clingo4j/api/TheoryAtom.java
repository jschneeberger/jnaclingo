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
public class TheoryAtom extends PointerObject<clingo_theory_atoms> {

    private final int id;

    public TheoryAtom(Pointer<clingo_theory_atoms> atoms, int id) {
        super(atoms);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<TheoryElement> getElements() throws ClingoException {
        Pointer<Pointer<Integer>> ret = Pointer.allocatePointer(Integer.class);
        Pointer<SizeT> n = Pointer.allocateSizeT();
        handleError(LIB.clingo_theory_atoms_atom_elements(pointer, id, ret, n), "Error reading the theory atom elements!");
        return TheoryElement.list(pointer, ret.get(), n.getInt());
    }

    public TheoryTerm getTerm() throws ClingoException {
        Pointer<Integer> ret = Pointer.allocateInt();
        handleError(LIB.clingo_theory_atoms_atom_term(pointer, id, ret), "Error reading the theory atom term!");
        return new TheoryTerm(pointer, ret.getInt());
    }

    public boolean hasGuard() throws ClingoException {
        Pointer<Boolean> ret = Pointer.allocateBoolean();
        handleError(LIB.clingo_theory_atoms_atom_has_guard(pointer, id, ret), "Error reading the theory atom has guard!");
        return ret.get();
    }

    public int getLiteral() throws ClingoException {
        Pointer<Integer> ret = Pointer.allocateInt();
        handleError(LIB.clingo_theory_atoms_atom_literal(pointer, id, ret), "Error reading the theory atom literal!");
        return ret.getInt();
    }

    public TheoryAtomGuard getGuard() throws ClingoException {
        Pointer<Pointer<Byte>> name = Pointer.allocatePointer(Byte.class);
        Pointer<Integer> term = Pointer.allocateInt();
        handleError(LIB.clingo_theory_atoms_atom_guard(pointer, id, name, term), "Error reading the theory atom guard!");
        return new TheoryAtomGuard(name.get().getWideCString(), new TheoryTerm(pointer, term.get()));
    }

    public static class TheoryAtomGuard {

        private final String name;

        private final TheoryTerm term;

        public TheoryAtomGuard(String name, TheoryTerm term) {
            this.name = name;
            this.term = term;
        }

        public TheoryTerm getTerm() {
            return term;
        }

        public String getName() {
            return name;
        }

    }

    @Override
    public String toString() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleRuntimeError(LIB.clingo_theory_atoms_atom_to_string_size(pointer, id, size), "Error reading to string size!");
        Pointer<Byte> string = Pointer.allocateByte();
        handleRuntimeError(LIB.clingo_theory_atoms_atom_to_string(pointer, id, string, size.getLong()), "Error reading theory atom string");
        return string.getCString();
    }
}
