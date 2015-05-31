package com.arondor.common.reflection.api.instantiator;

/**
 * 
 * @author Francois Barre
 *
 * @param <T>
 *            the callback class
 */
public interface InstantiationCallback<T>
{
    /**
     * Called when an asynchronous call fails to complete normally.
     * {@link IncompatibleRemoteServiceException}s, {@link InvocationException}
     * s, or checked exceptions thrown by the service method are examples of the
     * type of failures that can be passed to this method.
     * 
     * <p>
     * If <code>caught</code> is an instance of an
     * {@link IncompatibleRemoteServiceException} the application should try to
     * get into a state where a browser refresh can be safely done.
     * </p>
     * 
     * @param caught
     *            failure encountered while executing a remote procedure call
     */
    void onFailure(Throwable caught);

    /**
     * Called when an asynchronous call completes successfully.
     * 
     * @param result
     *            the return value of the remote produced call
     */
    void onSuccess(T result);
}
