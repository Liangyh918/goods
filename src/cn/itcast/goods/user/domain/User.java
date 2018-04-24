package cn.itcast.goods.user.domain;

public class User {
	/**
	 * ±ít_user×Ö¶Î
	 */
	private String uid;
	private String loginname;
	private String loginpass;
	private String email;
	private boolean status;
	private String activationCode;
	/**
	 * 	
	 * 
	 */
	private String reloginpass;
	private String verifycode;
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getLoginpass() {
		return loginpass;
	}
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activationCode == null) ? 0 : activationCode.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((loginname == null) ? 0 : loginname.hashCode());
		result = prime * result + ((loginpass == null) ? 0 : loginpass.hashCode());
		result = prime * result + ((reloginpass == null) ? 0 : reloginpass.hashCode());
		result = prime * result + (status ? 1231 : 1237);
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		result = prime * result + ((verifycode == null) ? 0 : verifycode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (activationCode == null) {
			if (other.activationCode != null)
				return false;
		} else if (!activationCode.equals(other.activationCode))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (loginname == null) {
			if (other.loginname != null)
				return false;
		} else if (!loginname.equals(other.loginname))
			return false;
		if (loginpass == null) {
			if (other.loginpass != null)
				return false;
		} else if (!loginpass.equals(other.loginpass))
			return false;
		if (reloginpass == null) {
			if (other.reloginpass != null)
				return false;
		} else if (!reloginpass.equals(other.reloginpass))
			return false;
		if (status != other.status)
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		if (verifycode == null) {
			if (other.verifycode != null)
				return false;
		} else if (!verifycode.equals(other.verifycode))
			return false;
		return true;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getReloginpass() {
		return reloginpass;
	}
	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}
	public String getVerifycode() {
		return verifycode;
	}
	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", loginname=" + loginname + ", loginpass=" + loginpass + ", email=" + email
				+ ", status=" + status + ", activationCode=" + activationCode + ", reloginpass=" + reloginpass
				+ ", verifycode=" + verifycode + "]";
	}
	
}
