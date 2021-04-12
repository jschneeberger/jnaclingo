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

import org.lorislab.clingo4j.api.ast.enums.TheoryTermType;
import org.lorislab.clingo4j.api.ast.enums.TheoryTermSequenceType;
import java.util.List;
import org.lorislab.clingo4j.api.ast.TheoryTerm.TheoryTermData;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_term;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_term_array;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class TheoryTermSequence implements ASTObject<clingo_ast_theory_term_array>, TheoryTermData {

    private final TheoryTermSequenceType type;

    private final List<TheoryTerm> terms;

    public TheoryTermSequence(TheoryTermSequenceType type, clingo_ast_theory_term_array t) {
        this(type, TheoryTerm.list(t.terms(), t.size()));
    }
    
    public TheoryTermSequence(TheoryTermSequenceType type, List<TheoryTerm> terms) {
        this.type = type;
        this.terms = terms;
    }

    public List<TheoryTerm> getTerms() {
        return terms;
    }

    public TheoryTermSequenceType getType() {
        return type;
    }

    @Override
    public String toString() {
        boolean tc = terms != null && terms.size() == 1 && type == TheoryTermSequenceType.TUPLE;
        StringBuilder sb = new StringBuilder();
        sb.append(print(terms, type.getLeft(), ",", "", true));
        if (tc) {
            sb.append(",)");
        } else {
            sb.append(type.getRight());
        }
        return sb.toString();
    }

    @Override
    public clingo_ast_theory_term_array create() {
        clingo_ast_theory_term_array a = new clingo_ast_theory_term_array();
        a.terms(ASTObject.array(terms));
        a.size(ASTObject.size(terms));
        return a;
    }

    @Override
    public void updateTheoryTerm(clingo_ast_theory_term ret) {
        switch (type) {
            case LIST:
                ret.field1().list(createPointer());
            case SET:
                ret.field1().set(createPointer());
            case TUPLE:
                ret.field1().tuple(createPointer());
        }
    }

    @Override
    public TheoryTermType getTheoryTermType() {
        switch (type) {
            case LIST:
                return TheoryTermType.LIST;
            case SET:
                return TheoryTermType.SET;
            case TUPLE:
                return TheoryTermType.TUPLE;
        }
        return null;
    }

}
