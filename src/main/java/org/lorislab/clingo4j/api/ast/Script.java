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
package org.lorislab.clingo4j.api.ast;

import org.lorislab.clingo4j.api.ast.enums.ScriptType;
import org.lorislab.clingo4j.api.ast.enums.StatementType;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.ast.Statement.StatementData;
import org.lorislab.clingo4j.api.c.clingo_ast_script;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.util.ASTObject;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class Script implements ASTObject<clingo_ast_script>, StatementData {

    private final ScriptType type;
    private final String code;

    public Script(clingo_ast_script s) {
        this(EnumValue.valueOfInt(ScriptType.class, s.type()), s.code().getCString());
    }
    
    public Script(ScriptType type, String code) {
        this.type = type;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public ScriptType getType() {
        return type;
    }

    @Override
    public String toString() {
        String tmp = code;
        if (tmp != null && !tmp.endsWith(".")) {
            tmp = tmp + ".";
        }
        return tmp;
    }

    @Override
    public clingo_ast_script create() {
        clingo_ast_script ret = new clingo_ast_script();
        ret.type(type.getInt());
        ret.code(Pointer.pointerToCString(code));
        return ret;
    }

    @Override
    public void updateStatement(clingo_ast_statement ret) {
        ret.field1().script(createPointer());
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.SCRIPT;
    }
    
}
