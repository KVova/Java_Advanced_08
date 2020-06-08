package volodymyr.service;

import volodymyr.domain.User;
import volodymyr.shared.AbstractCRUD;

public interface UserService extends AbstractCRUD<User>{
	 User getUserByEmail(String email);
}
