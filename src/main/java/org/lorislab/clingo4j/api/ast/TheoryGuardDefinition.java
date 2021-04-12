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

import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_guard_definition;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.print;
import static org.lorislab.clingo4j.util.ASTObject.stringArray;
import static org.lorislab.clingo4j.util.ASTObject.listString;

/**
 *
 * @author Andrej Petras
 */
public class TheoryGuardDefinition implements ASTObject<clingo_ast_theory_guard_definition> {

    private final String term;
    private final List<String> operators;

    public TheoryGuardDefinition(clingo_ast_theory_guard_definition d) {
        this(d.term().getCString(), listString(d.operators(), d.size()));
    }
    
    public TheoryGuardDefinition(String term, List<String> operators) {
        this.term = term;
        this.operators = operators;
    }

    public List<String> getOperators() {
        return operators;
    }

    public String getTerm() {
        return term;
    }

    @Override
    public String toString() {
        return "{ " + print(operators, "", ", ", "", false) + " }, " + term;
    }

    @Override
    public clingo_ast_theory_guard_definition create() {
        clingo_ast_theory_guard_definition ret = new clingo_ast_theory_guard_definition();
        ret.term(Pointer.pointerToCString(term));
        ret.operators(stringArray(operators));
        ret.size(ASTObject.size(operators));
        return ret;
    }

}
