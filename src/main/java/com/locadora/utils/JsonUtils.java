package com.locadora.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {

  public static String objetoParaJson(Object objetoSalvo) {

    final ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    String objeto = null;

    try {
    objeto = mapper.writeValueAsString(objetoSalvo);
    } catch (JsonProcessingException e) {
    e.printStackTrace();
    }
    return objeto;
    }
  
}
