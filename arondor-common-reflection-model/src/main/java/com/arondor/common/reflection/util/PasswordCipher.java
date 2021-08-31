package com.arondor.common.reflection.util;

public class PasswordCipher
{
    private static final String CIPHER_PREFIX = "xr1c/";

    private static final byte CIPHER[] = "1c10ec0c28b54f618ecb9f8a68bf610b".getBytes();

    private static final byte HEX[] = "0123456789abcdef".getBytes();

    public String encode(String source)
    {
        byte src[] = source.getBytes();

        byte tgt[] = new byte[src.length * 2];

        for (int idx = 0; idx < src.length; idx++)
        {
            byte car = (byte) (src[idx] ^ CIPHER[idx % CIPHER.length]);
            tgt[idx * 2] = HEX[car >> 4];
            tgt[idx * 2 + 1] = HEX[car & 0xf];
        }
        return CIPHER_PREFIX + new String(tgt);
    }

    int hex2int(byte b)
    {
        if ('0' <= b && b <= '9')
            return b - '0';
        if ('a' <= b && b <= 'f')
            return b - 'a' + 10;
        if ('A' <= b && b <= 'F')
            return b - 'A' + 10;
        throw new IllegalArgumentException("Unexpected byte 0x" + Integer.toHexString(b));
    }

    public String decode(String encoded)
    {
        if (!encoded.startsWith(CIPHER_PREFIX))
            throw new IllegalArgumentException("Unexpected encoded string !");

        byte src[] = encoded.substring(CIPHER_PREFIX.length()).getBytes();
        byte tgt[] = new byte[src.length / 2];

        for (int idx = 0; idx < src.length / 2; idx++)
        {
            int car = (hex2int(src[idx * 2]) * 16 + hex2int(src[idx * 2 + 1]));
            tgt[idx] = (byte) (car ^ CIPHER[idx % CIPHER.length]);
        }
        return new String(tgt);
    }
}
