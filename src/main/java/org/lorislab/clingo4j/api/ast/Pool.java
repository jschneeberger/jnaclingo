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
import org.lorislab.clingo4j.api.ast.Term.TermData;
import org.lorislab.clingo4j.api.c.clingo_ast_pool;
import org.lorislab.clingo4j.api.c.clingo_ast_term;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class Pool implements ASTObject<clingo_ast_pool>, TermData {

    private final List<Term> arguments;

    public Pool(clingo_ast_pool t) {
        this(Term.list(t.arguments(), t.size()));
    }
    
    public Pool(List<Term> arguments) {
        this.arguments = arguments;
    }

    public List<Term> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        if (arguments == null || arguments.isEmpty()) {
            return "(1/0)";
        }
        return print(arguments, "(", ";", ")", true);
    }

    @Override
    public void updateTerm(clingo_ast_term ret) {
        ret.field1().pool(createPointer());
    }

    @Override
    public TermType getTermType() {
        return TermType.POOL;
    }

    @Override
    public clingo_ast_pool create() {
        clingo_ast_pool pool = new clingo_ast_pool();
        pool.arguments(ASTObject.array(arguments));
        pool.size(ASTObject.size(arguments));
        return pool;
    }

}
