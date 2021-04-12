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

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_error;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_error.clingo_error_bad_alloc;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_error.clingo_error_logic;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_error.clingo_error_runtime;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_error.clingo_error_unknown;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public enum ErrorCode implements EnumValue<clingo_error> {

    RUNTIME(clingo_error_runtime),
    LOGIC(clingo_error_logic),
    BAD_ALLOC(clingo_error_bad_alloc),
    UNKNOWN(clingo_error_unknown);
    
    private final clingo_error code;

    private ErrorCode(clingo_error code) {
        this.code = code;
    }

    @Override
    public clingo_error getValue() {
        return code;
    }

    @Override
    public String toString() {
        return this.name() + "[" + getValue() + "]";
    }
    
}
