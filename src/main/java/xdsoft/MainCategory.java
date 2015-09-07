package xdsoft;

import java.util.ArrayList;
import java.util.List;

// тупо pojo
public class MainCategory {
	private List<Categories> catgorylist =new ArrayList<Categories>();

	public List<Categories> getCatgorylist() {
		return catgorylist;
	}

	public void setCatgorylist(List<Categories> catgorylist) {
		this.catgorylist = catgorylist;
	}

	public void setCatgorylistel(Categories catgorylist) {
		this.catgorylist.add(catgorylist);
	}

}
