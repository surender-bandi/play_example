package com.liaison.service.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class used for adding and retrieving the payload models.
 *
 */
public class PayloadStore {

	private static PayloadStore payload = new PayloadStore();
	
	public static PayloadStore getInstance( ) {
	      return payload;
	}

	public static void reset() {
		payload = new PayloadStore();
	}

	private Map<Integer, Payload> payloads = new HashMap<>();

    public Payload addPayload(Payload payload) {
		int id = (int) Math.random();
		payloads.put(id, payload);
		return payload;
    }

    public List<Payload> getPayloadData() {
        return new ArrayList<>(payloads.values());
    }
}
