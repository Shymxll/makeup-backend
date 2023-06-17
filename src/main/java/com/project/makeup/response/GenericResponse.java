package com.project.makeup.response;

import java.util.ArrayList;

import org.hibernate.mapping.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse {
    private String code;
    private String message;
    private Object data;

}
