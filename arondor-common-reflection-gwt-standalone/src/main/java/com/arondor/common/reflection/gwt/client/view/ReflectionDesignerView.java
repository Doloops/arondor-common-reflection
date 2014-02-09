package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.ReflectionDesignerPresenter.DesignerDisplay;
import com.arondor.common.reflection.gwt.client.presenter.ReflectionDesignerPresenter.MenuDisplay;
import com.arondor.common.reflection.gwt.client.presenter.SimpleObjectConfigurationMapPresenter.ObjectConfigurationMapDisplay;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReflectionDesignerView extends VerticalPanel implements DesignerDisplay
{
    public void setMenuDisplay(MenuDisplay menuDisplay)
    {
        add(menuDisplay);
    }

    public void setObjectConfigurationMapDisplay(ObjectConfigurationMapDisplay objectConfigurationMapDisplay)
    {
        add(objectConfigurationMapDisplay.getDisplayWidget());
    }
}
