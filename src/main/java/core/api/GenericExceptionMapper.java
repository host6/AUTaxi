package core.api;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Admin on 13.11.2015.
 */

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	static final Logger log = LogManager.getLogger(GenericExceptionMapper.class);

	@Context
	private UriInfo uriInfo;

	@Context
	private ServletContext servletContext;

	public Response toResponse(Throwable ex) {

		ErrorMessage errorMessage = new ErrorMessage();
		setHttpStatus(ex, errorMessage);
		errorMessage.setCode(ApiConstants.GENERIC_API_ERROR_CODE);
		errorMessage.setMessage(ex.getMessage());
		StringWriter errorStackTrace = new StringWriter();
		ex.printStackTrace(new PrintWriter(errorStackTrace));
		errorMessage.setDeveloperMessage(errorStackTrace.toString());

		log.error(String.format("Unhandled exception, %s", uriInfo.getRequestUri().toString()), ex);

		return Response.status(errorMessage.getStatus())
				       .entity(errorMessage)
				       .type(MediaType.APPLICATION_JSON)
				       .build();
	}

	private void setHttpStatus(Throwable ex, ErrorMessage errorMessage) {
		if(ex instanceof WebApplicationException) { //NICE way to combine both of methods, say it in the blog
			errorMessage.setStatus(((WebApplicationException)ex).getResponse().getStatus());
		} else {
			errorMessage.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()); //defaults to internal server error 500
		}
	}
}
