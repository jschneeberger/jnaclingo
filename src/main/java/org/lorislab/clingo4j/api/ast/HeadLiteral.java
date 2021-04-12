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

import org.lorislab.clingo4j.api.ast.enums.HeadLiteralType;
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.c.clingo_ast_head_literal;
import org.lorislab.clingo4j.util.ASTObject;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class HeadLiteral implements ASTObject<clingo_ast_head_literal> {

    private final Location location;

    //Literal, Disjunction, Aggregate, HeadAggregate, TheoryAtom   
    private final HeadLiteralData data;

    public HeadLiteral(final clingo_ast_head_literal head) {
        HeadLiteralType type = EnumValue.valueOfInt(HeadLiteralType.class, head.type());
        if (type != null) {
            location = new Location(head.location());
            switch (type) {
                case LITERAL:
                    data = new Literal(head.field1().literal().get());
                    break;
                case DISJUNCTION:
                    data = new Disjunction(head.field1().disjunction().get());
                    break;
                case AGGREGATE:
                    data = new Aggregate(head.field1().aggregate().get());
                    break;
                case HEAD_AGGREGATE:
                    data = new HeadAggregate(head.field1().head_aggregate().get());
                    break;
                case THEORY_ATOM:
                    data = new TheoryAtom(head.field1().theory_atom().get());
                    break;
                default:
                    data = null;
            }
        } else {
            throw new RuntimeException("cannot happen!");
        }
    }

    public HeadLiteral(Location location, HeadLiteralData data) {
        this.location = location;
        this.data = data;
    }

    public Location getLocation() {
        return location;
    }

    public HeadLiteralData getData() {
        return data;
    }

    @Override
    public clingo_ast_head_literal create() {
        clingo_ast_head_literal ret = new clingo_ast_head_literal();
        ret.type(data.getHeadLiteralType().getInt());
        ret.location(location.getStructObject());
        data.updateHeadLiteral(ret);
        return ret;
    }

    public interface HeadLiteralData {

        public void updateHeadLiteral(clingo_ast_head_literal ret);

        public HeadLiteralType getHeadLiteralType();

    }

    @Override
    public String toString() {
        return "" + data;
    }

}
