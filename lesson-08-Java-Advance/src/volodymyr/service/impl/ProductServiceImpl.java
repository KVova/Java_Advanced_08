package volodymyr.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import volodymyr.dao.ProductDao;
import volodymyr.dao.impl.ProductDaoImpl;
import volodymyr.domain.Product;
import volodymyr.service.ProductService;

public class ProductServiceImpl implements ProductService {

	private static Logger LOGGER = Logger.getLogger(ProductServiceImpl.class);
	private static ProductService productServiceImpl;
	private ProductDao productDao;

	private ProductServiceImpl() {
		try {
			productDao = new ProductDaoImpl();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			LOGGER.error(e);
		}
	}

	public static ProductService getProductService() {
		if (productServiceImpl == null) {
			productServiceImpl = new ProductServiceImpl();
		}
		return productServiceImpl;
	}

	@Override
	public Product create(Product t) {
		return productDao.create(t);
	}

	@Override
	public Product read(Integer id) {
		return productDao.read(id);
	}

	@Override
	public Product update(Product t) {
		return productDao.update(t);
	}

	@Override
	public void delete(Integer id) {
		productDao.delete(id);
	}

	@Override
	public List<Product> readAll() {
		return productDao.readAll();
	}

}
