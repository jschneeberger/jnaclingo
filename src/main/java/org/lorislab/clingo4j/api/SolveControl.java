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

import java.util.ArrayList;
import java.util.List;
import org.bridj.NativeList;
import org.bridj.Pointer;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_solve_control;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_symbolic_atoms;
import org.lorislab.clingo4j.util.PointerObject;

/**
 *
 * @author Andrej Petras
 */
public class SolveControl extends PointerObject<clingo_solve_control> {

    public SolveControl(Pointer<clingo_solve_control> pointer) {
        super(pointer);
    }

    public SymbolicAtoms getSymbolicAtoms() throws ClingoException {
        Pointer<Pointer<clingo_symbolic_atoms>> ret = Pointer.allocatePointer(clingo_symbolic_atoms.class);
        handleError(LIB.clingo_solve_control_symbolic_atoms(pointer, ret), "Error readint the solve control symbolic atoms!");
        return new SymbolicAtoms(ret.get());
    }

    public void addSymbolicLiterallause(List<SymbolicLiteral> clause) throws ClingoException {
        if (clause == null || clause.isEmpty()) {
            return;
        }
        List<Integer> list = new ArrayList<>(clause.size());
        SymbolicAtoms atoms = getSymbolicAtoms();
        for (SymbolicLiteral x : clause) {
            SymbolicAtom aa = atoms.find(x.getSymbol());
            if (aa != null) {
                int lit = aa.getLiteral();
                list.add(x.isPositive() ? lit : -lit);
            } else if (x.isNegative()) {
                return;
            }
        }
        addClause(list);
    }

    public void addClause(List<Integer> clause) throws ClingoException {
        if (clause != null && !clause.isEmpty()) {
            NativeList tmp = PointerObject.toNativeList(clause);
            handleError(LIB.clingo_solve_control_add_clause(pointer, tmp.getPointer(), tmp.size()), "Error solve control add clause");
        }
    }

}
