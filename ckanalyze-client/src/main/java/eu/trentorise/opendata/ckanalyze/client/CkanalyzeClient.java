/**
 * *****************************************************************************
 * Copyright 2012-2013 Trento Rise (www.trentorise.eu/)
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License (LGPL)
 * version 2.1 which accompanies this distribution, and is available at
 *
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 *******************************************************************************
 */

package eu.trentorise.opendata.ckanalyze.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import eu.trentorise.opendata.ckanalyze.client.exceptions.CkanalyzeClientLocalException;
import eu.trentorise.opendata.ckanalyze.client.exceptions.CkanalyzeClientRemoteException;
import eu.trentorise.opendata.ckanalyze.client.exceptions.CkanalyzeClientResourceNotFoundException;
import eu.trentorise.opendata.ckanalyze.model.JSONIZEDException;
import eu.trentorise.opendata.ckanalyze.model.Status;
import eu.trentorise.opendata.ckanalyze.model.catalog.CatalogStats;
import eu.trentorise.opendata.ckanalyze.model.configuration.ScheduleResponse;
import eu.trentorise.opendata.ckanalyze.model.resources.ResourceStats;

/**
 * Client main class
 * 
 * @author Alberto Zanella <a.zanella@trentorise.eu> Last modified by azanella
 *         On 16/set/2013
 */
public class CkanalyzeClient {
	private String basePath;
	private Client client;
	private static final String JSON = "application/json";
	private static final String RES_NOT_FOUND = "resource id not found";
	private static final String UTF8 = "UTF-8";
	private static final int REQUEST_OK = 200;

	/**
	 * 
	 * @param basePath
	 *            -- the baseURL (domain) i.e.
	 *            http://localhost:8080/ckanalyze-web
	 */
	public CkanalyzeClient(String basePath) {
		super();
		this.basePath = basePath;
		if(basePath.endsWith("/"))
		{
			this.basePath = basePath.substring(0, basePath.length()-2);
		}
		this.basePath = this.basePath + "/0.2/rest".replaceAll("//", "/");
		ClientConfig cc = new DefaultClientConfig();
		cc.getClasses().add(JacksonJsonProvider.class);
		this.client = Client.create(cc);
	}

	private void closeClient() {
		if (client != null) {
			client.destroy();
			client = null;
		}
	}

	private void openClient() {
		if (client == null) {
			client = Client.create();
		}
	}

	private void throwRemoteException(ClientResponse response) {
		closeClient();
		String rspstr = response.getEntity(String.class);
		try {
			JSONIZEDException exc = new ObjectMapper().readValue(rspstr,
					JSONIZEDException.class);
			throw new CkanalyzeClientRemoteException(exc.getErrorDescription());
		} catch (JsonMappingException e) {
			closeClient();
			throw new CkanalyzeClientRemoteException(rspstr);
		} catch (JsonParseException e) {
			closeClient();
			throw new CkanalyzeClientRemoteException(rspstr);
		} catch (IOException e) {
			closeClient();
			throw new CkanalyzeClientRemoteException(rspstr);
		}
	}

	/**
	 * Provide catalog statistics
	 * 
	 * @param catalogName
	 *            -- name of the catalog (URL)
	 * @return object containing catalog statistics or null if
	 *         UnsupportedEncodingException is thrown
	 * 
	 * 
	 */
	public CatalogStats getCatalogStats(String catalogName) {
		openClient();
		CatalogStats retval = null;
		if (catalogName == null) {
			closeClient();
			throw new CkanalyzeClientLocalException("Null catalog name");
		}
		try {
			if (catalogName.isEmpty()) {
				emptyCatalog();
			}
			String par = URLEncoder.encode(catalogName, UTF8);
			String url = basePath + "/stats?catalog=" + par;
			WebResource resource = client.resource(url);
			ClientResponse response = resource.accept(JSON).get(
					ClientResponse.class);
			if (response.getStatus() != REQUEST_OK) {
				throwRemoteException(response);
			} else {
				retval = response.getEntity(CatalogStats.class);
			}
		} catch (UnsupportedEncodingException e) {
			closeClient();
			unsupportedEncoding(e);
		}
		closeClient();
		return retval;
	}

