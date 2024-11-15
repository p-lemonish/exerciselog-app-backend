package s24.backend.exerciselog.util;

import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Component
@Converter
public class AttributeEncryptor implements AttributeConverter<String, String> {
    
    @Autowired
    private EncryptionService encryptionService;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            return encryptionService.encrypt(attribute);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Error encrypting attribute", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            return encryptionService.decrypt(dbData);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Error decrypting attribute", e);
        }
    }
}
