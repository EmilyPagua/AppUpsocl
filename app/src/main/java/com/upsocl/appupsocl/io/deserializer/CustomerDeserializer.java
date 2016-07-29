package com.upsocl.appupsocl.io.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.upsocl.appupsocl.domain.Customer;
import com.upsocl.appupsocl.domain.News;
import com.upsocl.appupsocl.io.model.JsonKeys;
import com.upsocl.appupsocl.keys.CustomerKeys;

import java.lang.reflect.Type;

/**
 * Created by upsocl on 25-07-16.
 */
public class CustomerDeserializer implements JsonDeserializer<Customer> {

    @Override
    public Customer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject customer = json.getAsJsonObject();

        return extractCustomerFromJsonArray(customer);
    }

    private Customer extractCustomerFromJsonArray(JsonObject item) {

        Customer currentCutomer = new Customer();
        currentCutomer.setId(item.get(JsonKeys.NEWS_ID).getAsString());
        currentCutomer.setFirstName(item.get(CustomerKeys.DATA_USER_FIRST_NAME).getAsString());
        currentCutomer.setLastName(item.get(CustomerKeys.DATA_USER_LAST_NAME).getAsString());
        currentCutomer.setEmail(item.get(CustomerKeys.DATA_USER_EMAIL).getAsString());
        currentCutomer.setLocation(item.get(CustomerKeys.DATA_USER_LOCATION).getAsString());

        return currentCutomer;
    }
}
