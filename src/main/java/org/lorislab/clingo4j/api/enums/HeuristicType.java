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

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_heuristic_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_heuristic_type.clingo_heuristic_type_factor;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_heuristic_type.clingo_heuristic_type_false;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_heuristic_type.clingo_heuristic_type_init;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_heuristic_type.clingo_heuristic_type_level;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_heuristic_type.clingo_heuristic_type_sign;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_heuristic_type.clingo_heuristic_type_true;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public enum HeuristicType implements EnumValue<clingo_heuristic_type> {

    LEVEL(clingo_heuristic_type_level, "Level"),
    SIGN(clingo_heuristic_type_sign, "Sign"),
    FACTOR(clingo_heuristic_type_factor, "Factor"),
    INIT(clingo_heuristic_type_init, "Init"),
    TRUE(clingo_heuristic_type_true, "True"),
    FALSE(clingo_heuristic_type_false, "False");

    private final clingo_heuristic_type type;

    private final String string;

    private HeuristicType(clingo_heuristic_type type, String string) {
        this.type = type;
        this.string = string;
    }

    @Override
    public clingo_heuristic_type getValue() {
        return type;
    }
    
    @Override
    public String toString() {
        return string;
    }

}
