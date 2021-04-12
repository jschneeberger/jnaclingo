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
import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.ast.TheoryTerm.TheoryTermData;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_function;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_term;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class TheoryFunction implements ASTObject<clingo_ast_theory_function>, TheoryTermData {

    private final String name;

    private final List<TheoryTerm> arguments;

    public TheoryFunction(clingo_ast_theory_function t) {
        this(t.name().getCString(), TheoryTerm.list(t.arguments(), t.size()));
    }

    public TheoryFunction(String name, List<TheoryTerm> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public List<TheoryTerm> getArguments() {
        return arguments;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "" + name + print(arguments, "(", ",", ")", !(arguments == null || arguments.isEmpty()));
    }

    @Override
    public clingo_ast_theory_function create() {
        clingo_ast_theory_function fun = new clingo_ast_theory_function();
        fun.name(Pointer.pointerToCString(name));
        fun.arguments(ASTObject.array(arguments));
        fun.size(ASTObject.size(arguments));
        return fun;
    }

    @Override
    public void updateTheoryTerm(clingo_ast_theory_term ret) {
        ret.field1().function(createPointer());
    }

    @Override
    public TheoryTermType getTheoryTermType() {
        return TheoryTermType.FUNCTIONS;
    }

}