	/**
	 * Provide resource statistics . This method could throw specific
	 * CkanResourceNotFoundException
	 * 
	 * @param catalogName
	 *            -- name of the catalog (URL)
	 * @param resourceId
	 *            -- CKAN-Id of the required resource
	 * @return an object containing Resource statistics or null if
	 *         UnsupportedEncodingException is thrown
	 * 
	 * 
	 */
	public ResourceStats getResourceStats(String catalogName, String resourceId) {
		openClient();
		if (catalogName == null) {
			closeClient();
			throw new CkanalyzeClientLocalException("Null catalog name");
		}
		if (resourceId == null) {
			closeClient();
			throw new CkanalyzeClientLocalException("Null resourceID");
		}
		ResourceStats retval = null;
		try {
			if (catalogName.isEmpty()) {
				emptyCatalog();
			}
			if (resourceId.isEmpty()) {
				emptyResource();
			}
			String catEsc = URLEncoder.encode(catalogName, UTF8);
			String residEsc = URLEncoder.encode(resourceId, UTF8);
			String url = basePath + "/resource-stats?catalog=" + catEsc
					+ "&idResource=" + residEsc;
			WebResource resource = client.resource(url);
			ClientResponse response = resource.accept(JSON).get(
					ClientResponse.class);
			if (response.getStatus() != REQUEST_OK) {
				String rspstr = response.getEntity(String.class);
				try {
					String json;
					json = new ObjectMapper().readValue(rspstr,
							JSONIZEDException.class).getErrorDescription();
					if (json.contains(RES_NOT_FOUND)) {
						closeClient();
						throw new CkanalyzeClientResourceNotFoundException();
					}
					closeClient();
					throw new CkanalyzeClientRemoteException(json);
				} catch (JsonMappingException e) {
					closeClient();
					throw new CkanalyzeClientRemoteException(rspstr);
				} catch (JsonParseException e) {
					closeClient();
					throw new CkanalyzeClientRemoteException(rspstr);
				} catch (IOException e) {
					closeClient();
					throw new CkanalyzeClientRemoteException(rspstr);
				}
			} else {
				retval = response.getEntity(ResourceStats.class);
			}
		} catch (UnsupportedEncodingException e) {
			closeClient();
			unsupportedEncoding(e);
		}
		closeClient();
		return retval;
	}

	/**
	 * Provide information about scheduled catalogs
	 * 
	 * @param catalogName
	 *            -- catalog name (URL)
	 * @return true if the specified catalog is already scheduled, false
	 *         otherwise.
	 */
	public boolean isScheduledCatalog(String catalogName) {
		openClient();
		Status retval;
		try {
			if (catalogName.isEmpty()) {
				emptyCatalog();
			}
			String par = URLEncoder.encode(catalogName, UTF8);
			String url = basePath + "/is-available?catalog=" + par;
			WebResource resource = client.resource(url);
			ClientResponse response = resource.accept(JSON).get(
					ClientResponse.class);
			if (response.getStatus() != REQUEST_OK) {
				throwRemoteException(response);
				retval = null;
			} else {
				retval = response.getEntity(Status.class);
			}
			closeClient();
			return retval.getStatus();
		} catch (UnsupportedEncodingException e) {
			unsupportedEncoding(e);
			closeClient();
			return false;
		}
	}

	/**
	 * Schedule a new catalog
	 * 
	 * @param catalogName
	 * @return
	 */
	public ScheduleResponse scheduleCatalog(String catalogName) {
		openClient();
		ScheduleResponse retval = null;
		try {
			if (catalogName.isEmpty()) {
				emptyCatalog();
			}
			String par = URLEncoder.encode(catalogName, UTF8);
			String url = basePath + "/schedule-catalog?catalog=" + par;
			WebResource resource = client.resource(url);
			ClientResponse response = resource.accept(JSON).get(
					ClientResponse.class);
			if (response.getStatus() != REQUEST_OK) {
				throwRemoteException(response);
			} else {
				retval = response.getEntity(ScheduleResponse.class);
			}
		} catch (UnsupportedEncodingException e) {
			closeClient();
			unsupportedEncoding(e);
		}
		closeClient();
		return retval;
	}

	private void emptyCatalog() {
		closeClient();
		throw new CkanalyzeClientLocalException("Empty parameter catalogName");
	}

	private void emptyResource() {
		closeClient();
		throw new CkanalyzeClientLocalException("Empty parameter resourceId");
	}

	private void unsupportedEncoding(Throwable e) {
		closeClient();
		throw new CkanalyzeClientLocalException(
				"Unsupported parameter encoding", e);
	}
}
