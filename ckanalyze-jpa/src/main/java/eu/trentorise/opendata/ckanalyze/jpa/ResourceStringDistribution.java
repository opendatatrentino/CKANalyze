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
package eu.trentorise.opendata.ckanalyze.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
/**
 * 
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 *
 */
@Entity
public class ResourceStringDistribution {
	@Id
	@GeneratedValue
	private long resourceStringDistributionId;
	@ManyToOne
	@JoinColumn(name="resourceId")
	private Resource resource;
	@Column
	private long length;
	@Column
	private long freq;

	public long getResourceStringDistributionId() {
		return resourceStringDistributionId;
	}
	public void setResourceStringDistributionId(long resourceStringDistributionId) {
		this.resourceStringDistributionId = resourceStringDistributionId;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	public long getFreq() {
		return freq;
	}
	public void setFreq(long freq) {
		this.freq = freq;
	}
	
	
}
