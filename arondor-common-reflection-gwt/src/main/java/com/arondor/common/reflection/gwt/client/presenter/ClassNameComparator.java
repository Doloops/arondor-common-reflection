package com.arondor.common.reflection.gwt.client.presenter;

import java.util.Comparator;

public class ClassNameComparator implements Comparator<String>
{
    public int compare(String arg0, String arg1)
    {
        if (arg0.equals(ImplementingClassPresenter.NULL_VALUE))
        {
            return -1;
        }
        if (arg1.equals(ImplementingClassPresenter.NULL_VALUE))
        {
            return 1;
        }
        return arg0.compareTo(arg1);
    }
}
