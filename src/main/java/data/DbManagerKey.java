/**
 *   Copyright 2018 Pascal Ammon, Gabriel Wyss
 * 
 * 	 Implementation eines anonymen Mobility Pricing Systems auf Basis eines Gruppensignaturschemas
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This class stores the data related to the group manager key (gsmk) in the database of the authority.
 */

package data;

import java.io.Serializable;
import java.math.BigInteger;

import keys.ManagerKey;

public class DbManagerKey implements ManagerKey, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer managerKeyId;
	private BigInteger Xg;
	private BigInteger p;
	private BigInteger q;

	public BigInteger getXg() {
		return Xg;
	}

	public void setXg(BigInteger xg) {
		Xg = xg;
	}

	public BigInteger getP() {
		return p;
	}

	public void setP(BigInteger p) {
		this.p = p;
	}

	public BigInteger getQ() {
		return q;
	}

	public void setQ(BigInteger q) {
		this.q = q;
	}

	public Integer getManagerKeyId() {
		return managerKeyId;
	}

	public void setManagerKeyId(Integer managerKeyId) {
		this.managerKeyId = managerKeyId;
	}

}
