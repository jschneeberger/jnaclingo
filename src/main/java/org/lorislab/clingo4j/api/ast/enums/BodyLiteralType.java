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
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_body_literal_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_body_literal_type.clingo_ast_body_literal_type_aggregate;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_body_literal_type.clingo_ast_body_literal_type_body_aggregate;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_body_literal_type.clingo_ast_body_literal_type_conditional;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_body_literal_type.clingo_ast_body_literal_type_disjoint;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_body_literal_type.clingo_ast_body_literal_type_literal;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_body_literal_type.clingo_ast_body_literal_type_theory_atom;

/**
 *
 * @author Andrej Petras
 */
public enum BodyLiteralType implements EnumValue<clingo_ast_body_literal_type> {

    LITERAL(clingo_ast_body_literal_type_literal),
    CONDITIONAL(clingo_ast_body_literal_type_conditional),
    AGGREGATE(clingo_ast_body_literal_type_aggregate),
    BODY_AGGREGATE(clingo_ast_body_literal_type_body_aggregate),
    THEORY_ATOM(clingo_ast_body_literal_type_theory_atom),
    DISJOINT(clingo_ast_body_literal_type_disjoint);

    private final clingo_ast_body_literal_type type;

    private BodyLiteralType(clingo_ast_body_literal_type type) {
        this.type = type;
    }

    @Override
    public clingo_ast_body_literal_type getValue() {
        return type;
    }

}
