/*
 *  Copyright 2013, Arondor
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ImplementingClassPresenter;
import com.google.gwt.user.client.ui.UIObject;

public class ClassTreeNodeView extends AbstractChildCreatorNodeView implements ClassTreeNodePresenter.ClassDisplay
{
    private final ImplementingClassPresenter.ImplementingClassDisplay implementingClassDisplay = new ImplementingClassView();

    public ClassTreeNodeView(UIObject parentNode)
    {
        super(parentNode);
        setHasRemoveButton(true);
        getContents().add(implementingClassDisplay.asWidget());
    }

    @Override
    public ImplementingClassPresenter.ImplementingClassDisplay getImplementingClassDisplay()
    {
        return implementingClassDisplay;
    }

    @Override
    public void clear()
    {
        removeItems();
    }

    @Override
    public void setProperLabel(String label)
    {
        // TODO Auto-generated method stub

    }

    // @Override
    // public void setDefaultValue(String defaultValue)
    // {
    // // TODO Auto-generated method stub
    //
    // }
}