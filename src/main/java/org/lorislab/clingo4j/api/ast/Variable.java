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

import org.lorislab.clingo4j.api.ast.enums.TermType;
import org.lorislab.clingo4j.api.ast.enums.TheoryTermType;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.ast.Term.TermData;
import org.lorislab.clingo4j.api.ast.TheoryTerm.TheoryTermData;
import org.lorislab.clingo4j.api.c.clingo_ast_term;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_term;

/**
 *
 * @author Andrej Petras
 */
public class Variable implements TermData, TheoryTermData {
    
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void updateTerm(clingo_ast_term ret) {
        ret.field1().variable(Pointer.pointerToCString(name));
    }

    @Override
    public TermType getTermType() {
        return TermType.VARIABLE;
    }

    @Override
    public void updateTheoryTerm(clingo_ast_theory_term ret) {
        ret.field1().variable(Pointer.pointerToCString(name));
    }

    @Override
    public TheoryTermType getTheoryTermType() {
        return TheoryTermType.VARIABLE;
    }
            
    
}
