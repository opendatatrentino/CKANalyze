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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import eu.trentorise.opendata.ckanalyze.client.exceptions.CkanalyzeClientLocalException;
import eu.trentorise.opendata.ckanalyze.client.exceptions.CkanalyzeClientRemoteException;
import eu.trentorise.opendata.ckanalyze.model.JSONIZEDException;
import eu.trentorise.opendata.ckanalyze.model.Status;
import eu.trentorise.opendata.ckanalyze.model.catalog.CatalogueStat;
import eu.trentorise.opendata.ckanalyze.model.configuration.ScheduleResponse;
import eu.trentorise.opendata.ckanalyze.model.resources.ResourceStat;

public class CkanalyzeClient {
	private String basePath;
	private Client client;

	public CkanalyzeClient(String basePath) {
		super();
		this.basePath = basePath + "/rest".replaceAll("//", "/");
		this.client = Client.create();
	}

	public CatalogueStat getCatalogueStat(String catalogueName)
			throws CkanalyzeClientLocalException, CkanalyzeClientRemoteException {
		CatalogueStat retval = null;
		try {
			if (catalogueName.isEmpty()) {
				emptyCatalogue();
			}
			String par = URLEncoder.encode(catalogueName, "UTF-8");
			String url = basePath + "/stats?catalogue=" + par;
			WebResource resource = client.resource(url);
			ClientResponse response = resource.accept("application/json").get(
					ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new CkanalyzeClientRemoteException(response.getEntity(
						JSONIZEDException.class).getErrorDescription());
			} else {
				retval = response.getEntity(CatalogueStat.class);
			}
		} catch (UnsupportedEncodingException e) {
			unsupportedEncoding(e);
		}
		return retval;
	}

	public ResourceStat getResourceStat(String catalogueName, String resourceId)
			throws CkanalyzeClientLocalException, CkanalyzeClientRemoteException {
		ResourceStat retval = null;
		try {
			if (catalogueName.isEmpty()) {
				emptyCatalogue();
			}
			if (resourceId.isEmpty()) {
				emptyResource();
			}
			String catEsc = URLEncoder.encode(catalogueName, "UTF-8");
			String residEsc = URLEncoder.encode(resourceId, "UTF-8");
			String url = basePath + "/resource-stats?catalogue=" + catEsc
					+ "&idResource=" + residEsc;
			WebResource resource = client.resource(url);
			ClientResponse response = resource.accept("application/json").get(
					ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new CkanalyzeClientRemoteException(response.getEntity(
						JSONIZEDException.class).getErrorDescription());
			} else {
				retval = response.getEntity(ResourceStat.class);
			}
		} catch (UnsupportedEncodingException e) {
			unsupportedEncoding(e);
		}
		return retval;
	}
	
	public boolean isScheduledCatalogue(String catalogueName)
	{
		Status retval;
		try {
			if (catalogueName.isEmpty()) {
				emptyCatalogue();
			}
			String par = URLEncoder.encode(catalogueName, "UTF-8");
			String url = basePath + "/is-available?catalogue=" + par;
			WebResource resource = client.resource(url);
			ClientResponse response = resource.accept("application/json").get(
					ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new CkanalyzeClientRemoteException(response.getEntity(
						JSONIZEDException.class).getErrorDescription());
			} else {
				retval = response.getEntity(Status.class);
			}
			return retval.getStatus();
		} catch (UnsupportedEncodingException e) {
			unsupportedEncoding(e);
			return false;
		}
	}
	
	public ScheduleResponse scheduleCatalog(String catalogueName)
	{
		ScheduleResponse retval = null;
		try {
			if (catalogueName.isEmpty()) {
				emptyCatalogue();
			}
			String par = URLEncoder.encode(catalogueName, "UTF-8");
			String url = basePath + "/schedule-catalogue?catalogue=" + par;
			WebResource resource = client.resource(url);
			ClientResponse response = resource.accept("application/json").get(
					ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new CkanalyzeClientRemoteException(response.getEntity(
						JSONIZEDException.class).getErrorDescription());
			} else {
				retval = response.getEntity(ScheduleResponse.class);
			}
		} catch (UnsupportedEncodingException e) {
			unsupportedEncoding(e);
		}
		return retval;
	}
	
	private void emptyCatalogue()
	{
		throw new CkanalyzeClientLocalException(
				"Empty parameter catalogueName");
	}
	
	private void emptyResource()
	{
		throw new CkanalyzeClientLocalException(
				"Empty parameter resourceId");
	}
	
	private void unsupportedEncoding(Throwable e)
	{
		throw new CkanalyzeClientLocalException(
				"Unsupported parameter encoding", e);
	}
}
