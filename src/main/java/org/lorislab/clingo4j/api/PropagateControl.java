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

import org.lorislab.clingo4j.api.enums.ClauseType;
import java.util.List;
import org.bridj.NativeList;
import org.bridj.Pointer;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_propagate_control;
import org.lorislab.clingo4j.util.PointerObject;

/**
 *
 * @author Andrej Petras
 */
public class PropagateControl extends PointerObject<clingo_propagate_control> {

    public PropagateControl(Pointer<clingo_propagate_control> pointer) {
        super(pointer);
    }

    public int getThreadId() {
        return LIB.clingo_propagate_control_thread_id(pointer);
    }

    public Assignment getAssignment() {
        return new Assignment(LIB.clingo_propagate_control_assignment(pointer));
    }

    public int addLiteral() throws ClingoException {
        Pointer<Integer> ret = Pointer.allocateInt();
        handleError(LIB.clingo_propagate_control_add_literal(pointer, ret), "Error add the literal to the propagate control!");
        return ret.getInt();
    }

    public void addWatch(int literal) throws ClingoException {
        handleError(LIB.clingo_propagate_control_add_watch(pointer, literal), "Error add the watch to the propagete control!");
    }

    public boolean hasWatch(int literal) {
        return LIB.clingo_propagate_control_has_watch(pointer, literal);
    }

    public void removeWatch(int literal) {
        LIB.clingo_propagate_control_remove_watch(pointer, literal);
    }

    public boolean addClause(List<Integer> clause, ClauseType type) throws ClingoException {
        NativeList list = PointerObject.toNativeList(clause);
        Pointer<Boolean> ret = Pointer.allocateBoolean();
        handleError(LIB.clingo_propagate_control_add_clause(pointer, list.getPointer(), list.size(), type.getInt(), ret), "Error add the clause to the propagete control!");
        return ret.get();
    }

    public boolean getPropagate() throws ClingoException {
        Pointer<Boolean> ret = Pointer.allocateBoolean();
        handleError(LIB.clingo_propagate_control_propagate(pointer, ret), "Error propagete the propagete control!");
        return ret.get();
    }

}
