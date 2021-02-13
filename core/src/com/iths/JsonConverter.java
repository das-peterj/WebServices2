package com.iths;

import com.google.gson.*;

import java.sql.ResultSet;

public class JsonConverter {


    // la till final
     private final Gson gson;

          public JsonConverter(ResultSet json) {
          gson = new Gson();
       }

            public String convertToJson(Object object){
            return gson.toJson(object);

       }
    }

