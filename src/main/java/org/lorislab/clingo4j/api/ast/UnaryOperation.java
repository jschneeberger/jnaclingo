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
import org.lorislab.clingo4j.api.ast.enums.UnaryOperator;
import org.lorislab.clingo4j.api.ast.Term.TermData;
import org.lorislab.clingo4j.api.c.clingo_ast_term;
import org.lorislab.clingo4j.api.c.clingo_ast_unary_operation;
import org.lorislab.clingo4j.util.ASTObject;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class UnaryOperation implements ASTObject<clingo_ast_unary_operation>, TermData {
    
    private final UnaryOperator unaryOperator;
    
    private final Term argument;

    public UnaryOperation(clingo_ast_unary_operation op) {
        this(EnumValue.valueOfInt(UnaryOperator.class, op.unary_operator()), new Term(op.argument()));
    }
    public UnaryOperation(UnaryOperator unaryOperator, Term argument) {
        this.unaryOperator = unaryOperator;
        this.argument = argument;
    }

    public UnaryOperator getUnaryOperator() {
        return unaryOperator;
    }

    public Term getArgument() {
        return argument;
    }

    @Override
    public String toString() {
        return unaryOperator.getLeft() + argument + unaryOperator.getRight();
    }

    @Override
    public void updateTerm(clingo_ast_term ret) {
        ret.field1().unary_operation(createPointer());
    }

    @Override
    public TermType getTermType() {
        return TermType.UNARY_OPERATION;
    }

    @Override
    public clingo_ast_unary_operation create() {
        clingo_ast_unary_operation uo = new clingo_ast_unary_operation();
        uo.unary_operator(unaryOperator.getInt());
        uo.argument(argument.create());
        return uo;
    }
    
}
