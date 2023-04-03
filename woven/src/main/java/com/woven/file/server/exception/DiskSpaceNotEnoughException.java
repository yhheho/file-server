package com.woven.file.server.exception;

public class DiskSpaceNotEnoughException extends Exception {
    public DiskSpaceNotEnoughException(String message) {
        super(message);
    }
}
