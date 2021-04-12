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

import org.lorislab.clingo4j.api.ast.enums.TheoryOperatorType;
import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_operator_definition;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class TheoryOperatorDefinition implements ASTObject<clingo_ast_theory_operator_definition> {
 
    private final Location location;
    private final String name;
    private final int priority;
    private final TheoryOperatorType type;

    public TheoryOperatorDefinition(clingo_ast_theory_operator_definition d) {
        this(new Location(d.location()), d.name().getCString(), d.priority(), EnumValue.valueOfInt(TheoryOperatorType.class, d.type()));
    }
    
    public TheoryOperatorDefinition(Location location, String name, int priority, TheoryOperatorType type) {
        this.location = location;
        this.name = name;
        this.priority = priority;
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public TheoryOperatorType getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + " : " + priority + ", " + type;
    }

    @Override
    public clingo_ast_theory_operator_definition create() {
        clingo_ast_theory_operator_definition ret = new clingo_ast_theory_operator_definition();
        ret.type(type.getInt());
        ret.priority(priority);
        ret.location(location.getStructObject());
        ret.name(Pointer.pointerToCString(name));
        return ret;
    }
    
    public static List<TheoryOperatorDefinition> list(Pointer<clingo_ast_theory_operator_definition> pointer, long size) {
        return pointerList(TheoryOperatorDefinition::new, pointer, size);
    }

}
