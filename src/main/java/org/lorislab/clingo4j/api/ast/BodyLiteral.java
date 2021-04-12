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

import org.lorislab.clingo4j.api.ast.enums.BodyLiteralType;
import org.lorislab.clingo4j.api.ast.enums.Sign;
import java.util.List;
import org.lorislab.clingo4j.util.EnumValue;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.c.clingo_ast_body_literal;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;

/**
 *
 * @author Andrej Petras
 */
public class BodyLiteral implements ASTObject<clingo_ast_body_literal> {

    private final Location location;
    private final Sign sign;
    //Literal, ConditionalLiteral, Aggregate, BodyAggregate, TheoryAtom, Disjoint    
    private final BodyLiteralData data;

    public BodyLiteral(clingo_ast_body_literal lit) {
        BodyLiteralType type = EnumValue.valueOfInt(BodyLiteralType.class, lit.type());
        if (type != null) {
            switch (type) {
                case LITERAL:
                    data = new Literal(lit.field1().literal().get());
                    break;
                case CONDITIONAL:
                    data = new ConditionalLiteral(lit.field1().conditional().get());
                    break;
                case AGGREGATE:
                    data = new Aggregate(lit.field1().aggregate().get());
                    break;
                case BODY_AGGREGATE:
                    data = new BodyAggregate(lit.field1().body_aggregate().get());
                    break;
                case THEORY_ATOM:
                    data = new TheoryAtom(lit.field1().theory_atom().get());
                    break;
                case DISJOINT:
                    data = new Disjoint(lit.field1().disjoint().get());
                    break;
                default:
                    data = null;
            }
            location = new Location(lit.location());
            sign = EnumValue.valueOfInt(Sign.class, lit.sign());
        } else {
            throw new RuntimeException("cannot happen");
        }
    }

    public BodyLiteral(Location location, Sign sign, BodyLiteralData data) {
        this.location = location;
        this.sign = sign;
        this.data = data;
    }

    public Location getLocation() {
        return location;
    }

    public BodyLiteralData getData() {
        return data;
    }

    public Sign getSign() {
        return sign;
    }

    @Override
    public clingo_ast_body_literal create() {
        clingo_ast_body_literal ret = new clingo_ast_body_literal();
        ret.type(data.getBodyLiteralType().getInt());
        ret.location(location.getStructObject());
        ret.sign(sign.getInt());
        data.updateBodyLiteral(ret);
        return ret;
    }
    
    public interface BodyLiteralData {

        public void updateBodyLiteral(clingo_ast_body_literal ret);

        public BodyLiteralType getBodyLiteralType();
    }

    @Override
    public String toString() {
        return "" + sign + data;
    }

    public static List<BodyLiteral> list(Pointer<clingo_ast_body_literal> pointer, long size) {
        return pointerList(BodyLiteral::new, pointer, size);
    }

}
