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
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_sign;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_sign.clingo_ast_sign_double_negation;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_sign.clingo_ast_sign_negation;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_sign.clingo_ast_sign_none;

/**
 *
 * @author Andrej Petras
 */
public enum Sign implements EnumValue<clingo_ast_sign> {
 
    NONE(clingo_ast_sign_none,""),
    
    NEGATION(clingo_ast_sign_negation,"not"),
    
    DOUBLE_NEGATION(clingo_ast_sign_double_negation,"not not");
    
    private final clingo_ast_sign sign;

    private final String string;
    
    private Sign(clingo_ast_sign sign, String string) {
        this.sign = sign;
        this.string = string;
    }

    @Override
    public clingo_ast_sign getValue() {
        return sign;
    }
    
    @Override
    public String toString() {
        return string;
    }
    
}
