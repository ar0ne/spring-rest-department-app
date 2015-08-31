package com.ar0ne.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;


public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    private static DateTimeFormatter formatter =  DateTimeFormat.forPattern("yyyy-MM-dd");

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return formatter.parseLocalDate(parser.getText());
    }
}