package member;

import java.io.Serializable;
import java.util.Objects;

import org.json.JSONObject;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Member implements Serializable {
	private static final long serialVersionUID = 1449132512754742285L;
	private String uid;
	private String pwd;
	private String name;
	private String sex;
	private String address;
	private String phone;
	private String exist;
	
	public Member(String uid, String pwd, String name, String sex, String address, String phone,String exist) {
		super();
		this.uid = uid;
		this.pwd = pwd;
		this.name = name;
		this.sex = sex;
		this.address = address;
		this.phone = phone;
		this.exist=exist;
	}
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getExist() {
		return exist;
	}

	public void setExist(String exist) {
		this.exist = exist;
	}

	public Member(JSONObject jsonObject) {
		uid = jsonObject.getString("uid");
		pwd = jsonObject.getString("pwd");
		name = jsonObject.getString("name");
		sex = jsonObject.getString("sex");
		address = jsonObject.getString("address");
		phone = jsonObject.getString("phone");
       
	}
	
	public Member() {
    }

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		return Objects.equals(uid, other.uid);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(uid);
	}

	public static class ExistMember extends Exception {
		public ExistMember(String reason) {
			super(reason);
		}
	}
	
	public static class NotExistMember extends Exception {
		public NotExistMember(String reason) {
			super(reason);
		}
	}
	
	public static class NotExistUidPwd extends Exception {
		public NotExistUidPwd(String reason) {
			super(reason);
		}
	}

    public JSONObject getJsonObject() {
        JSONObject jsonMember = new JSONObject();
        jsonMember.put("uid", uid);
        jsonMember.put("pwd", pwd);
        jsonMember.put("name", name);
        jsonMember.put("sex", sex);
        jsonMember.put("address", address);
        jsonMember.put("phone", phone);
        return jsonMember;
    }

    public boolean login(String uid2, String pwd2) {
        return uid2.equals(uid) && pwd2.equals(pwd);
    }

}
