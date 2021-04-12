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

import org.lorislab.clingo4j.api.ast.enums.TermType;
import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.ast.Term.TermData;
import org.lorislab.clingo4j.api.c.clingo_ast_function;
import org.lorislab.clingo4j.api.c.clingo_ast_term;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class Function implements ASTObject<clingo_ast_function>, TermData {

    private final String name;

    private final List<Term> arguments;

    private final boolean external;

    public Function(clingo_ast_function efn, boolean external) {
        this(efn.name().getCString(), Term.list(efn.arguments(), efn.size()), external);
    }

    public Function(String name, List<Term> arguments, boolean external) {
        this.name = name;
        this.arguments = arguments;
        this.external = external;
    }

    public List<Term> getArguments() {
        return arguments;
    }

    public String getName() {
        return name;
    }

    public boolean isExternal() {
        return external;
    }

    @Override
    public String toString() {
        boolean tc = name != null && name.startsWith("0") && arguments != null && arguments.size() == 1;
        boolean ey = (name != null && name.startsWith("0")) || (arguments != null && !arguments.isEmpty());
        return "" + (external ? "@" : "") + name + print(arguments, "(", ",", tc ? ",)" : ")", ey);
    }

    @Override
    public void updateTerm(clingo_ast_term ret) {
        ret.field1().function(createPointer());
    }

    @Override
    public TermType getTermType() {
        return TermType.FUNCTION;
    }

    @Override
    public clingo_ast_function create() {
        clingo_ast_function fn = new clingo_ast_function();
        fn.name(Pointer.pointerToCString(name));
        fn.arguments(ASTObject.array(arguments));
        fn.size(ASTObject.size(arguments));
        return fn;
    }

}
