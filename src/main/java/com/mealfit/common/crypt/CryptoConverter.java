package com.mealfit.common.crypt;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    private final AES128 aes128;

    public CryptoConverter(AES128 aes128) {
        this.aes128 = aes128;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        return aes128.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return aes128.decrypt(dbData);
    }
}
