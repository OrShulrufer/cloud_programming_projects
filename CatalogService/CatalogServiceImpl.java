package CatalogService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.HTMLDocument.HTMLReader.ParagraphAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatalogServiceImpl implements CatalogService {
	
	private CategoryCrud catagoryCrud;
	private ProductCrud productCrud;

	@Autowired
	public CatalogServiceImpl(CategoryCrud catagory, ProductCrud product) {
		super();
		this.catagoryCrud = catagory;
		this.productCrud = product;
	}

	// 1
	@Override
	@Transactional
	public Category createCategory(Category category) {
		String name = category.getCategoryName();
		if (this.catagoryCrud.existsById(name)) {
			throw new RuntimeException("Category already exists with name: " + name);
		}
		return this.catagoryCrud.save(category);
	}

	// 2
	@Override
	@Transactional
	public Product createProduct(Product product) {
		if (!this.catagoryCrud.existsById(product.getCategory().getCategoryName())) {
			throw new RuntimeException("Category not exists");
		} 
		else {
			catagoryCrud.findById(product.getCategory().getCategoryName());
			product.setCategory(catagoryCrud.findById(product.getCategory().getCategoryName()).get());
			return this.productCrud.save(product);
		}
	}

	//3
	@Override
	@Transactional(readOnly = true)
	public List<Category> getAllCategory(int page, int size, String sortAttr) {
		return this.catagoryCrud
				.findAll(PageRequest.of(page, size, Direction.DESC, sortAttr))
				.getContent();
	}

	//4
	@Override
	@Transactional(readOnly = true)
	public Optional<Product> getProductById(String id) {
		return this.productCrud.findById(id);
	}
	
	// 5 + 6 + 7 + 8 + 9
	@Override
	@Transactional(readOnly = true)
	public Collection<Product> getAllProductsSortByAttr(String sortBy, int size, int page) {
		return this.productCrud
				.findAll(PageRequest.of(page, size, Direction.DESC, sortBy))
				.getContent();
	}
	
	//6
	@Override
	@Transactional(readOnly = true)
	public Collection<Product> getAllProductsByName(String filterValue, String sortBy, int size, int page) {
		return this.productCrud
				.findAllByproductName(filterValue, PageRequest.of(page, size, Direction.DESC, sortBy));
	}

	//7
	@Override
	public Collection<Product> getAllProductsByMinPrice(String filterValue, String sortBy, int size, int page) {
		long minPrice = Long.parseLong(filterValue);
		return this.productCrud
				.findAllByPriceGreaterThan(minPrice, PageRequest.of(page, size, Direction.DESC, sortBy));
	}

	//8
	@Override
	public Collection<Product> getAllProductsByMaxPrice(String filterValue, String sortBy, int size, int page) {
		long maxPrice = Long.parseLong(filterValue);
		return this.productCrud
				.findAllByPriceLessThan(maxPrice, PageRequest.of(page, size, Direction.DESC, sortBy));
	}

	//9
	@Override
	public Collection<Product> getAllProductsByCategoryName(String filterValue, String sortBy, int size, int page) {
		return this.productCrud
				.findAllByCategory_categoryName(filterValue, PageRequest.of(page, size, Direction.DESC, sortBy));
	}

	//10
	@Override
	public void deleteCatalog() {
		this.productCrud.deleteAll();
		this.catagoryCrud.deleteAll();
	}
	
}
