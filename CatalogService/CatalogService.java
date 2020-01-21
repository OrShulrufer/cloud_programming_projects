package CatalogService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface CatalogService {
	
	//1
	public Category createCategory (Category catagory);
	
	//2
	public Product createProduct (Product product);
	
	public List<Category> getAllCategory(int page, int size, String sortAttr);
	public Optional<Product> getProductById(String id);
	
	//5 + 6 + 7 + 8 + 9 
	public Collection<Product> getAllProductsSortByAttr(String sortBy, int size, int page);
	public Collection<Product> getAllProductsByName(String filterValue, String sortBy, int size, int page);
	public Collection<Product> getAllProductsByMinPrice(String filterValue, String sortBy, int size, int page);
	public Collection<Product> getAllProductsByMaxPrice(String filterValue, String sortBy, int size, int page);
	public Collection<Product> getAllProductsByCategoryName(String filterValue, String sortBy, int size, int page);

	//10
	public void deleteCatalog();
}
