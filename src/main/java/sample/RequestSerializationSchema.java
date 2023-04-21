package sample;

import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

public class RequestSerializationSchema implements SerializationSchema<RequestWithCount> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(RequestWithCount request) {
        try {
            return objectMapper.writeValueAsBytes(request);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not serialize record: " + request, e);
        }
    }
}
