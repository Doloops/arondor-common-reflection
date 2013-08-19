package com.arondor.common.reflection.api.parser;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;

public interface AccessibleClassProvider
{
    void provideClasses(AccessibleClassCatalog catalog);
}
