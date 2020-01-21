package CatalogService;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {
	
	private CatalogService catalogService;
	
	@Autowired
	public CatalogController(CatalogService catalogService) {
		super();
		this.catalogService = catalogService;
	}

	// 1
	@RequestMapping(
			path = "/catalog/categories", 
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public CategoryBoundary create(
			@RequestBody CategoryBoundary categoryBoundary) {
		return fromEntity(this.catalogService.createCategory(toEntity(categoryBoundary)));
	}
    //2
	@RequestMapping(
			path = "/catalog/products", 
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ProductBoundary create(@RequestBody ProductBoundary productBoundary) {	
		
		return fromEntity(this.catalogService.createProduct(toEntity(productBoundary)));
	}

	// 3
	@RequestMapping(
			path = "/catalog/categories", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public CategoryBoundary[] getAllCategorys(
			@RequestParam(name = "sortAttr", required = false, defaultValue = "categoryName") String sortAttr,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "20") int size) {
		return this.catalogService
				.getAllCategory(page, size, sortAttr)
				.stream()
				.map(this::fromEntity)
				.collect(Collectors.toList())
				.toArray(new CategoryBoundary[0]);
	}
	
	// 4 
	@RequestMapping(
			path="/catalog/products/{productId}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ProductBoundary getProductById (
			@PathVariable("productId") String productId) {
		return this.fromEntity(
			this.catalogService
				.getProductById(productId)
					.orElseThrow(()->new ProductNotFoundException("could not find product by id: " + productId)));
	}
	
	//5: GET /catalog/products?sortBy={sortAttr}&page={page)&size={size}
	//6: GET /catalog/products?filterType=byName&filterValue={productName}&sortBy={sortAttr}&page={page)&size={size}
	//7: GET /catalog/products?filterType=byMinPrice&filterValue={minPrice}&sortBy={sortAttr}&page={page)&size={size}
	//8: GET /catalog/products?filterType=byMaxPrice&filterValue={maxPrice}&sortBy={sortAttr}&page={page)&size={size}
	//9: GET /catalog/products?filterType=byCategoryName&filterValue={categoryName}&sortBy={sortAttr}&page={page)&size={size}
	@RequestMapping(
			path="/catalog/products", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ProductBoundary[] getAllProductsFilterByValSortByAttr(
			// for 5, 6, 7, 8, 9
			@RequestParam(name="sortBy", required = false, defaultValue = "id") String sortBy,
			@RequestParam(name="page", required = false, defaultValue = "0") int page, 
			@RequestParam(name="size", required = false, defaultValue = "20") int size,
			// for 6, 7, 8, 9
			@RequestParam(name="filterType", required = false, defaultValue = "") String filterType,
			@RequestParam(name="filterValue", required = false, defaultValue = "") String filterValue) {

		Stream<ProductBoundary> col = null;
		
		//5
		if(filterType.equals("")) {
			col = this.catalogService
					.getAllProductsSortByAttr(sortBy, size, page)
					.stream()
					.map(this::fromEntity);
		}
		
		//6
		else if(filterType.equals("byName")) {
			col = this.catalogService
					.getAllProductsByName(filterValue, sortBy, size, page)
					.stream()
					.map(this::fromEntity);
		}
		
		//7
		else if(filterType.equals("byMinPrice")) {
			col = this.catalogService
					.getAllProductsByMinPrice(filterValue, sortBy, size, page)
					.stream()
					.map(this::fromEntity);
		}
		
		//8
		else if(filterType.equals("byMaxPrice")) {
			col = this.catalogService
					.getAllProductsByMaxPrice(filterValue, sortBy, size, page)
					.stream()
					.map(this::fromEntity);
			
			
		}
		
		//9
		else if(filterType.equals("byCategoryName")) {
			col = this.catalogService
					.getAllProductsByCategoryName(filterValue, sortBy, size, page)
					.stream()
					.map(this::fromEntity);
		}
		
		return col
				.collect(Collectors.toList())
				.toArray(new ProductBoundary[0]);
	}


	//10: DELETE /catalog
	@RequestMapping(
			path="/catalog", 
			method = RequestMethod.DELETE)
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteCatalog() {
		this.catalogService.deleteCatalog();		
	}
	
	
	private CategoryBoundary fromEntity(Category category) {

		return new CategoryBoundary(category.getCategoryName(), category.getDescription());
	}

	private Category toEntity(CategoryBoundary categoryBoundary) {

		return new Category(categoryBoundary.getName(), categoryBoundary.getDescription());
	}

	private ProductBoundary fromEntity(Product product) {

		return new ProductBoundary(product.getId(), product.getProductName(), product.getPrice(), product.getImage(),
				product.getCategory());
	}

	private Product toEntity(ProductBoundary productBoundary) {

		return new Product(productBoundary.getName(), productBoundary.getPrice(), productBoundary.getImage(),
				productBoundary.getCategory());
	}


	// Error code 500
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, String> handleError(CategoryExistException e) {
		String message = e.getMessage();
		if (message == null) {
			message = "Category already exist!";
		}
		return Collections.singletonMap("Error", message);
	}
	
	// Error code 404
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Map<String, String> handleException (ProductNotFoundException e){
		return Collections.singletonMap("error", "product not found");
	}
	

}
