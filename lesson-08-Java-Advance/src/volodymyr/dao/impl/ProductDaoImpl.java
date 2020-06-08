package volodymyr.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import volodymyr.dao.ProductDao;
import volodymyr.domain.Product;
import volodymyr.utils.ConnectionUtils;

public class ProductDaoImpl implements ProductDao {

	private static String READ_ALL = "select * from product";
	private static String CREATE = "insert into product(name, description, price) value(?,?,?)";
	private static String READ_BY_ID = "select * from product where id =?";
	private static String UPDATE_BY_ID = "update product set name=?, description=?, price=? where id =?";
	private static String DELETE_BY_ID = "delete from product where id =?";

	private static Logger LOGGER = Logger.getLogger(ProductDaoImpl.class);

	private Connection connection;
	private PreparedStatement preparedStatemant;

	public ProductDaoImpl()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		connection = ConnectionUtils.openConnection();
	}

	@Override
	public Product create(Product product) {
		try {
			preparedStatemant = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
			preparedStatemant.setString(1, product.getName());
			preparedStatemant.setString(2, product.getDescription());
			preparedStatemant.setDouble(3, product.getPrice());
			preparedStatemant.executeUpdate();

			ResultSet rs = preparedStatemant.getGeneratedKeys();
			rs.next();
			product.setId(rs.getInt(1));

		} catch (SQLException e) {
			LOGGER.error(e);
		}
		return product;
	}

	@Override
	public Product read(Integer id) {
		Product product = null;
		try {
			preparedStatemant = connection.prepareStatement(READ_BY_ID);
			preparedStatemant.setInt(1, id);
			ResultSet result = preparedStatemant.executeQuery();
			result.next();

			Integer productId = result.getInt("id");
			String name = result.getString("name");
			String description = result.getString("description");
			Double price = result.getDouble("price");
			product = new Product(productId, name, description, price);

		} catch (SQLException e) {
			LOGGER.error(e);
		}
		return product;
	}

	@Override
	public Product update(Product product) {
		try {
			preparedStatemant = connection.prepareStatement(UPDATE_BY_ID);
			preparedStatemant.setString(1, product.getName());
			preparedStatemant.setString(2, product.getDescription());
			preparedStatemant.setDouble(3, product.getPrice());
			preparedStatemant.setInt(4, product.getId());
			preparedStatemant.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error(e);
		}
		return product;
	}

	@Override
	public void delete(Integer id) {
		try {
			preparedStatemant = connection.prepareStatement(DELETE_BY_ID);
			preparedStatemant.setInt(1, id);
			preparedStatemant.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error(e);
		}

	}

	@Override
	public List<Product> readAll() {
		List<Product> productRecords = new ArrayList<>();

		try {
			preparedStatemant = connection.prepareStatement(READ_ALL);
			ResultSet result = preparedStatemant.executeQuery();
			while (result.next()) {
				Integer productId = result.getInt("id");
				String name = result.getString("name");
				String description = result.getString("description");
				Double price = result.getDouble("price");
				productRecords.add(new Product(productId, name, description, price));
			}
		} catch (SQLException e) {
			LOGGER.error(e);
		}
		return productRecords;
	}
}
