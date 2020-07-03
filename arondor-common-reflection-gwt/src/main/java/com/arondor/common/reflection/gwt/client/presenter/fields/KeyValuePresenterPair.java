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
package com.arondor.common.reflection.gwt.client.presenter.fields;

import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter.ErrorDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapPairDisplay;

public class KeyValuePresenterPair
{
    private final TreeNodePresenter keyPresenter;

    private final TreeNodePresenter valuePresenter;

    private final MapPairDisplay display;

    public KeyValuePresenterPair(TreeNodePresenter keyPresenter, TreeNodePresenter valuePresenter,
            MapPairDisplay display)
    {
        this.keyPresenter = keyPresenter;
        this.valuePresenter = valuePresenter;
        this.display = display;
    }

    public void displayKeyError(String message)
    {
        if (keyPresenter.getDisplay() instanceof ErrorDisplay)
        {
            ((ErrorDisplay) keyPresenter.getDisplay()).displayError(message);
        }
    }

    public void displayKeyValid()
    {
        if (keyPresenter.getDisplay() instanceof ErrorDisplay)
        {
            ((ErrorDisplay) keyPresenter.getDisplay()).displayValid();
        }
    }

    public TreeNodePresenter getKeyPresenter()
    {
        return keyPresenter;
    }

    public TreeNodePresenter getValuePresenter()
    {
        return valuePresenter;
    }

    public MapPairDisplay getDisplay()
    {
        return display;
    }
}
