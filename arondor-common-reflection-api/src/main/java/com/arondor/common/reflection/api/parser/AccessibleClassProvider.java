package com.arondor.common.reflection.api.parser;

import com.arondor.common.reflection.api.accessiblecatalog.AccessibleClassCatalog;

public interface AccessibleClassProvider
{
    void provideClasses(AccessibleClassCatalog catalog);
}
