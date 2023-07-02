package at.fhv.matchpoint.partnerservice.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        ObjectNode node = parser.readValueAsTree();
        Instant instant = Instant.ofEpochMilli(node.get("$date").asLong());
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC).toLocalTime();
    }
}
