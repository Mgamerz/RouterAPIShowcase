package com.cs481.commandcenter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import android.util.Log;

import com.cs481.commandcenter.responses.Response;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;

public class ResponsePersister {
	protected static void saveSerializableToCache(Object o, File cacheFile) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(o);
			out.close();

			// Get the bytes of the serialized object
			byte[] buf = bos.toByteArray();
			FileOutputStream fout = new FileOutputStream(cacheFile);
			fout.write(buf);
			fout.close();

		} catch (IOException ioe) {
			Log.e("serializeObject", "error", ioe);
		}
	}

	protected static Response getSerializableFromCache(File file)
			throws CacheLoadingException {
		if (file.exists()) {

			try {
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(file));
				Object object = in.readObject();
				in.close();
				return (Response) object;
			} catch (ClassNotFoundException cnfe) {
				Log.e("deserializeObject", "class not found error", cnfe);
				throw new CacheLoadingException(cnfe);
			} catch (IOException ioe) {
				Log.e("deserializeObject", "io error", ioe);
				throw new CacheLoadingException(ioe);
			}

		}
		return null;
	}
}
