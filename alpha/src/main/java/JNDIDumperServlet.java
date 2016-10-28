

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.connectivity.api.DestinationFactory;
import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

/**
 * Servlet implementation class JNDIDumperEJBServlet
 */
@WebServlet("/JNDIDump")
public class JNDIDumperServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5104599726368547290L;

	private static final Logger LOGGER = LoggerFactory.getLogger(JNDIDumperServlet.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();

		Context ctx;
		try {
			ctx = new InitialContext();
			@SuppressWarnings("unused")
			DestinationFactory destinationFactory = (DestinationFactory) ctx.lookup(DestinationFactory.JNDI_NAME);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			LOGGER.debug("JNDIDump.init() got an exception:", e);
		}

	}

	/** {@inheritDoc} */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// appendPersonTable(response);
			// appendAddForm(response);
			PrintWriter rw = response.getWriter();

			rw.println("<html><head></head><body>");
			rw.println("<H3>Fun with Servlets! v 5.3</H3>");

			appendStyleSheet(rw);
			appendUserInformation(request, rw);
			appendSystemEnvironment(rw);
			appendServletContextList(rw);
			appendSystemProperties(rw);
			appendNamingContextList(rw);

			rw.println("</body></html>");

		} catch (Exception e) {
			response.getWriter().println("Naming operation failed with reason: " + e.getMessage());
			LOGGER.error("Naming operation failed", e);
		}
	}

	private void appendStyleSheet(PrintWriter rw) {
		rw.println("<style>");
		rw.println("td { ");
		rw.println("  border: darkblue;");
		rw.println("  border-width: thin;");
		rw.println("  border-style: solid;");
		rw.println("   } ");
		rw.println("code { ");
		rw.println("  background: #e0e0ff;");
		rw.println("   } ");
		rw.println("</style>");
	}

	private void appendUserInformation(HttpServletRequest request, PrintWriter rw) {
		// TODO Auto-generated method stub
		rw.println("<hr>");
		rw.println("<h2>UserPrinciple</h2>");

		Principal up = request.getUserPrincipal();
		rw.println("<p><b>UserPrincipal name</b>: " + stringify(up) + " / "
				+ (up == null ? "null" : stringify(up.getName())) + "</p>");
		rw.println("<p><b>RemoteUser</b>: " + stringify(request.getRemoteUser()));

		if (up != null) {
			try {
				UserProvider users = UserManagementAccessor.getUserProvider();
				// Read the currently logged in user from the user storage
				User user = users.getUser(up.getName());
				rw.println("<p><b>companyid</b>: " + stringify(user.getAttribute("companyid")) + "</p>");
				rw.println("<p><b>loggedinuserid</b>: " + stringify(user.getAttribute("loggedinuserid")) + "</p>");

				user = users.getCurrentUser();
				rw.println("<p><b>CurrentUser.getName</b>: " + stringify(user.getName()) + "</p>");
				rw.println("<p><b>companyid</b>: " + stringify(user.getAttribute("companyid")) + "</p>");
				rw.println("<p><b>loggedinuserid</b>: " + stringify(user.getAttribute("loggedinuserid")) + "</p>");
			} catch (PersistenceException | UnsupportedUserAttributeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void appendSystemEnvironment(PrintWriter rw) {
		Map<String, String> environment = System.getenv();

		rw.println("<hr>");
		rw.println("<h2>System environment</h2>");

		for (Object k : environment.keySet()) {
			rw.print("<p><b>" + stringify(k) + "</b>: ");
			rw.println(stringify(environment.get((String) k)) + "</p>");
		}

	}

	private void appendSystemProperties(PrintWriter rw) {
		Properties properties = System.getProperties();

		rw.println("<hr>");
		rw.println("<h2>System properties</h2>");

		for (Object k : properties.keySet()) {
			rw.print("<p><b>" + stringify(k) + "</b>: ");
			rw.println(stringify(properties.getProperty((String) k)) + "</p>");
		}

	}

	// /** {@inheritDoc} */
	// @Override
	// protected void doPost(HttpServletRequest request,
	// HttpServletResponse response) throws ServletException, IOException {
	// try {
	// doAdd(request);
	// doGet(request, response);
	// } catch (Exception e) {
	// response.getWriter().println(
	// "Persistence operation failed with reason: "
	// + e.getMessage());
	// LOGGER.error("Persistence operation failed", e);
	// }
	// }

	private String stringify(Object o) {
		// TODO Auto-generated method stub
		if (o == null)
			return "[null]";
		if (String.class.isAssignableFrom(o.getClass()))
			return (String) o;
		if (CharSequence.class.isAssignableFrom(o.getClass()))
			return o.toString();

		return "[" + o.getClass().getName() + "] " + o.toString();
	}

	private void appendServletContextList(PrintWriter rw) throws IOException {
		rw.println("<hr>");
		rw.println("<h2>Servlet container information</h2>");

		ServletContext sc = this.getServletContext();

		rw.println("<p><b>Server:</b> " + sc.getServerInfo() + "</p>");
		rw.println("<p><b>Context Path:</b> <code>" + sc.getContextPath() + "</code></p>");

		Map<String, ? extends ServletRegistration> sregs = sc.getServletRegistrations();

		rw.println("<h2>Servlet registrations</h2>");
		int tableFlag = 0;
		for (Entry<String, ? extends ServletRegistration> sr : sregs.entrySet()) {
			if (tableFlag == 0) {
				rw.println("<table>");
				tableFlag++;
			}
			String k = sr.getKey();
			rw.print("<tr><td>" + k);
			String n = sr.getValue().getName();
			if (!k.equalsIgnoreCase(n))
				rw.print(" (" + n + ")");
			rw.println("</td><td>");
			for (String m : sr.getValue().getMappings()) {
				rw.println("<p><code>" + m + "</code></p>");
			}
			rw.println("</td></tr>");
		}
		if (tableFlag > 0)
			rw.println("</table>");
	}

	private void appendNamingContextList(PrintWriter rw) throws IOException {
		// TODO Auto-generated method stub

		rw.println("<hr>");
		rw.println("<h2>JNDI information</h2>");

		Context ctx = null;

		try {
			ctx = new InitialContext();

			rw.println("<p>Got ctx:" + ctx.getEnvironment() + "</p><h3>ctx.list():</h3>");
			NamingEnumeration list = ctx.list("java:");
			boolean found = false;

			if (list.hasMore()) {
				this.printContextList(rw, ctx, list, "java:", "");
				// NameClassPair nc = (NameClassPair) list.next();
				// rw.println("<b>" + nc.getName() + ": </b>" +
				// nc.getClassName());
				found = true;
			}
			if (!found)
				rw.println("<nbsp><i>List empty.</i>");

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			rw.println("<p>list() Oops:</p><p style='font-size: 80%'>");
			e.printStackTrace(rw);
			rw.println("</p>:(");
			// e.printStackTrace();
		}

		try {
			ctx = new InitialContext();

			boolean found = false;

			rw.println("<h3>ctx.listBindings():</h3>");
			NamingEnumeration list = ctx.listBindings("java:");

			while (list.hasMore()) {
				this.printContextBindings(rw, ctx, list, "java:", "");
				// NameClassPair nc = (NameClassPair) list.next();
				// rw.println("<b>" + nc.getName() + ": </b>" +
				// nc.getClassName());
				found = true;
			}
			if (!found)
				rw.println("<nbsp><i>Bindings empty.</i>");

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			rw.println("<p>listBindings() Oops:</p><p style='font-size: 80%'>");
			e.printStackTrace(rw);
			rw.println("</p>:(");
			// e.printStackTrace();
		}

		try {
			ctx = new InitialContext();
			Context envCtx = (Context) ctx.lookup("java:comp/env/jdbc");

			boolean found = false;

			rw.println("<h4>envCtx.list():</h4>");
			NamingEnumeration list = envCtx.list("");

			while (list.hasMore()) {
				NameClassPair nc = (NameClassPair) list.next();
				rw.println("<b>" + nc.getName() + ": </b>" + nc.getClassName());
				found = true;
			}
			if (!found)
				rw.println("<nbsp><i>Bindings empty.</i>");

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			rw.println("<p>lookup() Oops:</p><p style='font-size: 80%'>");
			e.printStackTrace(rw);
			rw.println("</p>:(");
			// e.printStackTrace();
		}

		rw.println("<hr><h3>defaultManagedDataSource:</h3>");

		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/defaultManagedDataSource");

			try {
				Connection conn = ds.getConnection();
				DatabaseMetaData dbmetadata = conn.getMetaData();
				rw.println("<p><b>URL:</b>" + dbmetadata.getURL() + "</p>");
				rw.println("<p><b>DriverName:</b>" + dbmetadata.getDriverName() + "</p>");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				rw.println("<p>getConnection() Oops:</p><p style='font-size: 80%'>");
				e.printStackTrace(rw);
				rw.println("</p>:(");

			}

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			rw.println("<p>lookup( defaultManagedDataSource ) Oops:</p><p style='font-size: 80%'>");
			e.printStackTrace(rw);
			rw.println("</p>:(");

		}

	}

	private void printContextList(PrintWriter rw, Context ctx, NamingEnumeration list, String prefix, String padding)
			throws NamingException {
		// TODO Auto-generated method stub
		while (list.hasMore()) {
			NameClassPair nc = (NameClassPair) list.next();
			rw.println("<p><b>" + padding + prefix + nc.getName() + ": </b>" + nc.getClassName() + "</p>");
			if (nc.getClassName().contains("NamingContext"))
				printContextList(rw, ctx, ((Context) ctx.lookup(prefix + nc.getName())).list(""), prefix + nc.getName()
						+ "/", padding + "&nbsp&#8231;&nbsp&#8231;");
		}
	}

	private void printContextBindings(PrintWriter rw, Context ctx, NamingEnumeration list, String prefix, String padding)
			throws NamingException {
		// TODO Auto-generated method stub
		while (list.hasMore()) {
			NameClassPair nc = (NameClassPair) list.next();
			rw.println("<p><b>" + padding + prefix + nc.getName() + ": </b>" + nc.getClassName() + "</p>");
			if (nc.getClassName().contains("NamingContext"))
				printContextList(rw, ctx, ((Context) ctx.lookup(prefix + nc.getName())).listBindings(""), prefix
						+ nc.getName() + "/", padding + "&nbsp&#8231;&nbsp&#8231;");
		}
	}

	// private void doAdd(HttpServletRequest request) throws ServletException,
	// IOException, SQLException {
	// // Extract name of person to be added from request
	// String firstName = request.getParameter("FirstName");
	// String lastName = request.getParameter("LastName");
	//
	// if (firstName != null && lastName != null
	// && !firstName.trim().isEmpty() && !lastName.trim().isEmpty()) {
	// Person person = new Person();
	// person.setFirstName(firstName);
	// person.setLastName(lastName);
	//
	// personBean.addPerson(person);
	// }
	// }
}