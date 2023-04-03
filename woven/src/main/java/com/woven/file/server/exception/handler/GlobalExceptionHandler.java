package com.woven.file.server.exception.handler;

import com.woven.file.server.exception.DiskSpaceNotEnoughException;
import com.woven.file.server.exception.FileSizeTooBigException;
import com.woven.file.server.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

import static com.woven.file.server.constant.Constant.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        error.setErrorCode(GENERAL_EXCEPTION_ERROR_CODE);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleFileNameExistException(FileAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        error.setErrorCode(FILE_NAME_ALREADY_EXIST_ERROR_CODE);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNameExistException(FileNotFoundException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        error.setErrorCode(FILE_NOT_FOUND_ERROR_CODE);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileSizeTooBigException.class)
    public ResponseEntity<ErrorResponse> handleFileSizeTooBigException(FileSizeTooBigException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        error.setErrorCode(FILE_SIZE_TOO_LARGE_ERROR_CODE);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DiskSpaceNotEnoughException.class)
    public ResponseEntity<ErrorResponse> handleDiskSpaceNotEnoughException(DiskSpaceNotEnoughException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        error.setErrorCode(DISK_SPACE_NOT_ENOUGH_ERROR_CODE);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}