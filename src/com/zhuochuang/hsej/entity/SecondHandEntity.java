package com.zhuochuang.hsej.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/11.
 */
public class SecondHandEntity {
	/**
	 * totalEnd : 0 totalBegin : 1 result : 1 pageNo : null pageSize : null type
	 * : null items : [{"createDate":1449650357000,"id":1,"status":"1","name":
	 * "二手台式，毕业转让，千真万确"
	 * ,"content":"台式电脑，已经入手两年，5000入手的，如今准备毕业，699大甩卖","price":699
	 * ,"tradingType":1
	 * ,"linkMan":"小明","phone":"18779898668","images":[{"id":621,"path":
	 * "http://hseschool.app360.cn:80/uploads/2015/12/5d67b966-c8fe-4ff8-8f60-83dc8eea9c65.jpg"
	 * }]}]
	 */

	private int totalEnd;
	private int totalBegin;
	private String result;
	private int pageNo;
	private int pageSize;
	private int type;
	/**
	 * createDate : 1449650357000 id : 1 status : 1 name : 二手台式，毕业转让，千真万确
	 * content : 台式电脑，已经入手两年，5000入手的，如今准备毕业，699大甩卖 price : 699 tradingType : 1
	 * linkMan : 小明 phone : 18779898668 images : [{"id":621,"path":
	 * "http://hseschool.app360.cn:80/uploads/2015/12/5d67b966-c8fe-4ff8-8f60-83dc8eea9c65.jpg"
	 * }]
	 */

	private List<ItemsEntity> items;

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

	public void setType(int type) {
		this.type = type;
	}

	public void setItems(List<ItemsEntity> items) {
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

	public int getType() {
		return type;
	}

	public List<ItemsEntity> getItems() {
		return items;
	}

	public static class ItemsEntity implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private long createDate;
		private int id;
		private String status;
		private String name;
		private String content;
		private double price;
		private int tradingType;
		private String linkMan;
		private String phone;
		private boolean isSelect = false;
		private boolean isEdit = false;
		/**
		 * id : 621 path :
		 * http://hseschool.app360.cn:80/uploads/2015/12/5d67b966
		 * -c8fe-4ff8-8f60-83dc8eea9c65.jpg
		 */

		private List<ImagesEntity> images;

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

		public void setContent(String content) {
			this.content = content;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public void setTradingType(int tradingType) {
			this.tradingType = tradingType;
		}

		public void setLinkMan(String linkMan) {
			this.linkMan = linkMan;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public void setImages(List<ImagesEntity> images) {
			this.images = images;
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

		public String getContent() {
			return content;
		}

		public double getPrice() {
			return price;
		}

		public int getTradingType() {
			return tradingType;
		}

		public String getLinkMan() {
			return linkMan;
		}

		public String getPhone() {
			return phone;
		}

		public List<ImagesEntity> getImages() {
			return images;
		}

		public boolean isSelect() {
			return isSelect;
		}

		public void setSelect(boolean isSelect) {
			this.isSelect = isSelect;
		}

		public boolean isEdit() {
			return isEdit;
		}

		public void setEdit(boolean isEdit) {
			this.isEdit = isEdit;
		}

		public static class ImagesEntity implements Serializable{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private int id;
			private String path;

			public void setId(int id) {
				this.id = id;
			}

			public void setPath(String path) {
				this.path = path;
			}

			public int getId() {
				return id;
			}

			public String getPath() {
				return path;
			}
		}
	}
}
