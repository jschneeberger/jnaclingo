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

import org.lorislab.clingo4j.api.ast.enums.StatementType;
import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.ast.Statement.StatementData;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_definition;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class TheoryDefinition implements ASTObject<clingo_ast_theory_definition>, StatementData {

    private final String name;
    private final List<TheoryTermDefinition> terms;
    private final List<TheoryAtomDefinition> atoms;

    public TheoryDefinition(clingo_ast_theory_definition d) {
        this(d.name().getCString(), TheoryTermDefinition.list(d.terms(), d.terms_size()), TheoryAtomDefinition.list(d.atoms(), d.atoms_size()));
    }
    
    public TheoryDefinition(String name, List<TheoryTermDefinition> terms, List<TheoryAtomDefinition> atoms) {
        this.name = name;
        this.terms = terms;
        this.atoms = atoms;
    }

    public List<TheoryAtomDefinition> getAtoms() {
        return atoms;
    }

    public String getName() {
        return name;
    }

    public List<TheoryTermDefinition> getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#theory ").append(name).append(" {\n");
        boolean comma = false;
        if (terms != null) {
            for (TheoryTermDefinition term : terms) {
                if (comma) {
                    sb.append(";\n");
                } else {
                    comma = true;
                }
                sb.append("  ").append(term.getName()).append(" {\n");
                sb.append(print(term.getOperators(), "    ", ";\n", "\n", true)).append("  }");
            }
        }

        if (atoms != null) {
            for (TheoryAtomDefinition atom : atoms) {
                if (comma) {
                    sb.append(";\n");
                } else {
                    comma = true;
                }
                sb.append("  ").append(atom);
            }
        }

        if (comma) {
            sb.append("\n");
        }
        sb.append("}.");
        return sb.toString();
    }

    @Override
    public clingo_ast_theory_definition create() {
        clingo_ast_theory_definition ret = new clingo_ast_theory_definition();
        ret.name(Pointer.pointerToCString(name));
        ret.terms(ASTObject.array(terms));
        ret.terms_size(ASTObject.size(terms));
        ret.atoms(ASTObject.array(atoms));
        ret.atoms_size(ASTObject.size(atoms));
        return ret;
    }

    @Override
    public void updateStatement(clingo_ast_statement ret) {
        ret.field1().theory_definition(createPointer());
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.THEORY_DEFINITION;
    }

}
