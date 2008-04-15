package com.serotonin.bacnet4j.base;

import com.serotonin.util.queue.ByteQueue;

public class BACnetUtils {
    public static void pushShort(ByteQueue queue, long value) {
        queue.push((byte)(0xff & (value >> 8)));
        queue.push((byte)(0xff & value));
    }
    
    public static void pushInt(ByteQueue queue, long value) {
        queue.push((byte)(0xff & (value >> 24)));
        queue.push((byte)(0xff & (value >> 16)));
        queue.push((byte)(0xff & (value >> 8)));
        queue.push((byte)(0xff & value));
    }
    
    public static void pushLong(ByteQueue queue, long value) {
        queue.push((byte)(0xff & (value >> 56)));
        queue.push((byte)(0xff & (value >> 48)));
        queue.push((byte)(0xff & (value >> 40)));
        queue.push((byte)(0xff & (value >> 32)));
        queue.push((byte)(0xff & (value >> 24)));
        queue.push((byte)(0xff & (value >> 16)));
        queue.push((byte)(0xff & (value >> 8)));
        queue.push((byte)(0xff & value));
    }
    
    public static int popShort(ByteQueue queue) {
        return (short)((toInt(queue.pop()) << 8) | toInt(queue.pop()));
    }
    
    public static int popInt(ByteQueue queue) {
        return (toInt(queue.pop()) << 24) | (toInt(queue.pop()) << 16)
                | (toInt(queue.pop()) << 8) | toInt(queue.pop());
    }
    
    public static long popLong(ByteQueue queue) {
        return (toLong(queue.pop()) << 56) | (toLong(queue.pop()) << 48)
                | (toLong(queue.pop()) << 40) | (toLong(queue.pop()) << 32)
                | (toLong(queue.pop()) << 24) | (toLong(queue.pop()) << 16)
                | (toLong(queue.pop()) << 8) | toLong(queue.pop());
    }
    
    public static int toInt(byte b) {
        return b & 0xff;
    }
    
    public static long toLong(byte b) {
        return (long)(b & 0xff);
    }
    
    public static byte[] convertToBytes(boolean[] bdata) {
        int byteCount = (bdata.length + 7) / 8;
        byte[] data = new byte[byteCount];
        for (int i=0; i<bdata.length; i++)
            data[i / 8] |= (bdata[i] ? 1 : 0) << (7 - (i % 8));
        return data;
    }

    public static boolean[] convertToBooleans(byte[] data, int length) {
        boolean[] bdata = new boolean[length];
        for (int i=0; i<bdata.length; i++)
            bdata[i] = ((data[i/8] >> (7 - (i%8))) & 0x1) == 1;
        return bdata;
    }
}
