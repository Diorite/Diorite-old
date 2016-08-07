/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016. Diorite (by Bartłomiej Mazur (aka GotoFinal))
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.diorite.impl.bean.injector;

import org.diorite.impl.bean.BeanManagerImpl;
import org.diorite.utils.collections.WeakCollection;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;

public class CodeInjector
{
    private final ClassPool classPool = ClassPool.getDefault();
    private final BeanManagerImpl beanManager;
    private final WeakCollection<Class<?>> injectedClasses;

    public CodeInjector(final BeanManagerImpl beanManager)
    {
        this.beanManager = beanManager;
        this.injectedClasses = new WeakCollection<>(256);
        this.classPool.importPackage("org.diorite.impl.bean.injector");
    }

    public void injectValueInjectorCode(final Class<?> clazz)
    {
        if (this.beanManager.isBean(clazz) || this.injectedClasses.contains(clazz))
        {
            return; // We arleady have this code
        }

        final CtClass ctClass;
        try
        {
            ctClass = this.classPool.getCtClass(clazz.getName());
        }
        catch (NotFoundException e)
        {
            e.printStackTrace();
            return;
        }

        final CtConstructor[] cs = ctClass.getConstructors();
        try
        {
            ctClass.addField(new CtField(CtPrimitiveType.booleanType, "DioriteIsValuesInjected", ctClass));
            for (final CtConstructor c : cs)
            {
                c.insertBefore("if(DioriteIsValuesInjected==false){Worker.injectValues(this);DioriteIsValuesInjected=true;}");
            }
            ctClass.toClass();
        }
        catch (final CannotCompileException e)
        {
            e.printStackTrace();
        }

        this.injectedClasses.add(clazz);
    }
}
