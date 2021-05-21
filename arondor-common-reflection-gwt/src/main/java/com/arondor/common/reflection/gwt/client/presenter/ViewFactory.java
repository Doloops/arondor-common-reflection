package com.arondor.common.reflection.gwt.client.presenter;

import java.util.List;

public interface ViewFactory
{
    HierarchicAccessibleClassPresenter.Display createClassDisplay(List<String> scopes);
}
