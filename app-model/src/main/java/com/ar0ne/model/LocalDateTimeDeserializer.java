package com.ar0ne.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;


public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private static DateTimeFormatter formatter =  DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return formatter.parseLocalDateTime(parser.getText());
    }
}