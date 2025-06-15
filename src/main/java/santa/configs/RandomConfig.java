package santa.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import santa.SantaConfig;

import java.nio.ByteBuffer;
import java.security.SecureRandom;

@Configuration
@RequiredArgsConstructor
public class RandomConfig {
    private final SantaConfig santaConfig;

    @Bean
    public SecureRandom random() {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(santaConfig.randomSeed());
        byte[] seedBytes = buffer.array();
        return new SecureRandom(seedBytes);
    }
}
