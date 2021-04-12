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
import org.lorislab.clingo4j.api.ast.Term.TermData;
import org.lorislab.clingo4j.api.c.clingo_ast_interval;
import org.lorislab.clingo4j.api.c.clingo_ast_term;
import org.lorislab.clingo4j.util.ASTObject;

/**
 *
 * @author Andrej Petras
 */
public class Interval implements ASTObject<clingo_ast_interval>, TermData {

    private final Term left;
    
    private final Term right;

    public Interval(clingo_ast_interval t) {
       this(new Term(t.left()), new Term(t.right()));
    }
    
    public Interval(Term left, Term right) {
        this.left = left;
        this.right = right;
    }

    public Term getLeft() {
        return left;
    }

    public Term getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "(" + left + ".." + right + ")";
    }

    @Override
    public void updateTerm(clingo_ast_term ret) {
        ret.field1().interval(createPointer());
    }

    @Override
    public TermType getTermType() {
        return TermType.INTERVAL;
    }

    @Override
    public clingo_ast_interval create() {
        clingo_ast_interval i = new clingo_ast_interval();
        i.left(left.create());
        i.right(right.create());
        return i;
    }
    
    
    
}
