package org.jordan.app.connect.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhaord
 * @Description:
 * @date 2018/9/18下午9:37
 */
public class Pager<T> implements Serializable ,Iterable<T> {
    public static final int PAGE_SIZE = 50;
    protected List<T> result;

    protected int pageSize=50;

    protected int pageNumber=1;
    @Setter
    @Getter
    protected String cursor;

    @Getter
    @Setter
    protected Long ttl;
    /**
     * 总记录数
     */
    protected long totalCount = 0;

    protected boolean isPagination = true;

    private int pageNum;//总页数

    private int startIndex;

    private int limit;

    public int getPageNum() {
        int  totalPageNum = (int) ((totalCount % pageSize == 0) ? totalCount / pageSize : totalCount / pageSize + 1);
        return totalPageNum;
    }

    public Pager() {

    }

    public Pager(int pageNumber, int pageSize, long totalCount) {
        this(pageNumber, pageSize, totalCount, new ArrayList(0));
    }

    public Pager(int pageNumber, int pageSize, long totalCount, List<T> result) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("[pageSize] must great than zero");
        }
        this.pageSize = pageSize;
        this.pageNumber = PageUtils.computePageNumber(pageNumber, pageSize, totalCount);
        this.totalCount = totalCount;
        setResult(result);
    }

    public void setResult(List<T> elements) {
        if (elements == null) {
            throw new IllegalArgumentException("'result' must be not null");
        }
        this.result = elements;
    }

    /**
     * 当前页包含的数据
     *
     * @return 当前页数据源
     */
    public List<T> getResult() {
        return result;
    }

    /**
     * 是否是首页（第一页），第一页页码为1
     *
     * @return 首页标识
     */
    public boolean isFirstPage() {
        return getThisPageNumber() == 1;
    }

    /**
     * 是否是最后一页
     *
     * @return 末页标识
     */
    public boolean isLastPage() {
        return getThisPageNumber() >= getLastPageNumber();
    }

    /**
     * 是否有下一页
     *
     * @return 下一页标识
     */
    public boolean isHasNextPage() {
        return getLastPageNumber() > getThisPageNumber();
    }

    /**
     * 是否有上一页
     *
     * @return 上一页标识
     */
    public boolean isHasPreviousPage() {
        return getThisPageNumber() > 1;
    }

    /**
     * 获取最后一页页码，也就是总页数
     *
     * @return 最后一页页码
     */
    public int getLastPageNumber() {
        return PageUtils.computeLastPageNumber(totalCount, pageSize);
    }

    /**
     * 总的数据条目数量，0表示没有数据
     *
     * @return 总数量
     */
    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 获取当前页的首条数据的行编码
     *
     * @return 当前页的首条数据的行编码
     */
    public int getThisPageFirstElementNumber() {
        return (getThisPageNumber() - 1) * getPageSize() + 1;
    }

    /**
     * 获取当前页的末条数据的行编码
     *
     * @return 当前页的末条数据的行编码
     */
    public long getThisPageLastElementNumber() {
        int fullPage = getThisPageFirstElementNumber() + getPageSize() - 1;
        return getTotalCount() < fullPage ? getTotalCount() : fullPage;
    }

    /**
     * 获取下一页编码
     *
     * @return 下一页编码
     */
    public int getNextPageNumber() {
        return getThisPageNumber() + 1;
    }

    /**
     * 获取上一页编码
     *
     * @return 上一页编码
     */
    public int getPreviousPageNumber() {
        return getThisPageNumber() - 1;
    }

    /**
     * 每一页显示的条目数
     *
     * @return 每一页显示的条目数
     */
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 当前页的页码
     *
     * @return 当前页的页码
     */
    public int getThisPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * 得到用于多页跳转的页码
     *
     * @return
     */
    public List<Integer> getLinkPageNumbers() {
        return PageUtils.generateLinkPageNumbers(getThisPageNumber(), getLastPageNumber(), 10);
    }

    /**
     * 得到数据库的第一条记录号
     *
     * @return
     */
    public int getFirstResult() {
        return PageUtils.getFirstResult(pageNumber, pageSize);
    }
    @Override
    public Iterator<T> iterator() {
        return (Iterator<T>) (result == null ? Collections.emptyList().iterator() : result.iterator());
    }

    public boolean isPagination() {
        return isPagination;
    }

    public void setPagination(boolean isPagination) {
        this.isPagination = isPagination;
    }

}
