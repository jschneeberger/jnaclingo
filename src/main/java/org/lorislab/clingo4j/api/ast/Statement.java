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
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.util.ASTObject;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class Statement implements ASTObject<clingo_ast_statement> {

    private final Location location;

    //Rule, Definition, ShowSignature, ShowTerm, Minimize, Script, Program, External, Edge, Heuristic, ProjectAtom, ProjectSignature, TheoryDefinition
    private final StatementData data;

    public Statement(clingo_ast_statement stm) {
        StatementType type = EnumValue.valueOfInt(StatementType.class, stm.type());
        if (type != null) {
            location = new Location(stm.location());
            switch (type) {
                case RULE:
                    data = new Rule(stm.field1().rule().get());
                    break;
                case CONST:
                    data = new Definition(stm.field1().definition().get());
                    break;
                case SHOW_SIGNATURE:
                    data = new ShowSignature(stm.field1().show_signature().get());
                    break;
                case SHOW_TERM:
                    data = new ShowTerm(stm.field1().show_term().get());
                    break;
                case MINIMIZE:
                    data = new Minimize(stm.field1().minimize().get());
                    break;
                case SCRIPT:
                    data = new Script(stm.field1().script().get());
                    break;
                case PROGRAM:
                    data = new Program(stm.field1().program().get());
                    break;
                case EXTERNAL:
                    data = new External(stm.field1().external().get());
                    break;
                case EDGE:
                    data = new Edge(stm.field1().edge().get());
                    break;
                case HEURISTIC:
                    data = new Heuristic(stm.field1().heuristic().get());
                    break;
                case PROJECT_ATOM:
                    data = new ProjectAtom(stm.field1().project_atom().get());
                    break;
                case PROJECT_ATOM_SIGNATURE:
                    data = new ProjectSignature(stm.field1().project_signature());
                    break;
                case THEORY_DEFINITION:
                    data = new TheoryDefinition(stm.field1().theory_definition().get());
                    break;
                default:
                    data = null;
            }
        } else {
            throw new RuntimeException("cannot happen");
        }
    }

    public Statement(Location location, StatementData data) {
        this.location = location;
        this.data = data;
    }

    public StatementData getData() {
        return data;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public clingo_ast_statement create() {
        clingo_ast_statement ret = new clingo_ast_statement();
        ret.type(data.getStatementType().getInt());
        ret.location(location.getStructObject());
        data.updateStatement(ret);
        return ret;
    }

    public interface StatementData {

        public void updateStatement(clingo_ast_statement statment);

        public StatementType getStatementType();

    }

    @Override
    public String toString() {
        return "" + data;
    }

}
