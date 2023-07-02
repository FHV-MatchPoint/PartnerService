package at.fhv.matchpoint.partnerservice.utils;

import java.io.IOException;
import org.bson.types.ObjectId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {

    @Override
    public ObjectId deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ObjectCodec codec = jp.getCodec();
        JsonNode node = codec.readTree(jp);
        String objectIdString = node.get("$oid").asText();
        return new ObjectId(objectIdString);
    }
}