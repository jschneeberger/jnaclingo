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

import org.lorislab.clingo4j.api.ast.enums.ComparisonOperator;
import org.lorislab.clingo4j.api.c.clingo_ast_aggregate_guard;
import org.lorislab.clingo4j.util.ASTObject;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class AggregateGuard implements ASTObject<clingo_ast_aggregate_guard> {

    private final ComparisonOperator operator;

    private final Term term;

    public AggregateGuard(clingo_ast_aggregate_guard g) {
        this(EnumValue.valueOfInt(ComparisonOperator.class, g.comparison()), new Term(g.term()));
    }

    public AggregateGuard(ComparisonOperator comparison, Term term) {
        this.operator = comparison;
        this.term = term;
    }

    public ComparisonOperator getOperator() {
        return operator;
    }

    public Term getTerm() {
        return term;
    }

    @Override
    public clingo_ast_aggregate_guard create() {
        clingo_ast_aggregate_guard g = new clingo_ast_aggregate_guard();
        g.comparison(operator.getInt());
        g.term(term.create());
        return g;
    }

}
