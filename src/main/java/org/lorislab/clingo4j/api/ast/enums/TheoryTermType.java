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
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_theory_term_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_theory_term_type.clingo_ast_theory_term_type_function;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_theory_term_type.clingo_ast_theory_term_type_list;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_theory_term_type.clingo_ast_theory_term_type_set;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_theory_term_type.clingo_ast_theory_term_type_symbol;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_theory_term_type.clingo_ast_theory_term_type_tuple;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_theory_term_type.clingo_ast_theory_term_type_unparsed_term;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_theory_term_type.clingo_ast_theory_term_type_variable;

/**
 *
 * @author Andrej Petras
 */
public enum TheoryTermType implements EnumValue<clingo_ast_theory_term_type> {

    SYMBOL(clingo_ast_theory_term_type_symbol),
    VARIABLE(clingo_ast_theory_term_type_variable),
    TUPLE(clingo_ast_theory_term_type_tuple),
    LIST(clingo_ast_theory_term_type_list),
    SET(clingo_ast_theory_term_type_set),
    FUNCTIONS(clingo_ast_theory_term_type_function),
    UNPARSED_TERM(clingo_ast_theory_term_type_unparsed_term);

    private final clingo_ast_theory_term_type type;

    private TheoryTermType(clingo_ast_theory_term_type type) {
        this.type = type;
    }

    @Override
    public clingo_ast_theory_term_type getValue() {
        return type;
    }
    
}
