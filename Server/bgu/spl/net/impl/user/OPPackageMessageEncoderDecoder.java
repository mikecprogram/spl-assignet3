package bgu.spl.net.impl.user;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class OPPackageMessageEncoderDecoder implements MessageEncoderDecoder<OPPackage> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private OPPackage tempPackage = new OPPackage();
    private int arg = 0;
    private int len = 0;


    @Override
    public OPPackage decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        pushByte(nextByte);

        if (len == 2) {
            //OP CODE
            if (arg == 0) {
                tempPackage.setOPCode(popShort());
                arg = 1;
            } else if (arg == 1) {
                tempPackage.setFirstArg_Short(getAsShort());
            }
        }
        if (nextByte == '\0' && tempPackage.needString()) {
            switch (arg) {
                case 1:
                    tempPackage.setFirstArg_Str(popString());
                    arg = 2;
                    break;
                case 2:
                    tempPackage.setSecondArg_Str(popString());
                    arg = 0;
                    return popOOPackage();
            }
        }
        if (tempPackage.isValid()) {
            return popOOPackage();
        }
        return null; //not an OOPackage yet
    }



    @Override
    public byte[] encode(OPPackage packet) {
        byte[] result = shortToBytes(packet.getOPCode());
        result = merge(result, shortToBytes(packet.getFirstArg_Short()));
        if (packet.isFirstArg_Str_HasVal()) {
            result = merge(result, (packet.getFirstArg_Str() + '\0').getBytes());
            if (packet.isSecondArg_Str_HasVal()) {
                result = merge(result, (packet.getSecondArg_Str() + '\0').getBytes());
            }
        }
        return result;
    }

    private byte[] merge(byte[] a, byte[] b) {
        int resultLen = a.length;
        a = Arrays.copyOf(a, resultLen + b.length);
        System.arraycopy(b, 0, a, resultLen, b.length);
        return a;
    }

    private byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    private short getAsShort() {
        short result = (short) ((bytes[0] & 0xff) << 8);
        result += (short) (bytes[1] & 0xff);
        return result;
    }

    private short popShort() {
        if (len == 2) {
            len = 0;
            return getAsShort();
        } else
            throw new IndexOutOfBoundsException("Accidentally popped the Int but the len isn't 2");
    }

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        len--;//In order to not get the \0
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    private OPPackage popOOPackage() {
        OPPackage mitosed = tempPackage.mitosis();
        tempPackage.clear();
        len =0;
        arg = 0;
        return mitosed;
    }
}
