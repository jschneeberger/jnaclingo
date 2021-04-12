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
import java.util.List;
import org.lorislab.clingo4j.api.ast.Statement.StatementData;
import org.lorislab.clingo4j.api.c.clingo_ast_edge;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.printBody;

/**
 *
 * @author Andrej Petras
 */
public class Edge implements ASTObject<clingo_ast_edge>, StatementData {

    private final Term u;
    private final Term v;
    private final List<BodyLiteral> body;

    public Edge(clingo_ast_edge e) {
        this(new Term(e.u()), new Term(e.v()), BodyLiteral.list(e.body(), e.size()));
    }
        
    public Edge(Term u, Term v, List<BodyLiteral> body) {
        this.u = u;
        this.v = v;
        this.body = body;
    }

    public List<BodyLiteral> getBody() {
        return body;
    }

    public Term getU() {
        return u;
    }

    public Term getV() {
        return v;
    }

    @Override
    public String toString() {
        return "#edge (" + u + "," + v + ")" + printBody(body);
    }

    @Override
    public clingo_ast_edge create() {
        clingo_ast_edge ret = new clingo_ast_edge();
        ret.u(u.create());
        ret.v(v.create());
        ret.body(ASTObject.array(body));
        ret.size(ASTObject.size(body));
        return ret;
    }

    @Override
    public void updateStatement(clingo_ast_statement ret) {
        ret.field1().edge(createPointer());
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.EDGE;
    }

}
