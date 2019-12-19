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
package com.arondor.common.reflection.gwt.client.view.fields;

import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.view.AbstractChildCreatorNodeView;
import com.google.gwt.user.client.ui.UIObject;

public class MapNodeView extends AbstractChildCreatorNodeView implements MapTreeNodePresenter.MapNodeDisplay
{

    protected MapNodeView(UIObject parentNode)
    {
        super(parentNode);
        setHasRemoveButton(true);
        setActive(true);
    }

    @Override
    public void clear()
    {
        getParentItem().removeItem(this);
    }

    @Override
    public void setProperLabel(String label)
    {
        // TODO Auto-generated method stub

    }

}
