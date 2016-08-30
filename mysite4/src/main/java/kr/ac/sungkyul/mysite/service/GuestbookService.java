package kr.ac.sungkyul.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ac.sungkyul.mysite.dao.GuestbookDao;
import kr.ac.sungkyul.mysite.vo.GuestbookVo;

@Service
public class GuestbookService {
	@Autowired
	private GuestbookDao guestbookDao;
	
	public GuestbookVo write(GuestbookVo vo){
		guestbookDao.insert(vo);
		return vo;
	}
	
	public GuestbookVo remove(GuestbookVo vo){
		guestbookDao.delete(vo);
		return vo;
	}
	
}
