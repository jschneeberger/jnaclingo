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
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_statement_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_statement_type.clingo_ast_statement_type_const;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_statement_type.clingo_ast_statement_type_edge;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_statement_type.clingo_ast_statement_type_external;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_statement_type.clingo_ast_statement_type_heuristic;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_statement_type.clingo_ast_statement_type_minimize;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_statement_type.clingo_ast_statement_type_program;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_statement_type.clingo_ast_statement_type_project_atom;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_statement_type.clingo_ast_statement_type_project_atom_signature;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_statement_type.clingo_ast_statement_type_rule;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_statement_type.clingo_ast_statement_type_script;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_statement_type.clingo_ast_statement_type_show_signature;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_statement_type.clingo_ast_statement_type_show_term;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_statement_type.clingo_ast_statement_type_theory_definition;

/**
 *
 * @author Andrej Petras
 */
public enum StatementType implements EnumValue<clingo_ast_statement_type> {
    
    RULE(clingo_ast_statement_type_rule),
    CONST(clingo_ast_statement_type_const),
    SHOW_SIGNATURE(clingo_ast_statement_type_show_signature),
    SHOW_TERM(clingo_ast_statement_type_show_term),
    MINIMIZE(clingo_ast_statement_type_minimize),
    SCRIPT(clingo_ast_statement_type_script),
    PROGRAM(clingo_ast_statement_type_program),
    EXTERNAL(clingo_ast_statement_type_external),
    EDGE(clingo_ast_statement_type_edge),
    HEURISTIC(clingo_ast_statement_type_heuristic),
    PROJECT_ATOM(clingo_ast_statement_type_project_atom),
    PROJECT_ATOM_SIGNATURE(clingo_ast_statement_type_project_atom_signature),
    THEORY_DEFINITION(clingo_ast_statement_type_theory_definition),
    ;
    
    private clingo_ast_statement_type type;

    private StatementType(clingo_ast_statement_type type) {
        this.type = type;
    }

    @Override
    public clingo_ast_statement_type getValue() {
        return type;
    }
   
}
