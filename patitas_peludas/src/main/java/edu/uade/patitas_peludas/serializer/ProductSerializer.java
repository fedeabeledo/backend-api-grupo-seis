package edu.uade.patitas_peludas.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import edu.uade.patitas_peludas.entity.Product;

import java.io.IOException;

public class ProductSerializer extends JsonSerializer<Product> {
    @Override
    public void serialize(Product product, JsonGenerator jsonGen, SerializerProvider serializers) throws IOException {
        jsonGen.writeStartObject();
        jsonGen.writeNumberField("id", product.getId());
        jsonGen.writeStringField("title", product.getTitle());
        jsonGen.writeStringField("description", product.getDescription());
        jsonGen.writeStringField("image_url", product.getImageUrl());
        jsonGen.writeStringField("brand", product.getBrand());
        jsonGen.writeStringField("pet_category", product.getPetCategory().toString());
        jsonGen.writeStringField("pet_stage", product.getPetStage().toString());
        jsonGen.writeNumberField("score", product.getScore());
        jsonGen.writeNumberField("score_voters", product.getScoreVoters());
        jsonGen.writeNumberField("price", product.getPrice());
        jsonGen.writeNumberField("discount", product.getDiscount());
        jsonGen.writeNumberField("stock", product.getStock());
        jsonGen.writeBooleanField("bestseller", product.getBestseller());
        jsonGen.writeNumberField("user_id", product.getUser().getId());
        jsonGen.writeEndObject();
    }
}
