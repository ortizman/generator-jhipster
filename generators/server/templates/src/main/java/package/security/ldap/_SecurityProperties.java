package <%=packageName%>.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific for Security.
 *
 * <p>
 * Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "security.auth", ignoreUnknownFields = true)
public class SecurityProperties {

	public enum Mode {
		BD, LDAP
	}

	private Mode mode = Mode.BD;

	private Ldap ldap = new Ldap();

	public static class Ldap {

		private String url;
		private String groupSearchFilter;
		private String userSearchFilter;
		private String groupSearchBase;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getGroupSearchFilter() {
			return groupSearchFilter;
		}

		public void setGroupSearchFilter(String groupSearchFilter) {
			this.groupSearchFilter = groupSearchFilter;
		}

		public String getUserSearchFilter() {
			return userSearchFilter;
		}

		public void setUserSearchFilter(String userSearchFilter) {
			this.userSearchFilter = userSearchFilter;
		}

		public String getGroupSearchBase() {
			return groupSearchBase;
		}

		public void setGroupSearchBase(String groupSearchBase) {
			this.groupSearchBase = groupSearchBase;
		}

	}

	public Ldap getLdap() {
		return ldap;
	}

	public void setLdap(Ldap ldap) {
		this.ldap = ldap;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
}
