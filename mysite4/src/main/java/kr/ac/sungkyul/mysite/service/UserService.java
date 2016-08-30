package kr.ac.sungkyul.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ac.sungkyul.mysite.dao.UserDao;
import kr.ac.sungkyul.mysite.vo.UserVo;
import kr.ac.sunkyul.mysite.exception.UserInfoUpdateException;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;

	public void join(UserVo vo) {
		userDao.insert(vo);
	}

	public UserVo login(String email, String password) {
		UserVo authUser = userDao.get(email, password);
		return authUser;
	}

	public void updateInfo(UserVo vo) {
		try {
			userDao.update(vo);
		} catch (UserInfoUpdateException e) {

		}
	}
}