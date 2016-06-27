package br.com.zup.ruan.data;



import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import br.com.zup.ruan.model.Product;


/**
 * 
 * @author ruan.queiroz
 * 
 * Responsible for run queries on repositoryPerforms searches in the repository
 *
 */
@ApplicationScoped
public class ProductRepository {

	/**
	 * Interface to interact with the persistence context.
	 */
    @Inject
    private EntityManager entityManager;

    /**
     * Get all products sorted by name
     * @return all products
     */
    public List<Product> getAllProducts() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = criteriaBuilder.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product).orderBy(criteriaBuilder.asc(product.get("name")));

        return entityManager.createQuery(criteria).getResultList();
    }
    
    /**
     * Get product by id
     * @param id of the product
     * @return product
     */
    public Product getById(Long id) {
        return entityManager.find(Product.class, id);
    }

    /**
     * Get product by name
     * @param name of the product
     * @return product
     */
    public Product getByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = criteriaBuilder.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product).where(criteriaBuilder.equal(product.get("name"), name));

        return entityManager.createQuery(criteria).getSingleResult();
    }
    
    /**
     * Update product
     * @param product
     */
    public void update(Product product){
    	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    	CriteriaUpdate<Product> criteria = criteriaBuilder.createCriteriaUpdate(Product.class);
    	Root<Product> baseProduct = criteria.from(Product.class);
    	criteria.set("name", product.getName());
    	criteria.set("description", product.getDescription());
    	criteria.set("category", product.getCategory());
    	criteria.set("price", product.getPrice());
    	criteria.where(criteriaBuilder.equal(baseProduct.get("id"),product.getId()));
    		  
    	entityManager.createQuery(criteria).executeUpdate();
    }
    
    /**
     * Delete product
     * @param product
     */
    public void delete(Product product) {
    	CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    	CriteriaDelete<Product> delete = criteriaBuilder.createCriteriaDelete(Product.class);
    	Root<Product> baseProduct = delete.from(Product.class);
    	delete.where(criteriaBuilder.equal(baseProduct.get("id"), product.getId()));
    	
    	this.entityManager.createQuery(delete).executeUpdate();
    }    

}
