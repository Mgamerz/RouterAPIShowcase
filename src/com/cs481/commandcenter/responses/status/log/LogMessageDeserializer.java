package com.cs481.commandcenter.responses.status.log;

import java.io.IOException;
import java.util.Iterator;

import android.util.Log;

import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Jackson deserializer for our unkeyed log object.
 */
public final class LogMessageDeserializer extends JsonDeserializer<LogMessage> {
	@Override
	public LogMessage deserialize(final JsonParser jp,
			final DeserializationContext ctxt) throws IOException {

		final JsonNode node = jp.readValueAs(JsonNode.class);
		if (!node.isContainerNode() && !node.isArray()) {
			Log.e(CommandCenterActivity.TAG, node.toString());
			throw new JsonMappingException("LogMessageDeserializer: expected an object");
		}
		//Log.i(CommandCenterActivity.TAG, "Log message parsing: "+node.toString());
		LogMessage logMessage = new LogMessage();

		// Build the log object manually...
		Iterator<JsonNode> iterator = node.elements();
		
		//timestamp
		logMessage.setTimeStamp(iterator.next().asDouble());
		logMessage.setSeverity(removeQuotes(iterator.next().toString()));
		logMessage.setTag(removeQuotes(iterator.next().toString()));
		logMessage.setMessage(removeQuotes(iterator.next().toString()));
		JsonNode trace = iterator.next();
		if (trace != null){
			logMessage.setTrace(trace.toString());
		} else {
			logMessage.setTrace(null);
		}
		
		
		return logMessage;
	}
	
	private String removeQuotes(String str){
		return str.replaceAll("(^\")|(\"$)","");
	}
}