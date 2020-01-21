package CatalogService;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;

public interface ProductCrud extends PagingAndSortingRepository<Product, String> {
	
	public List<Product> findAllByproductName(
			@Param("productName") String productName, 
			Pageable pageable);
		
	public List<Product> findAllByCategory_categoryName(
			@Param("categoryName") String categoryName, 
			Pageable pageable);
	
	public List<Product> findAllByPriceGreaterThan(long price, Pageable pageable);
	
	public List<Product> findAllByPriceLessThan(long price, Pageable pageable);

	
}
