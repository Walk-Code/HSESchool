package com.zhuochuang.hsej.entity;

import java.io.Serializable;
import java.util.List;

public class CommunityStructEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int result;
	private String commid;
	private ApplyUserEntity applyUser;
	private List<StructEntity> structures;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public List<StructEntity> getStructures() {
		return structures;
	}

	public void setStructures(List<StructEntity> structures) {
		this.structures = structures;
	}

	public String getCommid() {
		return commid;
	}

	public void setCommid(String commid) {
		this.commid = commid;
	}

	public ApplyUserEntity getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(ApplyUserEntity applyUser) {
		this.applyUser = applyUser;
	}

	public static class StructEntity implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int id;
		private String name;

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

	}

	public static class ApplyUserEntity implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String phone;
		private String selfEvaluation;
		private int applyStructure;
		private int first;
		private int second;
		private String hobby;

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getSelfEvaluation() {
			return selfEvaluation;
		}

		public void setSelfEvaluation(String selfEvaluation) {
			this.selfEvaluation = selfEvaluation;
		}


		public int getFirst() {
			return first;
		}

		public void setFirst(int first) {
			this.first = first;
		}

		public int getSecond() {
			return second;
		}

		public void setSecond(int second) {
			this.second = second;
		}

		public String getHobby() {
			return hobby;
		}

		public void setHobby(String hobby) {
			this.hobby = hobby;
		}

		public int getApplyStructure() {
			return applyStructure;
		}

		public void setApplyStructure(int applyStructure) {
			this.applyStructure = applyStructure;
		}

	}
}
