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
package org.lorislab.clingo4j.api.ast.enums;

import org.lorislab.clingo4j.util.EnumValue;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_unary_operator;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_unary_operator.clingo_ast_unary_operator_absolute;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_unary_operator.clingo_ast_unary_operator_minus;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_unary_operator.clingo_ast_unary_operator_negation;

/**
 *
 * @author Andrej Petras
 */
public enum UnaryOperator implements EnumValue<clingo_ast_unary_operator> {

    ABSOLUTE(clingo_ast_unary_operator_absolute,"|","|"),
    
    MINUS(clingo_ast_unary_operator_minus,"-",""),
    
    NEGATION(clingo_ast_unary_operator_negation,"~","");
            
    private final clingo_ast_unary_operator operator;

    private final String left;

    private final String right;
    
    private UnaryOperator(clingo_ast_unary_operator operator, String left, String right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public clingo_ast_unary_operator getValue() {
        return operator;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

}
