package com.kh.spring.mybatis;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.spring.member.model.dto.Member;

@Mapper
public interface MybatisRepository {

	@Select("select password from member WHERE user_id = #{userId}")
	String selectPasswordByUserId(String userId);
	// 쿼리는 한곳에만 있어야함. 선언은 양쪽에 있어야함. 즉, 인터페이스에서 처리할게 아니면 선언만 하자.
	
	@Select("select * from member WHERE user_id = #{userId}")
	Member selectMemberByUserId(String userId);
	
	@Select("select * from member inner join rent_master using(user_id) WHERE user_id = #{userId}")
	List<Map<String, Object>> selectRentAndMenberByUserId(String userId);
	// 단일행인지 다중행인지. return type는 list로. 다중행이니까.
	
	List<Map<String, Object>> selectRentBookByUserId(String userId);
	// 선언만.
	
	@Select("insert into member(user_id, password, tell, email) values(#{userId},#{password},#{tell},#{email})")
	Member insertWithDto(Member member);

	@Select("insert into rent_master(rm_idx, user_id, title, rent_book_cnt) values(sc_rm_idx.nextval,#{member.userId},#{title},#{rentBookCnt})")
	Member insertWithMap(Map<String,Object> map);
	
	@Select("delete rent_master where title = #{title}")
	String delete(String title);
	
	@Select("update member set password = #{password} where user_id = #{userId}")
	Member update(Member member);
	
	@Select("{call SP_RENT_EXTENTED(#{rbIdx, mode=IN})")
	String procedure(String rbIdx);
	
	@Select("insert into book(title, author, bk_idx) values(#{title},#{author},SC_BK_IDX.nextval)")
	String test01(Map<String, String> book);
	
	@Select("update rent_book set EXTENSION_CNT = 0 where EXTENSION_CNT >= #{cnt}")
	String test02(int cnt);
	
	@Select("delete member where REG_DATE &lt;= #{regDate2} and REG_DATE >= #{regDate}")
	String test03(Map<String, Date> date);
	
	@Select("select title from(select title from book order by rent_cnt desc) WHERE rownum &lt;= #{cnt}")
	List<Map<String, Object>> test04(int cnt);
	
	
	
	
}
