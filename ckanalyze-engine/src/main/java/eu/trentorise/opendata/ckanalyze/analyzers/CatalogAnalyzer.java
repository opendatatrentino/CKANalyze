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
package eu.trentorise.opendata.ckanalyze.analyzers;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import eu.trentorise.opendata.ckanalyze.jpa.CatalogStringDistribution;
import eu.trentorise.opendata.ckanalyze.managers.PersistencyManager;

/**
 * This class extracts statistics of the entire catalog and its analyze() method
 * should be called after all resources are already added to the database
 * 
 * @author Alberto Zanella <a.zanella@trentorise.eu> Last modified by azanella
 *         On 17/lug/2013
 * 
 */
public class CatalogAnalyzer {
	/**
	 * This method performs statistics on given catalog
	 * 
	 * @param catalog
	 *            -- catalog JPA object
	 */
	private List<CatalogStringDistribution> catalogStringDistribution;
	private double avgStringLength = 0;
	private double avgColumnCount = 0;
	private double avgRowCount = 0;

	public void analyze() {
		computeStringDistribution();
		computeAvgStringLength();
		computeRowsAndColumnAvgCount();
	}

	private void computeStringDistribution() {
		Session ss = PersistencyManager.getSessionFactory().openSession();
		String hql = "select new CatalogStringDistribution(length,sum(freq)) from ResourceStringDistribution group by length";
		Query q = ss.createQuery(hql);
		@SuppressWarnings("unchecked")
		List<CatalogStringDistribution> clist = q.list();
		ss.close();
		catalogStringDistribution = clist;
	}

	private void computeAvgStringLength() {
		double sum = 0;
		double numString = 0;
		for (CatalogStringDistribution csd : catalogStringDistribution) {
			numString = (numString + csd.getFreq());
			sum = sum + ((csd.getLength() * csd.getFreq()));
		}
		avgStringLength = sum / numString;

	}

	private void computeRowsAndColumnAvgCount() {
		Session ss = PersistencyManager.getSessionFactory().openSession();
		String hql = "select avg(rowCount) from Resource";
		Query q = ss.createQuery(hql);
		if (!q.list().isEmpty()) {
			avgRowCount = (Double) q.list().get(0);
		}
		hql = "select avg(columnCount) from Resource";
		q = ss.createQuery(hql);
		if (!q.list().isEmpty())
		{
			avgColumnCount = (Double) q.list().get(0);
		}
		ss.close();
	}

	public double getAvgStringLength() {
		return avgStringLength;
	}

	public double getAvgColumnCount() {
		return avgColumnCount;
	}

	public double getAvgRowCount() {
		return avgRowCount;
	}

	public List<CatalogStringDistribution> getCatalogStringDistribution() {
		return catalogStringDistribution;
	}

}
