package xdsoft;

import java.util.ArrayList;
import java.util.List;

// тупо pojo
public class MainCategory {
	private List<Categories> categoryList =new ArrayList<Categories>();

	public List<Categories> getCatgoryList() {
		return categoryList;
	}

	public void setCatgoryList(List<Categories> categoryList) {
		this.categoryList = categoryList;
	}

	public void setCatgorylistel(Categories categoryList) {
		this.categoryList.add(categoryList);
	}

}
