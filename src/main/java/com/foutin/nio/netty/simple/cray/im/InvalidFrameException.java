package com.foutin.nio.netty.simple.cray.im;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 16:30
 */
public class InvalidFrameException extends Exception {

    public InvalidFrameException(String s) {
        super(s);
    }
}
