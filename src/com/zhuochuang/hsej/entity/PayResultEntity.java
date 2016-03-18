package com.zhuochuang.hsej.entity;

/*
 * 支付结果的实体对象
 */
public class PayResultEntity {

	private int result;
	private String dealNumber;

	private PayEntity items;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getDealNumber() {
		return dealNumber;
	}

	public void setDealNumber(String dealNumber) {
		this.dealNumber = dealNumber;
	}

	public PayEntity getItems() {
		return items;
	}

	public void setItems(PayEntity items) {
		this.items = items;
	}

	public static class PayEntity {
		private String request_xml;
		private String sign;
		private String payUrl;
		private String pay_type;

		public String getRequest_xml() {
			return request_xml;
		}

		public void setRequest_xml(String request_xml) {
			this.request_xml = request_xml;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

		public String getPayUrl() {
			return payUrl;
		}

		public void setPayUrl(String payUrl) {
			this.payUrl = payUrl;
		}

		public String getPay_type() {
			return pay_type;
		}

		public void setPay_type(String pay_type) {
			this.pay_type = pay_type;
		}

	}
}
