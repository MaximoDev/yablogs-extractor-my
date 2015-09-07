package xdsoft;

import java.util.ArrayList;
import java.util.List;

// тупо pojo
public class Categories {
	private String namecategory;
	private List<Requests> requestslist =new ArrayList<Requests>();
	
	public String getNamecategory() {
		return namecategory;
	}
	public void setNamecategory(String namecategory) {
		this.namecategory = namecategory;
	}
	public List<Requests> getRequestslist() {
		return requestslist;
	}
	public void setRequestslist(List<Requests> requestslist) {
		this.requestslist = requestslist;
	}
	
	public void setRequestslistel(Requests requestslistel) {
		this.requestslist.add(requestslistel);
	}
	


}
