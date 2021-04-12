/*
 * Copyright 2018 Andrej Petras.
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

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_solve_event_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_solve_event_type.clingo_solve_event_type_finish;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_solve_event_type.clingo_solve_event_type_model;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public enum SolveEventType implements EnumValue<clingo_solve_event_type> {

    MODEL(clingo_solve_event_type_model),
    
    FINISH(clingo_solve_event_type_finish);

        private final clingo_solve_event_type type;

    private SolveEventType(clingo_solve_event_type type) {
        this.type = type;
    }

    @Override
    public clingo_solve_event_type getValue() {
        return type;
    }

}
