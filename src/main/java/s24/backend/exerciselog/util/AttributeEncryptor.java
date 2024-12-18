package s24.backend.exerciselog.util;

import java.security.GeneralSecurityException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import s24.backend.exerciselog.service.EncryptionService;

@Component
@Converter
public class AttributeEncryptor implements AttributeConverter<String, String> {
    
    @Autowired
    private EncryptionService encryptionService;

    private boolean isBase64Encoded(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        byte[] byteArray = str.getBytes();
        return Base64.isArrayByteBase64(byteArray);
    }

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
        if (dbData == null) return null;

        if(!isBase64Encoded(dbData)) {
            return dbData;
        }

        try {
            return encryptionService.decrypt(dbData);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Error decrypting attribute", e);
        }
    }
}
