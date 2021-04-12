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
import org.lorislab.clingo4j.api.ast.enums.HeadLiteralType;
import org.lorislab.clingo4j.api.ast.enums.LiteralType;
import org.lorislab.clingo4j.api.ast.enums.Sign;
import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.ast.BodyLiteral.BodyLiteralData;
import org.lorislab.clingo4j.api.ast.HeadLiteral.HeadLiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_body_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_head_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_literal;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class Literal implements ASTObject<clingo_ast_literal>, BodyLiteralData, HeadLiteralData {

    private final Location location;
    private final Sign sign;

    //Boolean, Term, Comparison, CSPLiteral
    private final LiteralData data;

    public Literal(clingo_ast_literal lit) {
        LiteralType type = EnumValue.valueOfInt(LiteralType.class, lit.type());
        if (type != null) {
            switch (type) {
                case BOOLEAN:
                    data = new BooleanLiteral(lit.field1().boolean$());
                    break;
                case SYMBOLIC:
                    data = new Term(lit.field1().symbol().get());
                    break;
                case COMPARISON:
                    data = new Comparison(lit.field1().comparison().get());
                    break;
                case CSP:
                    data = new CSPLiteral(lit.field1().csp_literal().get());
                    break;
                default:
                    data = null;
            }
            location = new Location(lit.location());
            sign = EnumValue.valueOfInt(Sign.class, lit.sign());
        } else {
            throw new  RuntimeException("cannot happen");
        }
    }

    public Literal(Location location, Sign sign, LiteralData data) {
        this.location = location;
        this.sign = sign;
        this.data = data;
    }

    public Location getLocation() {
        return location;
    }

    public Sign getSign() {
        return sign;
    }

    public LiteralData getData() {
        return data;
    }

    @Override
    public clingo_ast_literal create() {
        clingo_ast_literal r = new clingo_ast_literal();
        r.type(data.getLiteralType().getInt());
        r.location(location.getStructObject());
        data.updateLiteral(r);
        return r;
    }

    @Override
    public void updateHeadLiteral(clingo_ast_head_literal ret) {
        ret.field1().literal(Pointer.getPointer(create()));
    }

    @Override
    public HeadLiteralType getHeadLiteralType() {
        return HeadLiteralType.LITERAL;
    }

    @Override
    public void updateBodyLiteral(clingo_ast_body_literal ret) {
        ret.field1().literal(createPointer());
    }

    @Override
    public BodyLiteralType getBodyLiteralType() {
        return BodyLiteralType.LITERAL;
    }

    public interface LiteralData {

        public void updateLiteral(clingo_ast_literal lit);

        public LiteralType getLiteralType();
    }

    @Override
    public String toString() {
        return "" + sign + data;
    }

    public static List<Literal> list(Pointer<clingo_ast_literal> pointer, long size) {
        return pointerList(Literal::new, pointer, size);
    }
 
}
