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
package org.lorislab.clingo4j.api;

import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.c.clingo_weighted_literal;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;
import org.lorislab.clingo4j.util.Struct;

/**
 *
 * @author Andrej Petras
 */
public class WeightedLiteral extends Struct<clingo_weighted_literal> {
    
    public WeightedLiteral(clingo_weighted_literal literal) {
        super(literal);
    }
    
    public WeightedLiteral(int literal, int weight) {
        super(new clingo_weighted_literal());
        this.structObject.weight(weight);
        this.structObject.literal(literal);
    }

    public int getWeight() {
        return structObject.weight();
    }
    
    public int getLiteral() {
        return structObject.literal();
    }

    public static Pointer<clingo_weighted_literal> array(List<WeightedLiteral> list) {
        return Struct.array(list, clingo_weighted_literal.class);
    }
    
    public static List<WeightedLiteral> list(Pointer<clingo_weighted_literal> pointer, long size) {
        return pointerList(WeightedLiteral::new, pointer, size);
    }

}
