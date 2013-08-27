package com.arondor.common.reflection.gwt.client.presenter.fields;

import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;

public class KeyValuePresenterPair
{
    private final TreeNodePresenter keyPresenter;

    private final TreeNodePresenter valuePresenter;

    public KeyValuePresenterPair(TreeNodePresenter keyPresenter, TreeNodePresenter valuePresenter)
    {
        this.keyPresenter = keyPresenter;
        this.valuePresenter = valuePresenter;
    }

    public TreeNodePresenter getKeyPresenter()
    {
        return keyPresenter;
    }

    public TreeNodePresenter getValuePresenter()
    {
        return valuePresenter;
    }
}
