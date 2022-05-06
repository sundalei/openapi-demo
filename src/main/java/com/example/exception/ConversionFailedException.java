/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author dsun1
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ConversionFailedException extends RuntimeException {

    public ConversionFailedException(String message) {
        super(message);
    }
}
