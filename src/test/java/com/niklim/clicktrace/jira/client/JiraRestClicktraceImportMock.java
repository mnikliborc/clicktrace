package com.niklim.clicktrace.jira.client;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.utils.Base64;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("/clicktrace")
public class JiraRestClicktraceImportMock {
	static final String FAKE_STREAM = "FAKE_STREAM";
	public static final String ERROR_MSG = "Error message";

	@GET
	@Path("/test")
	public Response test() {
		return Response.ok("It's working!").build();
	}

	@GET
	@Path("/import/{issueKey}/{sessionName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkSessionExist(@PathParam("issueKey") String issueKey,
			@PathParam("sessionName") String sessionName) {
		if ("error".equals(sessionName)) {
			return Response.ok("{status=\"" + ExportStatus.ERROR + "\", msg=\"" + ERROR_MSG + "\"}").build();
		} else if ("notlogged".equals(sessionName)) {
			return Response.status(Status.UNAUTHORIZED).entity("{}").build();
		} else if ("existing".equals(sessionName)) {
			return Response.ok("{status=\"" + ExportStatus.SESSION_EXISTS + "\"}").build();
		} else {
			return Response.ok("{status=\"" + ExportStatus.NO_SESSION + "\"}").build();
		}
	}

	@POST
	@Path("/import/{issueKey}/{sessionName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importSession(@PathParam("issueKey") String issueKey, @PathParam("sessionName") String sessionName,
			String json) {
		if ("error".equals(sessionName)) {
			return Response.ok("{status=\"" + ExportStatus.ERROR + "\", msg=\"" + ERROR_MSG + "\"}").build();
		} else {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(json);
				String stream = jsonObject.getString(JiraRestClicktraceClient.JSON_CLICKTRACE_STREAM_FIELD_NAME);
				if (!FAKE_STREAM.equals(stream)) {
					decompress(sessionName, stream);
				}
				return Response.ok("{status=\"" + ExportStatus.OK + "\"}").build();
			} catch (JSONException e) {
				e.printStackTrace();
				return Response.ok("{status=\"" + ExportStatus.ERROR + "\", msg=\"" + e.getMessage() + "\"}").build();
			} catch (IOException e) {
				e.printStackTrace();
				return Response.ok("{status=\"" + ExportStatus.ERROR + "\", msg=\"" + e.getMessage() + "\"}").build();
			}
		}
	}

	private void decompress(String sessionName, String stream) throws IOException {
		try {
			byte[] bytes = Base64.decode(stream);
		} catch (Base64DecodingException e) {
			e.printStackTrace();
		}

	}
}
