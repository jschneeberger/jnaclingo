/*
 * Copyright 2017 Andrej Petras.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"){}
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
package org.lorislab.clingo4j.api.callback;

import org.lorislab.clingo4j.api.enums.ExternalType;
import org.lorislab.clingo4j.api.enums.HeuristicType;
import java.util.List;
import org.lorislab.clingo4j.api.Symbol;
import org.lorislab.clingo4j.api.WeightedLiteral;

/**
 *
 * @author Andrej Petras
 */
public interface GroundProgramObserver {

    default void initProgram(boolean incremental) {
    }

    default void beginStep() {
    }

    default void endStep() {
    }

    default void rule(boolean choice, List<Integer> head, List<Integer> body) {
    }

    default void weightRule(boolean choice, List<Integer> head, int lower_bound, List<WeightedLiteral> body) {
    }

    default void minimize(int priority, List<WeightedLiteral> literals) {
    }

    default void project(List<Integer> atoms) {
    }

    default void outputAtom(Symbol symbol, int atom) {
    }

    default void outputTerm(Symbol symbol, List<Integer> condition) {
    }

    default void outputCsp(Symbol symbol, int value, List<Integer> condition) {
    }

    default void external(int atom, ExternalType type) {
    }

    default void assume(List<Integer> literals) {
    }

    default void heuristic(int atom, HeuristicType type, int bias, int priority, List<Integer> condition) {
    }

    default void acycEdge(int nodeU, int nodeV, List<Integer> condition) {
    }

    default void theoryTermNumber(int termId, int number) {
    }

    default void theoryTermString(int termId, String name) {
    }

    default void theoryTermCompound(int termId, int nameIdOrType, List<Integer> arguments) {
    }

    default void theoryElement(int elementId, List<Integer> terms, List<Integer> condition) {
    }

    default void theoryAtom(int atomIdOrZero, int termId, List<Integer> elements) {
    }

    default void theoryAtomWithGuard(int atomIdOrZero, int termId, List<Integer> elements, int operatorId, int rightHandSideId) {
    }
}
