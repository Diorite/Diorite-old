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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.diorite.impl.CoreMain;
import org.diorite.impl.DioriteCore;
import org.diorite.impl.bean.injector.CodeInjector;
import org.diorite.beans.BeanException;
import org.diorite.beans.BeanManager;
import org.diorite.beans.BeanRegisteringException;

public class BeanManagerImpl implements BeanManager
{
    private final DioriteCore                      core;
    private final CodeInjector                     codeInjector;
    private final List<BeanContainer>              beans;
    private final HashMap<Class<?>, BeanContainer> classToBean;

    public BeanManagerImpl(final DioriteCore core)
    {
        this.core = core;
        this.codeInjector = new CodeInjector(this);
        this.beans = new ArrayList<>(64);
        this.classToBean = new HashMap<>(64);
    }

    public DioriteCore getCore()
    {
        return this.core;
    }

    @Override
    public boolean isBean(final Class<?> clazz)
    {
        return this.classToBean.keySet().contains(clazz);
    }

    @Override
    public boolean isBean(final String name)
    {
        return false;
    }

    @Override
    public <T> T getBean(final Class<T> dioriteBeanClass)
    {
        //noinspection unchecked
        return (T) this.classToBean.get(dioriteBeanClass).getBean();
    }

    @Override
    public <T> T getBean(final String name)
    {
        return null;
    }

    public void doScan(final ClassLoader classLoader, final String pack)
    {
        CoreMain.debug("Scanning class loader " + classLoader + " and package " + pack + " for beans");
        final BeanScanner scanner = new BeanScanner(this, classLoader, pack);
        try
        {
            scanner.doWork();
        }
        catch (final BeanException e)
        {
            this.core.getLogger().error(MessageFormat.format("An error occurred while scanning {0}-{1} for Diorite Beans.", classLoader, pack));
            e.printStackTrace();
        }
    }

    public void registerBean(final BeanContainer beanContainer) throws BeanException
    {
        if (this.isBean(beanContainer.getBeanClass()))
        {
            throw new BeanRegisteringException("Bean " + beanContainer.getBeanClass().getName() + " is arleady registered.");
        }
        this.beans.add(beanContainer);
        this.classToBean.put(beanContainer.getClass(), beanContainer);
        CoreMain.debug("Registered bean " + beanContainer.getBeanClass());
    }

    public void addInjectorCode(final Class<?> clazz)
    {
        this.codeInjector.injectValueInjectorCode(clazz);
    }
}
