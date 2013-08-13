package com.arondor.common.reflection.noreflect.exception;

public class NoReflectRuntimeException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = 3120927766612533238L;

    public NoReflectRuntimeException(String message)
    {
        super(message);
    }

    public NoReflectRuntimeException(String message, Throwable causedBy)
    {
        super(message, causedBy);
    }

}
