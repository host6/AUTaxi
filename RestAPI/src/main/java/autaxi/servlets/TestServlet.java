package autaxi.servlets;

import autaxi.core.db.HibernateUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/autaxi/api")
public class TestServlet extends HttpServlet {
	// This method is called if TEXT_PLAIN is request
	protected static final Logger log = LogManager.getLogger(TestServlet.class);

	@GET
	@Path("/hello")
	@Produces({MediaType.APPLICATION_JSON})
	//@Consumes(MediaType.APPLICATION_JSON)
	public NewEntity sayPlainTextHello() throws Exception {
		//throw new Exception("test exception");
		NewEntity ent = new NewEntity("dfdf", "dsdsd");
		ent.setId(7L);
		Transaction tx = null;
		Long eId = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();

			eId = (Long) session.save(ent);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			log.error("failed sayPlainTextHello", e);
		}
		log.trace(String.format("New Entity created id = %d", eId));

		return ent;
	}

	@GET
	@Path("/{parameter}")
	public Response responseMsg( @PathParam("parameter") String parameter,
	                             @DefaultValue("Nothing to say") @QueryParam("value") String value) {

		String output = "Hello from: " + parameter + " : " + value;
		return Response.status(200).entity(output).build();
	}


	// This method is called if XML is request
	@GET
	@Produces(MediaType.TEXT_XML)
	public String sayXMLHello() {

		return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
	}

	// This method is called if HTML is request
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return "<html> " + "<title>" + "Hello Jersey" + "</title>"
				       + "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
	}
}