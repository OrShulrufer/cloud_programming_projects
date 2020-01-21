package CatalogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("init")
public class ProductInitializer implements CommandLineRunner {
	
	private CatalogService catalogService;

	@Autowired
	public ProductInitializer(CatalogService catalogService) {
		super();
		this.catalogService = catalogService;
	}

	@Override
	public void run(String... args) throws Exception {
		
		Category c1 = new Category("Jeans", "Long Sleaves");
		Category c2 = new Category("Hat", "black hat");
		Category c3 = new Category("Pants", "shorts");

		this.catalogService.createCategory(c1);
		this.catalogService.createCategory(c2);
		this.catalogService.createCategory(c3);
		
		Product p1 = new Product("Jeans1", 200, "Jeans1.jpg", c1);
		Product p2 = new Product("Hat1", 10, "hat1.jpg", c2);
		Product p3 = new Product("Pants1", 250, "Pants1.jpg", c3);
		Product p4 = new Product("Hat2", 300, "hat2.jpg", c2);

		Product p5 = new Product("Jeans2", 40, "Jeans2.jpg", c1);
		Product p6 = new Product("Hat3", 500, "Hat3.jpg", c2);
		Product p7 = new Product("Pants2", 60, "Pants2.jpg", c3);
		Product p8 = new Product("Pants3", 700, "Pants3.jpg", c3);
		
		Product p9 = new Product("Jeans3", 8, "Jeans3.jpg", c1);
		Product p10 = new Product("Hat4", 9, "Hat4.jpg", c2);
		Product p11 = new Product("Pants4", 10, "Pants4.jpg", c3);
		Product p12 = new Product("Jeans4", 10, "Jeans4.jpg", c1);
		
		this.catalogService.createProduct(p1);
		this.catalogService.createProduct(p2);
		this.catalogService.createProduct(p3);
		this.catalogService.createProduct(p4);
		
		this.catalogService.createProduct(p5);
		this.catalogService.createProduct(p6);
		this.catalogService.createProduct(p7);
		this.catalogService.createProduct(p8);
		
		this.catalogService.createProduct(p9);
		this.catalogService.createProduct(p10);
		this.catalogService.createProduct(p11);
		this.catalogService.createProduct(p12);
		
	}
}
