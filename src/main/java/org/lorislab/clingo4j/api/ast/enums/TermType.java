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
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_term_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_term_type.clingo_ast_term_type_binary_operation;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_term_type.clingo_ast_term_type_external_function;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_term_type.clingo_ast_term_type_function;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_term_type.clingo_ast_term_type_interval;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_term_type.clingo_ast_term_type_pool;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_term_type.clingo_ast_term_type_symbol;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_term_type.clingo_ast_term_type_unary_operation;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_term_type.clingo_ast_term_type_variable;

/**
 *
 * @author Andrej Petras
 */
public enum TermType implements EnumValue<clingo_ast_term_type> {

    SYMBOL(clingo_ast_term_type_symbol),
    VARIABLE(clingo_ast_term_type_variable),
    UNARY_OPERATION(clingo_ast_term_type_unary_operation),
    BINARY_OPERATION(clingo_ast_term_type_binary_operation),
    INTERVAL(clingo_ast_term_type_interval),
    FUNCTION(clingo_ast_term_type_function),
    EXTERNAL_FUNCTION(clingo_ast_term_type_external_function),
    POOL(clingo_ast_term_type_pool);

    private final clingo_ast_term_type type;

    private TermType(clingo_ast_term_type type) {
        this.type = type;
    }

    @Override
    public clingo_ast_term_type getValue() {
        return type;
    }

}
