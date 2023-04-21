package sample;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class RequestDeserializationSchema implements DeserializationSchema<Request> {
    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public Request deserialize(byte[] message) throws IOException {
        return objectMapper.readValue(message, Request.class);
    }

    @Override
    public boolean isEndOfStream(Request request) {
        return false;
    }

    @Override
    public TypeInformation<Request> getProducedType() {
        return TypeInformation.of(Request.class);
    }
}
