/*
 *  Copyright 2014, Arondor
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
package com.arondor.common.reflection.parser.tools;

import java.util.Collection;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.api.parser.AccessibleClassProvider;

public class MultipleAccessibleClassProvider implements AccessibleClassProvider
{
    private Collection<AccessibleClassProvider> providers;

    public Collection<AccessibleClassProvider> getProviders()
    {
        return providers;
    }

    public void setProviders(Collection<AccessibleClassProvider> providers)
    {
        this.providers = providers;
    }

    public void provideClasses(AccessibleClassCatalog catalog)
    {
        for (AccessibleClassProvider provider : providers)
        {
            provider.provideClasses(catalog);
        }
    }
}
