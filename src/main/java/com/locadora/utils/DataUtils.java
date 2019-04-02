package com.locadora.utils;

import java.sql.Date;

public class DataUtils {
  
  public static Date gerarDataAtual() {
    return new Date(System.currentTimeMillis());
  }

}
