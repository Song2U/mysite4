package kr.ac.sungkyul.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.ac.sungkyul.mysite.vo.BoardVo;

@Repository
public class BoardDao {
	@Autowired
	private SqlSession sqlSession;
		
	public void update( BoardVo vo ) {
		sqlSession.update("user.update", vo);
	}
	
	public void updateViewCount( Long no ) {
		sqlSession.update("user.updateViewCount", no);
	}
	
	public BoardVo get( Long no ) {
		BoardVo vo = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = sqlSession.getConnection();
			
			String sql = "select no, title, content, user_no from board where no = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, no);
			rs = pstmt.executeQuery();
			
			if( rs.next() ) {
				vo = new BoardVo();
				vo.setNo( rs.getLong(1) );
				vo.setTitle( rs.getString(2) );
				vo.setContent( rs.getString(3) );
				vo.setUserNo( rs.getLong(4) );	
			}
			
		} catch( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null ) {
					rs.close();
				}
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		return vo;
	}
	
	public int getTotalCount(){
		
		int totalCount = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = sqlSession.getConnection();
			
			String sql = "select count(*) from board";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			
			if( rs.next() ) {
				totalCount = rs.getInt( 1 );
			}
			
		} catch( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null ) {
					rs.close();
				}
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		return totalCount;		
	}
	
	public List<BoardVo> getList( int page, int pagesize ) {
		List<BoardVo> list = new ArrayList<BoardVo>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = sqlSession.getConnection();
			String sql =
				"SELECT * " +
				"  FROM (SELECT c.*, rownum rn" +
				"          FROM (  SELECT a.no, a.title, b.name, a.user_no, a.view_count, to_char(reg_date, 'yyyy-mm-dd pm hh:mi:ss'), depth" +
				"                    FROM board a," +
				"                         users b" +
				"                   WHERE a.user_no = b.no" +
				"                ORDER BY group_no DESC, order_no ASC) c )" +
				" WHERE ?  <= rn" +
				"   AND rn <= ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, (page-1)*pagesize + 1);
			pstmt.setInt(2, page*pagesize);
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				Long no = rs.getLong( 1 );
				String title = rs.getString( 2 );
				String userName = rs.getString( 3 );
				Long userNo = rs.getLong( 4 );
				Integer viewCount = rs.getInt( 5 );
				String regDate = rs.getString( 6 );
				Integer depth = rs.getInt( 7 );

				BoardVo vo = new BoardVo();
				vo.setNo( no );
				vo.setTitle( title );
				vo.setUserName( userName );
				vo.setUserNo( userNo );
				vo.setViewCount( viewCount );
				vo.setRegDate( regDate );
				vo.setDepth( depth );
				
				list.add( vo );
			}
			
			return list;
		} catch( SQLException ex ) {
			System.out.println( "error: " + ex);
			return list;
		} finally {
			try{
				if( rs != null ) {
					rs.close();
				}
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			}catch( SQLException ex ) {
				ex.printStackTrace();
			}
		}
	}
	
	public void insert( BoardVo vo ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try{
			conn = sqlSession.getConnection();
			
			String sql = "insert into board values(seq_board.nextval, ?, ?, sysdate, 0, nvl((select max(group_no) from board), 0) + 1, 1, 1, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString( 1, vo.getTitle() );
			pstmt.setString( 2, vo.getContent() );
			pstmt.setLong( 3, vo.getUserNo() );
			
			pstmt.executeUpdate();
			
		} catch( SQLException e ) {
			e.printStackTrace();
		} finally {
			try{
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}		
	}
}
