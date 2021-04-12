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

import org.lorislab.clingo4j.api.ast.enums.BinaryOperator;
import org.lorislab.clingo4j.api.ast.enums.TermType;
import org.lorislab.clingo4j.api.ast.Term.TermData;
import org.lorislab.clingo4j.api.c.clingo_ast_binary_operation;
import org.lorislab.clingo4j.api.c.clingo_ast_term;
import org.lorislab.clingo4j.util.ASTObject;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class BinaryOperation implements ASTObject<clingo_ast_binary_operation>, TermData {
    
    private final BinaryOperator operator;
    
    private final Term left;
    
    private final Term right;

    public BinaryOperation(clingo_ast_binary_operation t) {
        this(EnumValue.valueOfInt(BinaryOperator.class, t.binary_operator()), new Term(t.left()), new Term(t.right()));
    }
    
    public BinaryOperation(BinaryOperator operator, Term left, Term right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public BinaryOperator getOperator() {
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
        return "("  + left + operator + right + ")";
    }

    @Override
    public clingo_ast_binary_operation create() {
        clingo_ast_binary_operation bo = new clingo_ast_binary_operation();
        bo.binary_operator(operator.getInt());
        bo.left(left.create());
        bo.right(right.create());
        return bo;
    }

    @Override
    public void updateTerm(clingo_ast_term ret) {
        ret.field1().binary_operation(createPointer());
    }

    @Override
    public TermType getTermType() {
        return TermType.BINARY_OPERATION;
    }
 
    
}
