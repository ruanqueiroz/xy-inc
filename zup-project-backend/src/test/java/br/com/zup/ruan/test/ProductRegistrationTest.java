package br.com.zup.ruan.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.zup.ruan.model.Product;
import br.com.zup.ruan.service.ProductRegistration;
import br.com.zup.ruan.util.Resources;

@RunWith(Arquillian.class)
public class ProductRegistrationTest {
	private static final double PRICE = 100D;
	private static final String DESCRIPTION = "descricar do produto de teste";
	private static final String PRODUCT_NAME = "Produto";
	private static final String DESCRIPTION2 = "descricao do produto 2 de teste";
	private static final double PRICE2 = 150D;
	private static final String CATEGORY = "Produto";
	private static final String PRODUCT_NAME2 = "Produto 2";

	
	@Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "testzupproject.war")
            .addClasses(Product.class, ProductRegistration.class, Resources.class)
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("test-ds.xml");
    }

    @Inject
    ProductRegistration productRegistration;

    /**
     * Test save product
     * @throws Exception
     */
    @Test
    public void testSave() throws Exception {
    	Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.setCategory(CATEGORY);
        product.setPrice(PRICE);
        product.setDescription(DESCRIPTION);
    	
    	productRegistration.create(product);
        assertNotNull(product.getId());
        productRegistration.delete(product);
        product = productRegistration.getById(product.getId());
        assertNull(product);
    }

    /**
     * Test update product and delete
     * @throws Exception
     */
    @Test
    public void testUpdateAndDelete() throws Exception {
    	Product product = new Product();
    	product.setName(PRODUCT_NAME);
    	product.setCategory(CATEGORY);
    	product.setPrice(PRICE);
    	product.setDescription(DESCRIPTION);
    	productRegistration.create(product);
        assertNotNull(product.getId());
        
        product.setName(PRODUCT_NAME2);
        product.setPrice(PRICE2);
        product.setDescription(DESCRIPTION2);
        productRegistration.update(product);
        product = productRegistration.getById(product.getId());
        assertEquals(product.getDescription(), DESCRIPTION2);
        assertEquals(product.getName(), PRODUCT_NAME2);
                
        productRegistration.delete(product);
        product = productRegistration.getById(product.getId());
        assertNull(product);
    }
    
   
}

