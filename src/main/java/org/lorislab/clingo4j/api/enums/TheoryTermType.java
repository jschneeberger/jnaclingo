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
package org.lorislab.clingo4j.api.enums;

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_theory_term_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_theory_term_type.clingo_theory_term_type_function;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_theory_term_type.clingo_theory_term_type_list;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_theory_term_type.clingo_theory_term_type_number;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_theory_term_type.clingo_theory_term_type_set;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_theory_term_type.clingo_theory_term_type_symbol;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_theory_term_type.clingo_theory_term_type_tuple;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public enum TheoryTermType implements EnumValue<clingo_theory_term_type> {

    TUPLE(clingo_theory_term_type_tuple),
    LIST(clingo_theory_term_type_list),
    SET(clingo_theory_term_type_set),
    FUNCTION(clingo_theory_term_type_function),
    NUMBER(clingo_theory_term_type_number),
    SYMBOL(clingo_theory_term_type_symbol);

    private clingo_theory_term_type type;

    private TheoryTermType(clingo_theory_term_type type) {
        this.type = type;
    }

    @Override
    public clingo_theory_term_type getValue() {
        return type;
    }

}
