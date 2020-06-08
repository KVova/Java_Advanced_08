package volodymyr.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import volodymyr.dao.BucketDao;
import volodymyr.domain.Bucket;
import volodymyr.utils.ConnectionUtils;

public class BucketDaoImpl implements BucketDao {

	private static String READ_ALL = "select * from bucket";
	private static String CREATE = "insert into bucket(user_id, product_id, purchase_date) value(?,?,?)";
	private static String READ_BY_ID = "select * from bucket where id =?";
	private static String DELETE_BY_ID = "delete from bucket where id =?";

	private static Logger LOGGER = Logger.getLogger(BucketDaoImpl.class);

	private Connection connection;
	private PreparedStatement preparedStatemant;

	public BucketDaoImpl() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		connection = ConnectionUtils.openConnection();
	}

	@Override
	public Bucket create(Bucket bucket) {
		try {
			preparedStatemant = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
			preparedStatemant.setInt(1, bucket.getUserId());
			preparedStatemant.setInt(2, bucket.getProductId());
			preparedStatemant.setDate(3, new Date(bucket.getPurchaseDate().getTime()));
			preparedStatemant.executeUpdate();

			ResultSet rs = preparedStatemant.getGeneratedKeys();
			rs.next();
			bucket.setId(rs.getInt(1));
//			throw new SQLException();
		} catch (SQLException e) {
			LOGGER.error(e);
		}
		return bucket;
	}
	
//	public static void main(String[] args) {
//		BucketService b = new BucketServiceImpl();
//		b.create(new Bucket(1, 2, new java.util.Date()));
//	}

	@Override
	public Bucket read(Integer id) {
		Bucket bucket = null;
		try {
			preparedStatemant = connection.prepareStatement(READ_BY_ID);
			preparedStatemant.setInt(1, id);
			ResultSet result = preparedStatemant.executeQuery();
			result.next();

			Integer bucketId = result.getInt("id");
			Integer userId = result.getInt("user_id");
			Integer productId = result.getInt("product_id");
			java.util.Date purchaseDate = result.getDate("purchase_date");

			bucket = new Bucket(bucketId, userId, productId, purchaseDate);
		} catch (SQLException e) {
			LOGGER.error(e);
		}
		return bucket;
	}

	@Override
	public Bucket update(Bucket t) {
		throw new IllegalStateException("there is no update for bucket");
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
	public List<Bucket> readAll() {
		List<Bucket> bucketRecords = new ArrayList<>();

		try {
			preparedStatemant = connection.prepareStatement(READ_ALL);
			ResultSet result = preparedStatemant.executeQuery();
			while (result.next()) {
				Integer bucketId = result.getInt("id");
				Integer userId = result.getInt("user_id");
				Integer productId = result.getInt("product_id");
				java.util.Date purchaseDate = result.getDate("purchase_date");
				bucketRecords.add(new Bucket(bucketId, userId, productId, purchaseDate));
			}
		} catch (SQLException e) {
			LOGGER.error(e);
		}
		return bucketRecords;
	}
}
