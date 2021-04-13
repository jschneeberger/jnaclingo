/*
 * BridJ - Dynamic and blazing-fast native interop for Java.
 * http://bridj.googlecode.com/
 *
 * Copyright (c) 2010-2015, Olivier Chafik (http://ochafik.com/)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Olivier Chafik nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY OLIVIER CHAFIK AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.bridj;

import java.util.ArrayList;
import java.util.List;

final class SolidRanges {

    public final long[] offsets, lengths;

    public SolidRanges(long[] offsets, long[] lengths) {
        this.offsets = offsets;
        this.lengths = lengths;
    }

    static class Builder {

        List<Long> offsets = new ArrayList<Long>(), lengths = new ArrayList<Long>();
        long lastOffset = -1, nextOffset = 0;
        int count;

        void add(StructFieldDescription f) {
            long offset = f.byteOffset;
            long length = f.byteLength;

            if (offset == lastOffset) {
		length = Math.max(lengths.get(count - 1), length);
                lengths.set(count - 1, length);
            } else if (offset == nextOffset && count != 0) {
                lengths.set(count - 1, lengths.get(count - 1) + length);
            } else {
                offsets.add(offset);
                lengths.add(length);
                count++;
            }
            lastOffset = offset;
            nextOffset = offset + length;
        }

        SolidRanges toSolidRanges() {
            long[] offsets = new long[count];
            long[] lengths = new long[count];
            for (int i = 0; i < count; i++) {
                offsets[i] = this.offsets.get(i);
                lengths[i] = this.lengths.get(i);
            }
            return new SolidRanges(offsets, lengths);
        }
    }
}
