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
import org.lorislab.clingo4j.api.c.clingo_ast_theory_unparsed_term_element;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;
import static org.lorislab.clingo4j.util.ASTObject.print;
import static org.lorislab.clingo4j.util.ASTObject.stringArray;

/**
 *
 * @author Andrej Petras
 */
public class TheoryUnparsedTermElement implements ASTObject<clingo_ast_theory_unparsed_term_element> {

    private final List<String> operators;

    private final TheoryTerm term;

    public TheoryUnparsedTermElement(clingo_ast_theory_unparsed_term_element e) {
        this(ASTObject.listString(e.operators(), e.size()), new TheoryTerm(e.term()));
    }

    public TheoryUnparsedTermElement(List<String> operators, TheoryTerm term) {
        this.operators = operators;
        this.term = term;
    }

    public List<String> getOperators() {
        return operators;
    }

    public TheoryTerm getTerm() {
        return term;
    }

    @Override
    public String toString() {
        return print(operators, " ", " ", " ", false) + term;
    }

    @Override
    public clingo_ast_theory_unparsed_term_element create() {
        clingo_ast_theory_unparsed_term_element ret = new clingo_ast_theory_unparsed_term_element();
        ret.term(term.create());
        ret.size(ASTObject.size(operators));
        ret.operators(stringArray(operators));
        return ret;
    }

    public static List<TheoryUnparsedTermElement> list(Pointer<clingo_ast_theory_unparsed_term_element> pointer, long size) {
        return pointerList(TheoryUnparsedTermElement::new, pointer, size);
    }

}
