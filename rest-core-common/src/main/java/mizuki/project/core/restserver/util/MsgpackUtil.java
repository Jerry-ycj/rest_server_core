package mizuki.project.core.restserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.util.Map;

public class MsgpackUtil {

    private static ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());

    public byte[] pack(Map<String,Object> map) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(map);
    }

    public Map unpack(byte[] bytes) throws IOException {
        return objectMapper.readValue(bytes,Map.class);
    }

}
