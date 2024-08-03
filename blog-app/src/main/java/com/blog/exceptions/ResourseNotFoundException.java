package com.blog.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ResourseNotFoundException extends RuntimeException{
    private String resourceName;
    private String fieldName;
    private long fieldValue;

    public ResourseNotFoundException(String resourceName,String fieldName,long fieldValue){
        super(String.format("%s not found with %s : %s",resourceName,fieldName,fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.resourceName = resourceName;
    }
}
