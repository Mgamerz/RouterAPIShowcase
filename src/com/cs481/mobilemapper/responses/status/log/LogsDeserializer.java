package com.cs481.mobilemapper.responses.status.log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

import com.cs481.mobilemapper.activities.CommandCenterActivity;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson deserializer for our unkeyed log object.
 */
public final class LogsDeserializer extends JsonDeserializer<Logs> {
	@Override
	public Logs deserialize(final JsonParser jp,
			final DeserializationContext ctxt) throws IOException {

		final JsonNode node = jp.readValueAs(JsonNode.class);
		if (!node.isContainerNode() || !node.isArray())
			throw new JsonMappingException("expected an array");
		Logs logs = new Logs();

		
		Log.i(CommandCenterActivity.TAG, "Starting LOGS deserialization.");

		// Build the log object manually...
		Iterator<JsonNode> iterator = node.elements();
		ArrayList<LogMessage> messages = new ArrayList<LogMessage>();
		ObjectMapper mapper = new ObjectMapper();
		while (iterator.hasNext()){
			JsonNode logNode = iterator.next();
			LogMessage message = mapper.readValue(logNode.toString(), LogMessage.class);
			Log.i(CommandCenterActivity.TAG, message.toString());
		}
		//timestamp
		
		//ArrayList<JsonNode> logJson = iterator.next();
		//new ArrayNode();
		
		logs.setLogs(messages);
		
		
		return logs;
	}
}