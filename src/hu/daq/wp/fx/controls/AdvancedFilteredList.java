/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.controls;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Predicate;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 *
 * @author DAQ
 */
public class AdvancedFilteredList<E> implements InvocationHandler {

    FilteredList<E> underlying;

    public AdvancedFilteredList(ObservableList<E> ol, Predicate<? super E> prdct) {
        this.underlying = new FilteredList(ol, prdct);
    }

    public AdvancedFilteredList(ObservableList<E> ol) {
        this.underlying = new FilteredList(ol);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] os) throws Throwable {
        if (method.getName().equalsIgnoreCase("remove")) {
            return method.invoke(this.underlying.getSource(), os);
        }
        return method.invoke(this.underlying, os);
    }

}
