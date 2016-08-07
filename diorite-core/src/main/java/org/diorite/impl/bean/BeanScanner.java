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

package org.diorite.impl.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import org.diorite.impl.bean.providers.ClassBeanProvider;
import org.diorite.impl.bean.providers.ConstructorBeanProvider;
import org.diorite.impl.bean.providers.MethodBeanProvider;
import org.diorite.beans.BeanException;
import org.diorite.beans.BeanRegisteringException;
import org.diorite.beans.annotation.DioriteBean;
import org.diorite.beans.annotation.InjectedValue;

public class BeanScanner
{
    private static final Package JAVA_PACKAGE = Package.getPackage("java");
    private final BeanManagerImpl beanManager;
    private final Reflections     reflections;

    public BeanScanner(final BeanManagerImpl beanManager, final ClassLoader classLoader, final String packageToScan)
    {
        this.beanManager = beanManager;
        final ConfigurationBuilder config = new ConfigurationBuilder();

        config.setClassLoaders(new ClassLoader[] {classLoader});
        config.setUrls(ClasspathHelper.forPackage(packageToScan, classLoader));
        if (StringUtils.isNotEmpty(packageToScan))
        {
            config.filterInputsBy(new FilterBuilder().includePackage(packageToScan));
        }
        config.useParallelExecutor();
        config.setScanners(new SubTypesScanner(false));

        this.reflections = new Reflections(config);
    }

    public void doWork() throws BeanException
    {
        for (final Class<?> aClass : this.reflections.getSubTypesOf(Object.class))
        {
            this.processClass(aClass);
        }
    }

    private void processClass(final Class<?> clazz) throws BeanException
    {
        // Bean registration by class
        if (clazz.isAnnotationPresent(DioriteBean.class))
        {
            final DioriteBean beanInfo = clazz.getAnnotation(DioriteBean.class);
            final BeanContainer beanContainer = new BeanContainer(clazz, new ClassBeanProvider(clazz));

            this.beanManager.registerBean(beanContainer);
        }

        // Bean registration by constructor
        for (final Constructor<?> constructor : clazz.getConstructors())
        {
            if (constructor.isAnnotationPresent(DioriteBean.class))
            {
                final DioriteBean beanInfo = constructor.getAnnotation(DioriteBean.class);
                final BeanContainer beanContainer = new BeanContainer(clazz, new ConstructorBeanProvider(constructor));

                this.beanManager.registerBean(beanContainer);
            }
        }

        // Bean registration by method
        for (final Method method : clazz.getMethods())
        {
            if (method.isAnnotationPresent(DioriteBean.class))
            {
                final Class<?> returnType = method.getReturnType();
                if (returnType.equals(Void.TYPE) || returnType.isPrimitive())
                {
                    throw new BeanRegisteringException(MessageFormat.format("Can't register method '{0}' in class '{1}' as Diorite Bean. Method must return object.", method.getName(), clazz.getName()));
                }
                else if (returnType.getPackage().equals(JAVA_PACKAGE))
                {
                    throw new BeanRegisteringException(MessageFormat.format("Can't register method '{0}' in class '{1}' as Diorite Bean. Method can't return object from java package.", method.getName(), clazz.getName()));
                }
                final DioriteBean beanInfo = method.getAnnotation(DioriteBean.class);

                final BeanContainer beanContainer = new BeanContainer(returnType, new MethodBeanProvider(method));
                this.beanManager.registerBean(beanContainer);
            }
        }

        for (final Field field : clazz.getDeclaredFields())
        {
            if (field.isAnnotationPresent(InjectedValue.class))
            {
                this.beanManager.addInjectorCode(clazz);
                break;
            }
        }
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("beanManager", this.beanManager).toString();
    }
}
