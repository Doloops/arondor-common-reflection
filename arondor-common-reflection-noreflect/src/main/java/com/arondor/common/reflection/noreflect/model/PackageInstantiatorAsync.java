package com.arondor.common.reflection.noreflect.model;

import com.arondor.common.reflection.api.instantiator.InstantiationCallback;

public interface PackageInstantiatorAsync
{
    void instantiatePackage(InstantiationCallback<Void> callback);
}
