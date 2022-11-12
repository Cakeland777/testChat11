package server1;


import java.sql.SQLException;
import java.util.Scanner; 
import member.Member; 
import member.Member.ExistMember; 
import member.Member.NotExistUidPwd; 

public interface MemberRepository { 
	void insertMember(Member member) throws Member.ExistMember; 
	Member findByUid(String uid) throws Member.NotExistUidPwd; 
	void updateMember(Member member) throws Member.NotExistUidPwd; 
	void detail(Member member) throws NotExistUidPwd; 
	void insertTest(Scanner scanner, Member member) throws ExistMember; 
} 
