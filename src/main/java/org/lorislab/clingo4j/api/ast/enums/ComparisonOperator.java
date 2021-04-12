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
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_comparison_operator;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_comparison_operator.clingo_ast_comparison_operator_equal;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_comparison_operator.clingo_ast_comparison_operator_greater_equal;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_comparison_operator.clingo_ast_comparison_operator_greater_than;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_comparison_operator.clingo_ast_comparison_operator_less_equal;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_comparison_operator.clingo_ast_comparison_operator_less_than;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_comparison_operator.clingo_ast_comparison_operator_not_equal;

/**
 *
 * @author Andrej Petras
 */
public enum ComparisonOperator implements EnumValue<clingo_ast_comparison_operator> {
 
    GREATER_THAN(clingo_ast_comparison_operator_greater_than,">"),
    
    LESS_THAN(clingo_ast_comparison_operator_less_than,"<"),
    
    LESS_EQUAL(clingo_ast_comparison_operator_less_equal,"<="),
    
    GREATER_EQUAL(clingo_ast_comparison_operator_greater_equal,">="),
    
    NOT_EQUAL(clingo_ast_comparison_operator_not_equal, "!="),

    EQUAL(clingo_ast_comparison_operator_equal, "=");
    
    private final clingo_ast_comparison_operator operator;

    private final String string;
    
    private ComparisonOperator(clingo_ast_comparison_operator operator, String string) {
        this.operator = operator;
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public clingo_ast_comparison_operator getValue() {
        return operator;
    }
    
}
