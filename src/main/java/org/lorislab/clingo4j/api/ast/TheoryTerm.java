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

import org.lorislab.clingo4j.api.ast.enums.TheoryTermType;
import org.lorislab.clingo4j.api.ast.enums.TheoryTermSequenceType;
import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.Symbol;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_term;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class TheoryTerm implements ASTObject<clingo_ast_theory_term> {

    private final Location location;

    //Symbol, Variable, TheoryTermSequence, TheoryFunction, TheoryUnparsedTerm
    private final TheoryTermData data;

    public TheoryTerm(clingo_ast_theory_term t) {
        TheoryTermType type = EnumValue.valueOfInt(TheoryTermType.class, t.type());
        if (type != null) {
            location = new Location(t.location());
            switch (type) {
                case SYMBOL:
                    data = new Symbol(t.field1().symbol());
                    break;
                case VARIABLE:
                    data = new Variable(t.field1().variable().getCString());
                    break;
                case LIST:
                    data = new TheoryTermSequence(TheoryTermSequenceType.LIST, t.field1().tuple().get());
                    break;
                case SET:
                    data = new TheoryTermSequence(TheoryTermSequenceType.SET, t.field1().tuple().get());
                    break;
                case TUPLE:
                    data = new TheoryTermSequence(TheoryTermSequenceType.TUPLE, t.field1().tuple().get());
                    break;
                case FUNCTIONS:
                    data = new TheoryFunction(t.field1().function().get());
                    break;
                case UNPARSED_TERM:
                    data = new TheoryUnparsedTerm(t.field1().unparsed_term().get());
                    break;
                default:
                    data = null;
            }
        } else {
            throw new RuntimeException("cannot happen");
        }
    }

    public TheoryTerm(Location location, TheoryTermData data) {
        this.location = location;
        this.data = data;
    }

    @Override
    public clingo_ast_theory_term create() {
        clingo_ast_theory_term result = new clingo_ast_theory_term();
        result.type(data.getTheoryTermType().getInt());
        result.location(location.getStructObject());
        data.updateTheoryTerm(result);
        return result;
    }

    public TheoryTermData getData() {
        return data;
    }

    public Location getLocation() {
        return location;
    }

    public interface TheoryTermData {

        public void updateTheoryTerm(clingo_ast_theory_term ret);

        public TheoryTermType getTheoryTermType();
    }

    @Override
    public String toString() {
        return "" + data;
    }

    public static List<TheoryTerm> list(Pointer<clingo_ast_theory_term> pointer, long size) {
        return pointerList(TheoryTerm::new, pointer, size);
    }

}
