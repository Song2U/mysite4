package kr.ac.sungkyul.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.ac.sungkyul.mysite.vo.GuestbookVo;

@Repository
public class GuestbookDao {
	@Autowired
	private SqlSession sqlSession;
	
	public void delete( GuestbookVo vo ) {
		int count = sqlSession.delete("guestbook.delete", vo);
		System.out.println(count);
	}
	
	public boolean insert( GuestbookVo vo ) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		
		try {
			conn = sqlSession.getConnection();
			
			String sql = 
				" insert" +
				"   into guestbook" +
				" values(seq_guestbook.nextval, ?, ?, ?, sysdate)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString( 1, vo.getName() );
			pstmt.setString( 2, vo.getPassword() );
			pstmt.setString( 3, vo.getContent() );
			
			count = pstmt.executeUpdate();
			
		} catch( SQLException e ) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch( SQLException e ) {
				e.printStackTrace();
				return false;
			}
		}		
		
		return (count == 1);
	}
	
	public List<GuestbookVo> getList() {
		List<GuestbookVo> list = new ArrayList<GuestbookVo>();
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = sqlSession.getConnection();
			stmt = conn.createStatement();
			
			String sql = 
				"   select no, " +
			    "          name, " + 
			    "          content, " + 
				"          to_char(reg_date, 'yyyy-mm-dd pm hh:mm:dd')" +
			    "     from guestbook" +
				" order by reg_date desc";
			
			rs = stmt.executeQuery(sql);
			while( rs.next() ) {
				Long no = rs.getLong( 1 );
				String name = rs.getString( 2 );
				String content = rs.getString( 3 );
				String regDate = rs.getString( 4 );
				
				GuestbookVo vo = new GuestbookVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setContent(content);
				vo.setRegDate(regDate);
				
				list.add( vo );
			}
			
		} catch( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null ) {
					rs.close();
				}
				if( stmt != null ) {
					stmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
