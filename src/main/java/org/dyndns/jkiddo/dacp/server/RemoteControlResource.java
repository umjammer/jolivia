package org.dyndns.jkiddo.dacp.server;

import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;
import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.dyndns.jkiddo.Jolivia;
import org.dyndns.jkiddo.NotImplementedException;
import org.dyndns.jkiddo.dmap.MDNSResource;

import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class RemoteControlResource extends MDNSResource implements IRemoteControlResource
{
	public static final String DACP_SERVER_PORT_NAME = "DACP_SERVER_PORT_NAME";

	@Inject
	public RemoteControlResource(JmmDNS mDNS, @Named(DACP_SERVER_PORT_NAME) Integer port) throws IOException
	{
		super(mDNS, port);
	}

	@Override
	@GET
	@Path("login")
	public Response login(@Context HttpServletRequest httpServletRequest, @QueryParam("pairing-guid") final String guid)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("logout")
	public Response logout(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/pause")
	public String pause(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/stop")
	public String stop(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/playpause")
	public String playpause(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/nextitem")
	public String nextitem(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/previtem")
	public String previtem(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/playlist")
	public String playlist(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/setproperty")
	public String setproperty(@Context UriInfo uriInfo, @QueryParam("dmcp.volume") String dmcpVolume, @QueryParam("dacp.playingtime") String dacpPlayingtime, @QueryParam("dacp.shufflestate") String dacpShufflestate, @QueryParam("dacp.repeatstate") String dacpRepeatstate, @QueryParam("session-id") long session_id)
	{
		MultivaluedMap<String, String> map = uriInfo.getQueryParameters();
		map.get("dmcp.volume");
		map.get("dacp.playingtime");
		map.get("dacp.shufflestate");
		map.get("dacp.repeatstate");
		map.get("dacp.userrating");
		map.get("song-spec");
		map.get("speaker-id");
		map.get("include-speaker-id");
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/getproperty")
	public String getproperty(@Context UriInfo uriInfo, @QueryParam("properties") String properties, @QueryParam("session-id") long session_id)
	{
		MultivaluedMap<String, String> map = uriInfo.getQueryParameters();
		map.get("properties");
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/playstatusupdate")
	public String playstatusupdate(@QueryParam("revision-number") long revisionNumber, @QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/cue")
	public String cue(@QueryParam("commmand") String command, @QueryParam("query") String query, @QueryParam("index") String index, @QueryParam("sort") String sort, @QueryParam("session-id") long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/getspeakers")
	public String getspeakers(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/setspeakers")
	public String setspeakers(@QueryParam("speaker-id") final String speaker_id, @QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("/ctrl-int/1/playspec")
	public String playspec(@QueryParam("playlist-spec") final String playlist_spec, @QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	protected ServiceInfo getServiceToRegister()
	{
		String hash = Integer.toHexString(hostname.hashCode()).toUpperCase();
		hash = (hash + hash).substring(0, 13);

		final HashMap<String, String> records = new HashMap<String, String>();
		records.put("CtlN", Jolivia.name + " on " + hostname);
		records.put("OSsi", "0x1F6");
		records.put("Ver", "131073");
		records.put("txtvers", "1");
		records.put("DvTy", "iTunes(JKiddo Inc)");
		records.put("DvSv", "2049");
		records.put("DbId", hash);

		return ServiceInfo.create(TOUCH_ABLE_TYPE, hash, port, 0, 0, records);
	}

	@Override
	@GET
	@Path("/ctrl-int/1/nowplayingartwork")
	public
	String nowplayingartwork(@QueryParam("mw") String mw, @QueryParam("mh") String mh, @QueryParam("session-id") long session_id)
	{
		throw new NotImplementedException();
	}
}