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
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import org.lorislab.clingo4j.api.ClingoException;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;

/**
 *
 * @author Andrej Petras
 */
public class Signature {

    private final long pointer;

    public Signature(long pointer) {
        this.pointer = pointer;
    }

    public Signature(String name, int arity, boolean positive) throws ClingoException {
        Pointer<Long> tmp = Pointer.allocateLong();
        handleError(LIB.clingo_signature_create(Pointer.pointerToCString(name), arity, positive, tmp), "Error creating the signature!");
        pointer = tmp.get();
    }

    public long getPointer() {
        return pointer;
    }

    public String getName() {
        Pointer<Byte> tmp = LIB.clingo_signature_name(pointer);
        return tmp.getCString();
    }

    public int getArity() {
        return LIB.clingo_signature_arity(pointer);
    }

    public boolean isPositive() {
        return LIB.clingo_signature_is_positive(pointer);
    }

    public boolean isNegative() {
        return LIB.clingo_signature_is_negative(pointer);
    }

    public long getHash() {
        return LIB.clingo_signature_hash(pointer);
    }

    @Override
    public String toString() {
        return (isNegative() ? "-" : "") + getName() + "/" + getArity();
    }

    public boolean isEqual(Signature s) {
        return LIB.clingo_signature_is_equal_to(pointer, s.pointer);
    }

    public boolean isNotEqual(Signature s) {
        return !isEqual(s);
    }

    public boolean isLessThan(Signature s) {
        return LIB.clingo_signature_is_less_than(pointer, s.pointer);
    }

    public boolean isLessEqualThan(Signature s) {
        return !LIB.clingo_signature_is_less_than(s.pointer, pointer);
    }

    public boolean isMoreThan(Signature s) {
        return LIB.clingo_signature_is_less_than(s.pointer, pointer);
    }

    public boolean isMoreEqualThan(Signature s) {
        return !LIB.clingo_signature_is_less_than(pointer, s.pointer);
    }

    public static List<Signature> list(Pointer<Long> pointer, long size) {
        return pointerList(Signature::new, pointer, size);
    }

}
