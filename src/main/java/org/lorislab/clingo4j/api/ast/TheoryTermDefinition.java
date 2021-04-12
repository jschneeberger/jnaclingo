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

import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_term_definition;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class TheoryTermDefinition implements ASTObject<clingo_ast_theory_term_definition> {
    
    private final Location location;
    private final String name;
    private final List<TheoryOperatorDefinition> operators;    

    public TheoryTermDefinition(clingo_ast_theory_term_definition d) {
        this(new Location(d.location()), d.name().getCString(), TheoryOperatorDefinition.list(d.operators(), d.size()));
    }
    
    public TheoryTermDefinition(Location location, String name, List<TheoryOperatorDefinition> operators) {
        this.location = location;
        this.name = name;
        this.operators = operators;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public List<TheoryOperatorDefinition> getOperators() {
        return operators;
    }

    @Override
    public String toString() {
        return name + " {\n" + print(operators, "  ", ";\n", "\n", true) + "}";
    }

    @Override
    public clingo_ast_theory_term_definition create() {
        clingo_ast_theory_term_definition ret = new clingo_ast_theory_term_definition();
        ret.location(location.getStructObject());
        ret.name(Pointer.pointerToCString(name));
        ret.operators(ASTObject.array(operators));
        ret.size(ASTObject.size(operators));
        return ret;
    }
    
    public static List<TheoryTermDefinition> list(Pointer<clingo_ast_theory_term_definition> pointer, long size) {
        return pointerList(TheoryTermDefinition::new, pointer, size);
    }
    
}
