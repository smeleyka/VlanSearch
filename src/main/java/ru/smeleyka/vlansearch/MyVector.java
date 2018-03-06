package ru.smeleyka.vlansearch;


import java.util.Iterator;
import java.util.Vector;

public class MyVector<S extends Switch> extends Vector<S> {

    private static Switch temp;

    @Override
    public synchronized boolean add(S aSwitch) {
        Iterator iterator = super.iterator();
        while (iterator.hasNext()) {
            temp = ((Switch) iterator.next());
            if (temp.getName().equals(aSwitch.getName())) {
                if (temp.biggerThan(aSwitch)) {
                    iterator.remove();
                    return super.add(aSwitch);
                } else return false;
            }
        }
        return super.add(aSwitch);
    }
}
