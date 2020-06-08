package volodymyr.dao;

import volodymyr.domain.User;
import volodymyr.shared.AbstractCRUD;

public interface UserDao extends AbstractCRUD<User>{

	User getUserByEmail(String email);

}
