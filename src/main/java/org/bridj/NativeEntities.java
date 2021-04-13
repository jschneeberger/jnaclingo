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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Collection of handles to natively-bound classes and methods (which native
 * resources can be released all at once).
 *
 * @author ochafik
 */
public class NativeEntities {

    static class CBInfo {

        long handle;
        int size;

        public CBInfo(long handle, int size) {
            this.handle = handle;
            this.size = size;
        }
    }
    Map<Class<?>, CBInfo> functions = new HashMap<Class<?>, CBInfo>(),
            virtualMethods = new HashMap<Class<?>, CBInfo>(),
            //getters = new HashMap<Class<?>, CBInfo>(),
            //setters = new HashMap<Class<?>, CBInfo>(),
            javaToNativeCallbacks = new HashMap<Class<?>, CBInfo>(),
            //cppMethods = new HashMap<Class<?>, CBInfo>(),
            objcMethodInfos = new HashMap<Class<?>, CBInfo>();

    /**
     * Helper class to build a NativeEntities instance easily.
     */
    public static class Builder {

        List<MethodCallInfo> functionInfos = new ArrayList<MethodCallInfo>(),
                virtualMethods = new ArrayList<MethodCallInfo>(),
                javaToNativeCallbacks = new ArrayList<MethodCallInfo>(),
                //getters = new ArrayList<MethodCallInfo>(),
                cppMethodInfos = new ArrayList<MethodCallInfo>(),
                objcMethodInfos = new ArrayList<MethodCallInfo>();
        //List<MethodCallInfo> getterInfos = new ArrayList<MethodCallInfo>();

        public void addFunction(MethodCallInfo info) {
            functionInfos.add(info);
        }

        public void addVirtualMethod(MethodCallInfo info) {
            virtualMethods.add(info);
        }
        /*public void addGetter(MethodCallInfo info) {
         getters.add(info);
         }
         public void addSetter(MethodCallInfo info) {
         getters.add(info);
         }*/

        public void addJavaToNativeCallback(MethodCallInfo info) {
            javaToNativeCallbacks.add(info);
        }

        public void addMethodFunction(MethodCallInfo info) {
            cppMethodInfos.add(info);
        }//*/

        public void addObjCMethod(MethodCallInfo info) {
            objcMethodInfos.add(info);
        }
    }

    /**
     * Free everything (native callbacks, bindings, etc...).<br>
     * Called automatically by {@link NativeEntities#finalize()} upon garbage
     * collection.
     */
    @SuppressWarnings("deprecation")
    public void release() {
        if (BridJ.debugNeverFree) {
            return;
        }

        for (CBInfo callbacks : functions.values()) {
            JNI.freeCFunctionBindings(callbacks.handle, callbacks.size);
        }

        /*
         for (CBInfo callbacks : cppMethods.values())
         JNI.freeCPPMethodBindings(callbacks.handle, callbacks.size);
         //*/
        for (CBInfo callbacks : javaToNativeCallbacks.values()) {
            JNI.freeJavaToCCallbacks(callbacks.handle, callbacks.size);
        }

        for (CBInfo callbacks : virtualMethods.values()) {
            JNI.freeVirtualMethodBindings(callbacks.handle, callbacks.size);
        }

        //for (CBInfo callbacks : getters.values())
        //    JNI.freeGetters(callbacks.handle, callbacks.size);

        for (CBInfo callbacks : objcMethodInfos.values()) {
            JNI.freeObjCMethodBindings(callbacks.handle, callbacks.size);
        }
    }

    @Override
    public void finalize() {
        release();
    }

    @SuppressWarnings("deprecation")
    public void addDefinitions(Class<?> type, Builder builder) {
        int n;
        try {

            n = builder.functionInfos.size();
            if (n != 0) {
                functions.put(type, new CBInfo(JNI.bindJavaMethodsToCFunctions(builder.functionInfos.toArray(new MethodCallInfo[n])), n));
            }

            n = builder.virtualMethods.size();
            if (n != 0) {
                virtualMethods.put(type, new CBInfo(JNI.bindJavaMethodsToVirtualMethods(builder.virtualMethods.toArray(new MethodCallInfo[n])), n));
            }

            n = builder.javaToNativeCallbacks.size();
            if (n != 0) {
                javaToNativeCallbacks.put(type, new CBInfo(JNI.bindJavaToCCallbacks(builder.javaToNativeCallbacks.toArray(new MethodCallInfo[n])), n));
            }

            /*
             n = builder.cppMethodInfos.size();
             if (n != 0)
             cppMethods.put(type, new CBInfo(JNI.bindJavaMethodsToCPPMethods(builder.cppMethodInfos.toArray(new MethodCallInfo[n])), n));
             //*/
            n = builder.objcMethodInfos.size();
            if (n != 0) {
                objcMethodInfos.put(type, new CBInfo(JNI.bindJavaMethodsToObjCMethods(builder.objcMethodInfos.toArray(new MethodCallInfo[n])), n));
            }

            /*n = builder.getters.size();
             if (n != 0)
             getters.put(type, new CBInfo(JNI.bindGetters(builder.getters.toArray(new MethodCallInfo[n])), n));
             */
        } catch (Throwable th) {
            assert BridJ.error("Failed to add native definitions for class " + type.getName(), th);
        }
    }
}
