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

import org.lorislab.clingo4j.api.ast.enums.LiteralType;
import org.lorislab.clingo4j.api.ast.enums.TermType;
import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.Symbol;
import org.lorislab.clingo4j.api.ast.Literal.LiteralData;
import org.lorislab.clingo4j.api.ast.Term.TermData;
import org.lorislab.clingo4j.api.c.clingo_ast_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_term;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class Term implements ASTObject<clingo_ast_term>, LiteralData {

    private final Location location;

    //Symbol, Variable, UnaryOperation, BinaryOperation, Interval, Function, Pool
    private final TermData data;

    public Term(final clingo_ast_term term) {
        TermType type = EnumValue.valueOfInt(TermType.class, term.type());
        if (type != null) {
            location = new Location(term.location());
            switch (type) {
                case SYMBOL:
                    data = new Symbol(term.field1().symbol());
                    break;
                case VARIABLE:
                    data = new Variable(term.field1().variable().getCString());
                    break;
                case UNARY_OPERATION:
                    data = new UnaryOperation(term.field1().unary_operation().get());
                    break;
                case BINARY_OPERATION:
                    data = new BinaryOperation(term.field1().binary_operation().get());
                    break;
                case INTERVAL:
                    data = new Interval(term.field1().interval().get());
                    break;
                case FUNCTION:
                    data = new Function(term.field1().function().get(), false);
                    break;
                case EXTERNAL_FUNCTION:
                    data = new Function(term.field1().external_function().get(), true);
                    break;
                case POOL:
                    data = new Pool(term.field1().pool().get());
                    break;
                default:
                    data = null;
            }
        } else {
            throw new RuntimeException("cannot happen");
        }
    }

    public Term(Location location, TermData data) {
        this.location = location;
        this.data = data;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public clingo_ast_term create() {
        clingo_ast_term ret = new clingo_ast_term();
        ret.type(data.getTermType().getInt());
        ret.location(location.getStructObject());
        data.updateTerm(ret);
        return ret;
    }

    @Override
    public void updateLiteral(clingo_ast_literal ret) {
        ret.field1().symbol(Pointer.getPointer(create()));
    }

    @Override
    public LiteralType getLiteralType() {
        return LiteralType.SYMBOLIC;
    }

    public interface TermData {

        public void updateTerm(clingo_ast_term ret);

        public TermType getTermType();
        
    }

    @Override
    public String toString() {
        return "" + data;
    }

    public static List<Term> list(Pointer<clingo_ast_term> pointer, long size) {
        return pointerList(Term::new, pointer, size);
    }

}
