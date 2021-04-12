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
import org.lorislab.clingo4j.api.ast.enums.LiteralType;
import org.lorislab.clingo4j.api.ast.Literal.LiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_comparison;
import org.lorislab.clingo4j.api.c.clingo_ast_literal;
import org.lorislab.clingo4j.util.ASTObject;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class Comparison implements ASTObject<clingo_ast_comparison>, LiteralData {

    private final ComparisonOperator operator;
    private final Term left;
    private final Term right;

    public Comparison(clingo_ast_comparison com) {
        this(EnumValue.valueOfInt(ComparisonOperator.class, com.comparison()), new Term(com.left()), new Term(com.right()));
    }
    
    public Comparison(ComparisonOperator comparison, Term left, Term right) {
        this.operator = comparison;
        this.left = left;
        this.right = right;
    }
    
    public ComparisonOperator getOperator() {
        return operator;
    }

    public Term getLeft() {
        return left;
    }

    public Term getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "" + left + operator  + right;
    }

    @Override
    public clingo_ast_comparison create() {
        clingo_ast_comparison com = new clingo_ast_comparison();
        com.comparison(operator.getInt());
        com.left(left.create());
        com.right(right.create());
        return com;
    }

    @Override
    public void updateLiteral(clingo_ast_literal ret) {
        ret.field1().comparison(createPointer());
    }

    @Override
    public LiteralType getLiteralType() {
        return LiteralType.COMPARISON;
    }
    
    
}
