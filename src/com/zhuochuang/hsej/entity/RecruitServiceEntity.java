package com.zhuochuang.hsej.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/12/15.
 */
public class RecruitServiceEntity {

	private int totalEnd;
	private int totalBegin;
	private String result;
	private int pageNo;
	private int pageSize;
	private List<RecruitItemsEntity> items;

	public void setTotalEnd(int totalEnd) {
		this.totalEnd = totalEnd;
	}

	public void setTotalBegin(int totalBegin) {
		this.totalBegin = totalBegin;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setItems(List<RecruitItemsEntity> items) {
		this.items = items;
	}

	public int getTotalEnd() {
		return totalEnd;
	}

	public int getTotalBegin() {
		return totalBegin;
	}

	public String getResult() {
		return result;
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public List<RecruitItemsEntity> getItems() {
		return items;
	}

	public static class RecruitItemsEntity {
		private long createDate;
		private int id;
		private String status;
		private String name;
		private int jobNature;
		private int peopleNum;
		private String jobRequirements;
		private String jobContent;
		private String linkMan;
		private String phone;
		private String jobTime;
		private String jobAddress;
		private String salary;
		private String resourceType;
		private int resourceId;
		private String userName;
		private boolean isSelect = false;

		public void setCreateDate(long createDate) {
			this.createDate = createDate;
		}

		public void setId(int id) {
			this.id = id;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setJobNature(int jobNature) {
			this.jobNature = jobNature;
		}

		public void setPeopleNum(int peopleNum) {
			this.peopleNum = peopleNum;
		}

		public void setJobRequirements(String jobRequirements) {
			this.jobRequirements = jobRequirements;
		}

		public void setJobContent(String jobContent) {
			this.jobContent = jobContent;
		}

		public void setLinkMan(String linkMan) {
			this.linkMan = linkMan;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public void setJobTime(String jobTime) {
			this.jobTime = jobTime;
		}

		public void setJobAddress(String jobAddress) {
			this.jobAddress = jobAddress;
		}

		public void setSalary(String salary) {
			this.salary = salary;
		}

		public void setResourceType(String resourceType) {
			this.resourceType = resourceType;
		}

		public void setResourceId(int resourceId) {
			this.resourceId = resourceId;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public long getCreateDate() {
			return createDate;
		}

		public int getId() {
			return id;
		}

		public String getStatus() {
			return status;
		}

		public String getName() {
			return name;
		}

		public int getJobNature() {
			return jobNature;
		}

		public int getPeopleNum() {
			return peopleNum;
		}

		public String getJobRequirements() {
			return jobRequirements;
		}

		public String getJobContent() {
			return jobContent;
		}

		public String getLinkMan() {
			return linkMan;
		}

		public String getPhone() {
			return phone;
		}

		public String getJobTime() {
			return jobTime;
		}

		public String getJobAddress() {
			return jobAddress;
		}

		public String getSalary() {
			return salary;
		}

		public String getResourceType() {
			return resourceType;
		}

		public int getResourceId() {
			return resourceId;
		}

		public String getUserName() {
			return userName;
		}

		public boolean isSelect() {
			return isSelect;
		}

		public void setSelect(boolean isSelect) {
			this.isSelect = isSelect;
		}
	}
}
