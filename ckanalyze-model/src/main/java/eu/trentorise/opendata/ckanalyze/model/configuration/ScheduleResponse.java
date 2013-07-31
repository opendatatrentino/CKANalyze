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

package eu.trentorise.opendata.ckanalyze.model.configuration;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * This class represents a schedule response. 
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 *
 */
@XmlRootElement
public class ScheduleResponse {
	private Boolean alreadyScheduled;
	private Date lastProcessed;
	public Boolean getAlreadyScheduled() {
		return alreadyScheduled;
	}
	public void setAlreadyScheduled(Boolean alreadyScheduled) {
		this.alreadyScheduled = alreadyScheduled;
	}
	/**
	 * 
	 * @return the last time the dataset was processed (if it is scheduled
	 */
	public Date getLastProcessed() {
		return lastProcessed;
	}
	public void setLastProcessed(Date lastProcessed) {
		this.lastProcessed = lastProcessed;
	}
	
}
