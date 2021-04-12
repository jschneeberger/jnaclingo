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

import org.lorislab.clingo4j.api.enums.PropagatorCheckMode;
import org.bridj.Pointer;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_propagate_init;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_symbolic_atoms;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_theory_atoms;
import org.lorislab.clingo4j.util.PointerObject;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class PropagateInit extends PointerObject<clingo_propagate_init> {

    public PropagateInit(Pointer<clingo_propagate_init> pointer) {
        super(pointer);
    }

    public int initSolverLiteral(int lit) throws ClingoException {
        Pointer<Integer> ret = Pointer.allocateInt();
        handleError(LIB.clingo_propagate_init_solver_literal(pointer, lit, ret), "Error reading the progagete init solver literal!");
        return ret.getInt();
    }

    public void addWatch(int lit) throws ClingoException {
        handleError(LIB.clingo_propagate_init_add_watch(pointer, lit), "Error add the progagete init watch!");
    }

    public int getNumberOfThreads() {
        return LIB.clingo_propagate_init_number_of_threads(pointer);
    }

    public SymbolicAtoms getSymbolicAtoms() throws ClingoException {
        Pointer<Pointer<clingo_symbolic_atoms>> ret = Pointer.allocatePointer(clingo_symbolic_atoms.class);
        handleError(LIB.clingo_propagate_init_symbolic_atoms(pointer, ret), "Error reading the progagete init symbolic atoms!");
        return new SymbolicAtoms(ret.get());
    }

    public TheoryAtoms getTheoryAtoms() throws ClingoException {
        Pointer<Pointer<clingo_theory_atoms>> ret = Pointer.allocatePointer(clingo_theory_atoms.class);
        handleError(LIB.clingo_propagate_init_theory_atoms(pointer, ret), "Error reading the progagete init theory atoms!");
        return new TheoryAtoms(ret.get());
    }

    public PropagatorCheckMode getCheckMode() {
        return EnumValue.valueOfInt(PropagatorCheckMode.class, LIB.clingo_propagate_init_get_check_mode(pointer));
    }

    public void setCheckMode(PropagatorCheckMode mode) {
        LIB.clingo_propagate_init_set_check_mode(pointer, mode.getInt());
    }

}
