package com.zhuochuang.hsej.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/15.
 */
public class CommonServiceEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String content;
	private boolean isfree;
	private double cost;
	private boolean applyStatus;
	private boolean payment;
	private List<CommItemsEntity> items;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isIsfree() {
		return isfree;
	}

	public void setIsfree(boolean isfree) {
		this.isfree = isfree;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public boolean isApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(boolean applyStatus) {
		this.applyStatus = applyStatus;
	}

	public boolean isPayment() {
		return payment;
	}

	public void setPayment(boolean payment) {
		this.payment = payment;
	}

	public List<CommItemsEntity> getItems() {
		return items;
	}

	public void setItems(List<CommItemsEntity> items) {
		this.items = items;
	}

	public static class CommItemsEntity implements Serializable,
			Comparable<CommItemsEntity> {
		private static final long serialVersionUID = 1L;
		private int id;
		private String name;
		private int row;
		private int type;
		private int sequence;
		private boolean isRequired;
		private String result;
		private List<ItemEntity> items;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getSequence() {
			return sequence;
		}

		public void setSequence(int sequence) {
			this.sequence = sequence;
		}

		public boolean isRequired() {
			return isRequired;
		}

		public void setRequired(boolean isRequired) {
			this.isRequired = isRequired;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public List<ItemEntity> getItems() {
			return items;
		}

		public void setItems(List<ItemEntity> items) {
			this.items = items;
		}

		public static class ItemEntity implements Serializable {
			private static final long serialVersionUID = 1L;
			private int id;
			private String name;
			private boolean checked;

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public boolean isChecked() {
				return checked;
			}

			public void setChecked(boolean checked) {
				this.checked = checked;
			}

		}

		@Override
		public int compareTo(CommItemsEntity another) {

			return getSequence() - another.getSequence();
		}

	}
}
