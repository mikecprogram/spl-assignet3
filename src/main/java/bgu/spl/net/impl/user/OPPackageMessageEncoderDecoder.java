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
        if (arg == 0 && len == 2) {//OP CODE
            tempPackage.setOPCode(popInt());
            arg = 1;
        } else if (nextByte == '\n') {
            return popOOPackage();
        } else if (nextByte == '\0') {
            switch (arg) {
                case 1:
                    tempPackage.setFirstArg(popString());
                    arg = 2;
                    break;
                case 2:
                    tempPackage.setSecondArg(popString());
                    arg = 0;
                    return popOOPackage();
            }
        }

        pushByte(nextByte);
        return null; //not an OOPackage yet
    }


    @Override
    public byte[] encode(OPPackage message) {
        return (message + "\n").getBytes(); //uses utf8 by default
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private int popInt() {
        if (len == 2) {
            int result = bytes[0] * 8 + bytes[1];
            len = 0;
            return result;
        } else
            throw new IndexOutOfBoundsException("Accidentally popped the Int but the len isn't 2");
    }

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    private OPPackage popOOPackage() {
        return tempPackage.mitosis();
    }
}
