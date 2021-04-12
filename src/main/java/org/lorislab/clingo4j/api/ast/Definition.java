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

import org.lorislab.clingo4j.api.ast.enums.StatementType;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.ast.Statement.StatementData;
import org.lorislab.clingo4j.api.c.clingo_ast_definition;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.util.ASTObject;

/**
 *
 * @author Andrej Petras
 */
public class Definition implements ASTObject<clingo_ast_definition>, StatementData {

    private final String name;
    private final Term value;
    private final boolean isDefault;

    public Definition(clingo_ast_definition d) {
        this(d.name().getCString(), new Term(d.value()), d.is_default());
    }

    public Definition(String name, Term value, boolean isDefault) {
        this.name = name;
        this.value = value;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public Term getValue() {
        return value;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#const ").append(name).append(" = ").append(value).append(".");
        if (isDefault) {
            sb.append(" [default]");
        }
        return sb.toString();
    }

    @Override
    public clingo_ast_definition create() {
        clingo_ast_definition ret = new clingo_ast_definition();
        ret.is_default(isDefault);
        ret.name(Pointer.pointerToCString(name));
        ret.value(value.create());
        return ret;
    }

    @Override
    public void updateStatement(clingo_ast_statement ret) {
        ret.field1().definition(createPointer());
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.CONST;
    }

}
