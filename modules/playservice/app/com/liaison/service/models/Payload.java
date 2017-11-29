package com.liaison.service.models;

/**
 * Payload model POJO class
 *
 */
public class Payload {

	private String payloadData;
    private int payloadId;

    public int getPayloadId() {
		return payloadId;
	}
	public void setPayloadId(int payloadId) {
		this.payloadId = payloadId;
	}
	public String getPayloadData() {
        return payloadData;
    }
    public void setPayloadData(String payloadData) {
        this.payloadData = payloadData;
    }
}
