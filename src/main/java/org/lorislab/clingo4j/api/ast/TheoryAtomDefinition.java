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

import org.lorislab.clingo4j.api.ast.enums.TheoryAtomDefinitionType;
import java.util.List;
import java.util.Optional;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_atom_definition;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class TheoryAtomDefinition implements ASTObject<clingo_ast_theory_atom_definition> {

    private final Location location;
    private final TheoryAtomDefinitionType type;
    private final String name;
    private final int arity;
    private final String elements;
    private final Optional<TheoryGuardDefinition> guard;

    public TheoryAtomDefinition(clingo_ast_theory_atom_definition d) {
        this(new Location(d.location()), EnumValue.valueOfInt(TheoryAtomDefinitionType.class, d.type()), d.name().getCString(), d.arity(), d.elements().getCString(), ASTObject.optional(TheoryGuardDefinition::new, d.guard()));
    }

    public TheoryAtomDefinition(Location location, TheoryAtomDefinitionType type, String name, int arity, String elements, Optional<TheoryGuardDefinition> guard) {
        this.location = location;
        this.type = type;
        this.name = name;
        this.arity = arity;
        this.elements = elements;
        this.guard = guard;
    }

    public int getArity() {
        return arity;
    }

    public String getElements() {
        return elements;
    }

    public Optional<TheoryGuardDefinition> getGuard() {
        return guard;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public TheoryAtomDefinitionType getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("&").append(name).append("/").append(arity).append(" : ").append(elements);
        if (guard.isPresent()) {
            sb.append(", ").append(guard.get());
        }
        sb.append(", ").append(type);
        return sb.toString();
    }

    @Override
    public clingo_ast_theory_atom_definition create() {
        clingo_ast_theory_atom_definition ret = new clingo_ast_theory_atom_definition();
        ret.name(Pointer.pointerToCString(name));
        ret.arity(arity);
        ret.location(location.getStructObject());
        ret.type(type.getInt());
        ret.elements(Pointer.pointerToCString(elements));
        ret.guard(ASTObject.optionalPointer(guard));
        return ret;
    }

    public static List<TheoryAtomDefinition> list(Pointer<clingo_ast_theory_atom_definition> pointer, long size) {
        return pointerList(TheoryAtomDefinition::new, pointer, size);
    }

}
