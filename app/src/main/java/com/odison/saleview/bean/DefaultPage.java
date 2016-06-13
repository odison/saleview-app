package com.odison.saleview.bean;

import com.odison.saleview.dao.Page;

/**
 * 分页信息
 * 
 * @author Blazz
 */
public class DefaultPage implements Page {
	private static final long serialVersionUID = 8526602444853514919L;
	private int curpage = 1; // 当前页码
	private int pagesize = -1; // 每页记录数
	private int total; // 总记录数
	private int totalPage; // 总页数

	/**
	 * 获取当前页码
	 * 
	 * @return the curpage
	 */
	public int getCurpage() {
		return curpage;
	}

	/**
	 * 设置当前页码（1开始）
	 * 
	 * @param curpage
	 *            当前页码
	 */
	public void setCurpage(int curpage) {
		this.curpage = curpage;
		this.total = -1;
	}

	/**
	 * 获取每页记录数
	 * 
	 * @return the pagesize
	 */
	public int getPagesize() {
		return pagesize;
	}

	/**
	 * 设置每页记录数
	 * 
	 * @param pagesize
	 *            每页记录数
	 */
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	/**
	 * 获取总记录数
	 * 
	 * @return 总记录数
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 设置总记录数
	 * 
	 * @param total
	 *            总记录数
	 */
	public void setTotal(int total) {
		this.total = total;
		int pages = total / pagesize;
		boolean hasMore = (0 != total % pagesize);
		if (0 == pages) {
			totalPage = 1;
		} else {
			if (hasMore) {
				totalPage = pages + 1;
			} else {
				totalPage = pages;
			}
		}
		if (curpage > totalPage) {
			curpage = totalPage;
		}
		if (curpage < 1) {
			curpage = 1;
		}
	}

	/**
	 * 是否为第一页
	 * 
	 * @return true-是第一页
	 */
	public boolean isFirst() {
		return (curpage <= 1);
	}

	/**
	 * 是否为最后一页
	 * 
	 * @return true-是最后一页
	 */
	public boolean isLast() {
		return (curpage >= totalPage);
	}

	/**
	 * 获取总页数
	 * 
	 * @return 总页数
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * 获取当前页起始记录号
	 * 
	 * @return 当前页起始记录号
	 */
	public int getStartIndex() {
		return (curpage - 1) * pagesize;
	}

	/**
	 * 获取Page的克隆对象，此对象不能进行setTotal操作，只用于保存
	 * 
	 * @return
	 */
	public Page clonePage() {
		Page page = new DefaultPage();
		
		page.setCurpage(curpage);
		page.setPagesize(pagesize);
		page.setTotal(total);
		
		return page;
	}
}