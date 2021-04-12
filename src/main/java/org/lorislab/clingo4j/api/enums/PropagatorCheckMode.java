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

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_propagator_check_mode;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_propagator_check_mode.clingo_propagator_check_mode_fixpoint;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_propagator_check_mode.clingo_propagator_check_mode_none;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_propagator_check_mode.clingo_propagator_check_mode_total;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public enum PropagatorCheckMode implements EnumValue<clingo_propagator_check_mode> {

    NONE(clingo_propagator_check_mode_none),
    TOTAL(clingo_propagator_check_mode_total),
    PARTIAL(clingo_propagator_check_mode_fixpoint);

    private clingo_propagator_check_mode mode;

    private PropagatorCheckMode(clingo_propagator_check_mode mode) {
        this.mode = mode;
    }

    @Override
    public clingo_propagator_check_mode getValue() {
        return mode;
    }

}
