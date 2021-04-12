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
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_script_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_script_type.clingo_ast_script_type_lua;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_script_type.clingo_ast_script_type_python;

/**
 *
 * @author Andrej Petras
 */
public enum ScriptType implements EnumValue<clingo_ast_script_type> {

    LUA(clingo_ast_script_type_lua, "lua"),
    PYTHON(clingo_ast_script_type_python, "python");

    private clingo_ast_script_type type;

    private String string;

    private ScriptType(clingo_ast_script_type type, String string) {
        this.type = type;
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public clingo_ast_script_type getValue() {
        return type;
    }

}
