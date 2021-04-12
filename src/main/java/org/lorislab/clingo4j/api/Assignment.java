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

import org.lorislab.clingo4j.api.enums.TruthValue;
import org.bridj.Pointer;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_assignment;
import org.lorislab.clingo4j.util.PointerObject;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class Assignment extends PointerObject<clingo_assignment> {

    public Assignment(Pointer<clingo_assignment> pointer) {
        super(pointer);
    }

    public boolean hasConflict() {
        return LIB.clingo_assignment_has_conflict(pointer);
    }

    public int getDecisionLevel() {
        return LIB.clingo_assignment_decision_level(pointer);
    }

    public boolean hasLiteral(int literal) {
        return LIB.clingo_assignment_has_literal(pointer, literal);
    }

    public TruthValue getTruthValue(int literal) throws ClingoException {
        Pointer<Integer> ret = Pointer.allocateInt();
        handleError(LIB.clingo_assignment_truth_value(pointer, literal, ret), "Error reading the assignment truth value!");
        return EnumValue.valueOfInt(TruthValue.class, ret.getInt());
    }

    public int getLevel(int lit) throws ClingoException {
        Pointer<Integer> ret = Pointer.allocateInt();
        handleError(LIB.clingo_assignment_level(pointer, lit, ret), "Error reading the assignment level!");
        return ret.getInt();
    }

    public int getDecision(int level) throws ClingoException {
        Pointer<Integer> ret = Pointer.allocateInt();
        handleError(LIB.clingo_assignment_decision(pointer, level, ret), "Error reading the assignment decision!");
        return ret.getInt();
    }

    public boolean isFixed(int lit) throws ClingoException {
        Pointer<Boolean> ret = Pointer.allocateBoolean();
        handleError(LIB.clingo_assignment_is_fixed(pointer, lit, ret), "Error reading the assignment is fixed!");
        return ret.get();
    }

    public boolean isTrue(int lit) throws ClingoException {
        Pointer<Boolean> ret = Pointer.allocateBoolean();
        handleError(LIB.clingo_assignment_is_true(pointer, lit, ret), "Error reading the assignment is true!");
        return ret.get();
    }

    public boolean isFalse(int lit) throws ClingoException {
        Pointer<Boolean> ret = Pointer.allocateBoolean();
        handleError(LIB.clingo_assignment_is_false(pointer, lit, ret), "Error reading the assignment is false!");
        return ret.get();
    }

    public long getSize() {
        return LIB.clingo_assignment_size(pointer);
    }

    public long getMaxSize() {
        return LIB.clingo_assignment_max_size(pointer);
    }

    public boolean isTotal() {
        return LIB.clingo_assignment_is_total(pointer);
    }

}
