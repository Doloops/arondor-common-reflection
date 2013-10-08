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

import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.ClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.ListTreeNodePresenter.ListRootDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapRootDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter.StringListDisplay;
import com.arondor.common.reflection.gwt.client.view.fields.ListRootView;
import com.arondor.common.reflection.gwt.client.view.fields.MapRootView;
import com.arondor.common.reflection.gwt.client.view.fields.PrimitiveBooleanView;
import com.arondor.common.reflection.gwt.client.view.fields.PrimitiveStringView;
import com.arondor.common.reflection.gwt.client.view.fields.StringListView;
import com.google.gwt.user.client.ui.UIObject;

public abstract class AbstractChildCreatorNodeView extends AbstractTreeNodeView implements
        TreeNodePresenter.ChildCreatorDisplay
{

    protected AbstractChildCreatorNodeView(UIObject parentNode)
    {
        super(parentNode);
    }

    public ClassDisplay createClassChild()
    {
        return new ClassTreeNodeView(getTreeItem());
    }

    public PrimitiveDisplay createPrimitiveChild(String fieldClassName)
    {
        if (fieldClassName.equals("boolean") || fieldClassName.equals("java.lang.Boolean"))
        {
            return new PrimitiveBooleanView(getTreeItem());
        }
        PrimitiveStringView primitiveStringView = new PrimitiveStringView(getTreeItem());
        if (fieldClassName.equals("java.lang.String"))
        {
            primitiveStringView.setTextWidth(400);
        }
        return primitiveStringView;
    }

    public MapRootDisplay createMapChild()
    {
        return new MapRootView(getTreeItem());
    }

    public ListRootDisplay createListChild()
    {
        return new ListRootView(getTreeItem());
    }

    public StringListDisplay createStringListChild()
    {
        return new StringListView(getTreeItem());
    }
}
