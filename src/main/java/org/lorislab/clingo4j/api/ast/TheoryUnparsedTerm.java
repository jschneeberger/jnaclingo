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
import java.util.List;
import org.lorislab.clingo4j.api.ast.TheoryTerm.TheoryTermData;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_term;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_unparsed_term;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class TheoryUnparsedTerm implements ASTObject<clingo_ast_theory_unparsed_term>, TheoryTermData {

    private final List<TheoryUnparsedTermElement> elements;

    public TheoryUnparsedTerm(clingo_ast_theory_unparsed_term t) {
        this(TheoryUnparsedTermElement.list(t.elements(), t.size()));
    }
    
    public TheoryUnparsedTerm(List<TheoryUnparsedTermElement> elements) {
        this.elements = elements;
    }

    public List<TheoryUnparsedTermElement> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (elements != null && elements.size() > 1) {
            sb.append("(");
        }
        sb.append(print(elements, "", "", "", false));
        if (elements != null && elements.size() > 1) {
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public clingo_ast_theory_unparsed_term create() {
        clingo_ast_theory_unparsed_term ret = new clingo_ast_theory_unparsed_term();
        ret.elements(ASTObject.array(elements));
        ret.size(ASTObject.size(elements));
        return ret;
    }
    
    @Override
    public void updateTheoryTerm(clingo_ast_theory_term ret) {
        ret.field1().unparsed_term(createPointer());
    }

    @Override
    public TheoryTermType getTheoryTermType() {
        return TheoryTermType.UNPARSED_TERM;
    }
    
}
