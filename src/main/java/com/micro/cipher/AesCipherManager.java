package com.micro.cipher;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class AesCipherManager {

    @SneakyThrows
    public String decrypt(String nameFile) {
        String key = Files.readString(Path.of("/var/lib/karen/data/key.txt"));
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher decrypter = Cipher.getInstance("AES");
        decrypter.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] encryptedBytes = Files.readAllBytes(Path.of("/var/lib/karen/data/"+nameFile));

        return new String(decrypter.doFinal(encryptedBytes));
    }

}
