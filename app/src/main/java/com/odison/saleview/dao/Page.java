package com.odison.saleview.dao;

public interface Page {

	/**
	 * 获取当前页码
	 * 
	 * @return the curpage
	 */
	public int getCurpage();

	/**
	 * 设置当前页码（1开始）
	 * 
	 * @param curpage
	 *            当前页码
	 */
	public void setCurpage(int curpage);

	/**
	 * 获取每页记录数
	 * 
	 * @return the pagesize
	 */
	public int getPagesize();

	/**
	 * 设置每页记录数
	 * 
	 * @param pagesize
	 *            每页记录数
	 */
	public void setPagesize(int pagesize);

	/**
	 * 获取总记录数
	 * 
	 * @return 总记录数
	 */
	public int getTotal();

	/**
	 * 设置总记录数
	 * 
	 * @param total
	 *            总记录数
	 */
	public void setTotal(int total);

	/**
	 * 是否为第一页
	 * 
	 * @return true-是第一页
	 */
	public boolean isFirst();

	/**
	 * 是否为最后一页
	 * 
	 * @return true-是最后一页
	 */
	public boolean isLast();

	/**
	 * 获取总页数
	 * 
	 * @return 总页数
	 */
	public int getTotalPage();

	/**
	 * 获取当前页起始记录号
	 * 
	 * @return 当前页起始记录号
	 */
	public int getStartIndex();

	/**
	 * 获取Page的克隆对象，此对象不能进行setTotal操作，只用于保存
	 * 
	 * @return
	 */
	public Page clonePage();
}
