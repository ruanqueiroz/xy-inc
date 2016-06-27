package br.com.zup.ruan.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.zup.ruan.model.Product;


/**
 * 
 * @author ruan.queiroz
 * 
 * Responsible for receiving the requests
 *
 */
@RequestScoped
public class ProductListProducer {

	@Inject
	private ProductRepository productRepository;

	private List<Product> products;

	/**
	 * Get products list
	 * @return product list
	 */
	@Produces
	@Named
	public List<Product> getProducts() {
		return products;
	}
	
	@PostConstruct
	public void getAllProducts() {
		products = productRepository.getAllProducts();
	}

	/**
	 * Called from ui when change product list, add or remove product  
	 * @param product
	 */
	public void onProductListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Product product) {
		getAllProducts();
	}

	
}
