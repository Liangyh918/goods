package cn.itcast.goods.pager;

import java.util.List;

public class PageBean<T> {
	private int pc;// ��ǰҳ��
	private int tr;// �ܼ�¼��
	private int ps;// ÿҳ��¼��
	private String url;// ����·���Ͳ��������磬/BookServlet?method=findxxxx&bname=2
	private List<T> beanList;//

	
	//������ҳ��
	public int getTp(){
		int tp = tr/ps;
		if(tr%ps!=0){
			tp++;
		}
		return tp;
	}
	
	
	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public int getTr() {
		return tr;
	}

	public void setTr(int tr) {
		this.tr = tr;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		this.ps = ps;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<T> getBeanList() {
		return beanList;
	}

	public void setBeanList(List<T> beanList) {
		this.beanList = beanList;
	}
}
