package data;

import java.lang.reflect.Type;
import java.math.BigInteger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class BigIntegerAdapater implements JsonSerializer<BigInteger> {

	@Override
	public JsonElement serialize(BigInteger src, Type typeOfSrc, JsonSerializationContext context) {
		// TODO Auto-generated method stub
		 return new JsonPrimitive(src.toString());
	}

}
