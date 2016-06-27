package br.com.zup.ruan.service;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.zup.ruan.model.Product;

/**
 * 
 * @author ruan.queiroz
 * 
 *         Defines methods of persistence to front-end web
 *
 */
@Stateless
public class ProductRegistration {

	@Inject
	private EntityManager entityManager;

	@Inject
	private Event<Product> event;

	/**
	 * Get product by id
	 * @param id of the product
	 * @return product
	 */
	public Product getById(Long id) {
		return entityManager.find(Product.class, id);
	}
	
	/**
	 * Create/save new product
	 * @param product
	 * @throws Exception
	 */
	public void create(Product product) throws Exception {
		entityManager.persist(product);
		event.fire(product);
	}

	/**
	 * Update product
	 * @param product
	 * @throws Exception
	 */
	public void update(Product product) throws Exception {
		entityManager.merge(product);
		event.fire(product);
	}

	/**
	 * Delete product
	 * @param product
	 * @throws Exception
	 */
	public void delete(Product product) throws Exception {
		entityManager.remove(entityManager.contains(product) ? product : entityManager.merge(product));
		event.fire(product);
	}
}
