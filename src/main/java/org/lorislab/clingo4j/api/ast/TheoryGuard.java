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
package org.lorislab.clingo4j.api.ast;

import org.bridj.Pointer;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_guard;
import org.lorislab.clingo4j.util.ASTObject;

/**
 *
 * @author Andrej Petras
 */
public class TheoryGuard implements ASTObject<clingo_ast_theory_guard> {

    private final String operatorName;

    private final TheoryTerm term;

    public TheoryGuard(clingo_ast_theory_guard g) {
        this(g.operator_name().getCString(), new TheoryTerm(g.term()));
    }

    public TheoryGuard(String operatorName, TheoryTerm term) {
        this.operatorName = operatorName;
        this.term = term;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public TheoryTerm getTerm() {
        return term;
    }

    @Override
    public String toString() {
        return operatorName + " " + term;
    }

    @Override
    public clingo_ast_theory_guard create() {
        clingo_ast_theory_guard g = new clingo_ast_theory_guard();
        g.operator_name(Pointer.pointerToCString(operatorName));
        g.term(term.create());
        return g;
    }
}
