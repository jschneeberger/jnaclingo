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

import org.lorislab.clingo4j.api.ast.enums.BodyLiteralType;
import org.lorislab.clingo4j.api.ast.enums.HeadLiteralType;
import java.util.List;
import java.util.Optional;
import org.lorislab.clingo4j.api.ast.BodyLiteral.BodyLiteralData;
import org.lorislab.clingo4j.api.ast.HeadLiteral.HeadLiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_body_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_head_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_atom;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class TheoryAtom implements ASTObject<clingo_ast_theory_atom>, HeadLiteralData, BodyLiteralData {

    private final Term term;
    private final List<TheoryAtomElement> elements;
    private final Optional<TheoryGuard> guard;

    public TheoryAtom(clingo_ast_theory_atom a) {
        this(new Term(a.term()), TheoryAtomElement.list(a.elements(), a.size()), ASTObject.optional(TheoryGuard::new, a.guard()));
    }

    public TheoryAtom(Term term, List<TheoryAtomElement> elements, Optional<TheoryGuard> guard) {
        this.term = term;
        this.elements = elements;
        this.guard = guard;
    }

    public List<TheoryAtomElement> getElements() {
        return elements;
    }

    public Optional<TheoryGuard> getGuard() {
        return guard;
    }

    public Term getTerm() {
        return term;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('&').append(term).append(" { ").append(print(elements, "", "; ", "", false)).append(" }");
        if (guard.isPresent()) {
            sb.append(" ").append(guard.get());
        }
        return sb.toString();
    }

    @Override
    public void updateHeadLiteral(clingo_ast_head_literal ret) {
        ret.field1().theory_atom(createPointer());
    }

    @Override
    public HeadLiteralType getHeadLiteralType() {
        return HeadLiteralType.THEORY_ATOM;
    }

    @Override
    public clingo_ast_theory_atom create() {
        clingo_ast_theory_atom ret = new clingo_ast_theory_atom();
        ret.term(term.create());
        ret.guard(ASTObject.optionalPointer(guard));
        ret.elements(ASTObject.array(elements));
        ret.size(ASTObject.size(elements));
        return ret;
    }

    @Override
    public void updateBodyLiteral(clingo_ast_body_literal ret) {
        ret.field1().theory_atom(createPointer());
    }

    @Override
    public BodyLiteralType getBodyLiteralType() {
        return BodyLiteralType.THEORY_ATOM;
    }

}
