package br.com.zup.ruan.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.zup.ruan.data.ProductRepository;
import br.com.zup.ruan.model.Product;
import br.com.zup.ruan.service.ProductRegistration;
import br.com.zup.ruan.util.Constants;

/**
 * @author ruan.queiroz
 * 
 *         Class defines the methods of the REST API for products
 * 
 *         Defines the methods 
 *         
 *         GET /products - get all products
 *         GET /products/{id} - get product by id 
 *         POST /products - create/save new product 
 *         PUT /products - update product 
 *         DELETE /products/{id} - delete product
 *
 */
@Path("/products")
@RequestScoped
public class ProductResourceRESTService {

	@Inject
	private Validator validator;

	@Inject
	private ProductRepository productRepository;

	@Inject
	ProductRegistration productRegistration;

	/**
	 * Get all products
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> getProducts() {
		return productRepository.getAllProducts();
	}

	/**
	 * Get product by id
	 * 
	 * @param id
	 *            of the product
	 * @return product
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Product getProductById(@PathParam("id") long id) {
		Product product = productRepository.getById(id);
		if (product == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return product;
	}

	/**
	 * Create new product
	 * 
	 * Returns response code 200 if save the product
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createProduct(Product product) {
		Response.ResponseBuilder responseBuilder = null;

		try {
			validate(product);
			productRegistration.create(product);
			responseBuilder = Response.ok(product);

		} catch (ConstraintViolationException ce) {
			responseBuilder = createViolationResponseBuilder(ce.getConstraintViolations());
		} catch (Exception e) {
			Map<String, String> responseMap = new HashMap<>();
			responseMap.put(Constants.ERROR_NAME_GENERIC_EXCEPTION, e.getMessage());
			responseBuilder = Response.status(Response.Status.BAD_REQUEST).entity(responseMap);
		}

		return responseBuilder.build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateProduct(Product product) {
		Response.ResponseBuilder responseBuilder = null;

		try {
			if (productRepository.getById(product.getId()) == null) {
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}

			validate(product);
			productRegistration.update(product);
			responseBuilder = Response.ok(Constants.MSG_UPDATE_PRODUCT_SUCCESSFUL);

		} catch (ConstraintViolationException ce) {
			responseBuilder = createViolationResponseBuilder(ce.getConstraintViolations());
		} catch (Exception e) {
			Map<String, String> responseMap = new HashMap<>();
			responseMap.put(Constants.ERROR_NAME_GENERIC_EXCEPTION, e.getMessage());
			responseBuilder = Response.status(Response.Status.BAD_REQUEST).entity(responseMap);
		}
		return responseBuilder.build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteProduct(@PathParam("id") long id) {
		Response.ResponseBuilder responseBuilder = null;

		try {
			Product productOld = productRepository.getById(id);
			if (productOld == null) {
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}

			productRegistration.delete(productOld);

			responseBuilder = Response.ok(Constants.MSG_DELETE_PRODUCT_SUCCESSFUL);

		} catch (Exception e) {
			Map<String, String> responseMap = new HashMap<>();
			responseMap.put(Constants.ERROR_NAME_GENERIC_EXCEPTION, e.getMessage());
			responseBuilder = Response.status(Response.Status.BAD_REQUEST).entity(responseMap);
		}
		return responseBuilder.build();
	}

	/**
	 * Validate if already product
	 * 
	 * @param product
	 * @throws ConstraintViolationException
	 * @throws ValidationException
	 */
	private void validate(Product product) throws ConstraintViolationException, ValidationException {
		Set<ConstraintViolation<Product>> violations = validator.validate(product);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}
	

	/**
	 * 
	 * Create builder response BAR_REQUEST
	 *
	 * @param violations
	 * @return JAX-RS response all violations
	 */
	private Response.ResponseBuilder createViolationResponseBuilder(Set<ConstraintViolation<?>> violations) {
		Map<String, String> responseMap = new HashMap<>();

		for (ConstraintViolation<?> violation : violations) {
			responseMap.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(responseMap);
	}
	
}
