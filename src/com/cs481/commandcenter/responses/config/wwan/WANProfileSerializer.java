package com.cs481.commandcenter.responses.config.wwan;

import java.io.IOException;

import com.cs481.commandcenter.Utility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
@JsonIgnoreProperties(ignoreUnknown = true)
public class WANProfileSerializer extends JsonSerializer<WANProfile> {
	/*
	 * public WANProfileSerializer() { super(); }
	 */

	@Override
	public void serialize(WANProfile profile, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonGenerationException {
		// We have to do some custom work here to prevent having to do a ton of
		// work other places.

		/*
		 * { ssid: "CS481-WhiteMoon", 
		 * bssid: null, 
		 * uid: "CS481-WhiteMoon",
		 * wpacipher: "tkipaes", 
		 * enabled: true, wpapsk:
		 * "$1$cce5cbc9$Gzb/GIEAwue4XQ40D6MeXg==", 
		 * authmode: "wpa1wpa2psk" },
		 */

		jgen.writeStartObject(); // writes the outer tags.
		jgen.writeStringField("ssid", profile.getSsid());
		jgen.writeStringField("bssid", profile.getBssid()); //this can be null
		jgen.writeStringField("uid", profile.getSsid());
		jgen.writeBooleanField("enabled", profile.getEnabled());
		jgen.writeStringField("authmode", profile.getAuthmode());
		
		//have to switch on the many types of authmodes.
		String authMode = profile.getAuthmode();
		
		
		if (authMode.equals(Utility.AUTH_WEPAUTO)){
			jgen.writeStringField("wepkey0", profile.getSerializePassword());
		} else if (authMode.equals(Utility.AUTH_WPA2) || authMode.equals(Utility.AUTH_WPA1WPA2)){
			jgen.writeStringField("wpacipher", profile.getWpacipher());
			jgen.writeStringField("wpapsk", profile.getSerializePassword());
		}
		
		jgen.writeEndObject();

	}
}
