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
import org.lorislab.clingo4j.api.c.clingo_part;
import org.lorislab.clingo4j.util.Struct;

/**
 *
 * @author Andrej Petras
 */
public class Part extends Struct<clingo_part> {
    
    public Part(clingo_part part) {
        super(part);
    }
    
    public Part(String name) {
        this(new clingo_part());        
        structObject.name(Pointer.pointerToCString(name));
        structObject.params(null);
        structObject.size(0);        
    }

    public Part(String name, List<Symbol> symbols) {
        this(name);
        if (symbols != null && !symbols.isEmpty()) {
            Pointer<Long> tmp = Symbol.array(symbols);
            structObject.params(tmp);
            structObject.size(symbols.size());
        }
    }
        
    public String getName() {
        return structObject.name().getCString();
    }
    
    public List<Symbol> getParameters() {
        return Symbol.list(structObject.params(), (int) structObject.size());
    }

    public static Pointer<clingo_part> array(List<Part> parts) {
        return Struct.array(parts, clingo_part.class);
    }
}
