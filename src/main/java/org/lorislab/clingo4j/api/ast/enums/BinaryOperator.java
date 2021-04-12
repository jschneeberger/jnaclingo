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
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_binary_operator;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_binary_operator.clingo_ast_binary_operator_and;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_binary_operator.clingo_ast_binary_operator_division;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_binary_operator.clingo_ast_binary_operator_minus;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_binary_operator.clingo_ast_binary_operator_modulo;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_binary_operator.clingo_ast_binary_operator_multiplication;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_binary_operator.clingo_ast_binary_operator_or;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_binary_operator.clingo_ast_binary_operator_plus;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_binary_operator.clingo_ast_binary_operator_xor;

/**
 *
 * @author Andrej Petras
 */
public enum BinaryOperator implements EnumValue<clingo_ast_binary_operator> {
    
    XOR(clingo_ast_binary_operator_xor,"^"),
    
    OR(clingo_ast_binary_operator_or,"?"),
    
    AND(clingo_ast_binary_operator_and,"&"),
    
    PLUS(clingo_ast_binary_operator_plus,"+"),
    
    MINUS(clingo_ast_binary_operator_minus,"-"),
    
    MULTIPLICATION(clingo_ast_binary_operator_multiplication,"*"),
    
    DIVISION(clingo_ast_binary_operator_division,"/"),
    
    MODULO(clingo_ast_binary_operator_modulo,"\\");
            
    private clingo_ast_binary_operator operator;
    
    private String string;

    private BinaryOperator(clingo_ast_binary_operator operator, String string) {
        this.operator = operator;
        this.string = string;
    }


    @Override
    public String toString() {
        return string;
    }

    @Override
    public clingo_ast_binary_operator getValue() {
        return operator;
    }

}
