package com.cs481.commandcenter.responses.status.log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Jackson deserializer for our unkeyed log object.
 */
public final class LogsDeserializer extends JsonDeserializer<Logs> {
	public final static int MAX_LOGS = 150;
	@Override
	public Logs deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException {
											// under the 1MB parcel limit.
		JsonNode node = jp.readValueAs(JsonNode.class);
		if (!node.isArray()) {
			//might need to go down a level
			node = node.get("logs"); //go down to the 'logs' array
		}
		
		if (!node.isArray()) {
			Log.e(CommandCenterActivity.TAG, "Deserialization of Logs Error. Not an array or container node.");
			Log.e(CommandCenterActivity.TAG, node.toString());
			throw new JsonMappingException("Array expected in logs deserializer, could not find one at the top level/logs level.");
		}
			
		Logs logs = new Logs();

		Log.i(CommandCenterActivity.TAG, "Starting LOGS deserialization.");

		// Build the log object manually...
		Iterator<JsonNode> iterator = node.elements();
		ArrayList<LogMessage> messages = new ArrayList<LogMessage>();
		ObjectMapper mapper = new ObjectMapper();
		int numMessages = 0;
		while (iterator.hasNext() && numMessages < MAX_LOGS) {
			//Log.w(CommandCenterActivity.TAG, "Adding log message "+numMessages);
			JsonNode logNode = iterator.next();
			LogMessage message = mapper.readValue(logNode.toString(),
					LogMessage.class);
			messages.add(message);
			numMessages++;
		}

		logs.setLogs(messages);

		return logs;
	}
}