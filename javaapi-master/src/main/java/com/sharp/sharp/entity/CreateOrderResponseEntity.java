package com.sharp.sharp.entity;

public class CreateOrderResponseEntity {
	private long cf_order_id;
	private String order_id;
	private String entity;
	private String order_currency;
	private double order_amount;
	private String order_status;
	private String payment_session_id;
	private String order_note;
	public long getCf_order_id() {
		return cf_order_id;
	}
	public void setCf_order_id(long cf_order_id) {
		this.cf_order_id = cf_order_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getOrder_currency() {
		return order_currency;
	}
	public void setOrder_currency(String order_currency) {
		this.order_currency = order_currency;
	}
	public double getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(double order_amount) {
		this.order_amount = order_amount;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getPayment_session_id() {
		return payment_session_id;
	}
	public void setPayment_session_id(String payment_session_id) {
		this.payment_session_id = payment_session_id;
	}
	public String getOrder_note() {
		return order_note;
	}
	public void setOrder_note(String order_note) {
		this.order_note = order_note;
	}
	

}
