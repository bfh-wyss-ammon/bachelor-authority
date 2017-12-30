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
 * This class stores the data that forms the public key of a signature group in the database of the authority.
 */

package data;

import java.io.Serializable;
import java.math.BigInteger;

import com.google.gson.annotations.Expose;

import interfaces.HashValue;
import keys.PublicKey;

public class DbPublicKey implements PublicKey, Serializable {
	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

	private Integer publicKeyId;

	@Expose
	@HashValue
	private BigInteger n;
	@Expose
	@HashValue
	private BigInteger a;
	@Expose
	@HashValue
	private BigInteger g;
	@Expose
	@HashValue
	private BigInteger h;
	@Expose
	@HashValue
	private BigInteger w;
	@Expose
	@HashValue
	private BigInteger bigQ;
	@Expose
	@HashValue
	private BigInteger bigP;
	@Expose
	@HashValue
	private BigInteger bigF;
	@Expose
	@HashValue
	private BigInteger bigG;
	@Expose
	@HashValue
	private BigInteger bigH;

	public BigInteger getN() {
		return n;
	}

	public void setN(BigInteger n) {
		this.n = n;
	}

	public BigInteger getA() {
		return a;
	}

	public void setA(BigInteger a) {
		this.a = a;
	}

	public BigInteger getG() {
		return g;
	}

	public void setG(BigInteger g) {
		this.g = g;
	}

	public BigInteger getH() {
		return h;
	}

	public void setH(BigInteger h) {
		this.h = h;
	}

	public BigInteger getW() {
		return w;
	}

	public void setW(BigInteger w) {
		this.w = w;
	}

	public BigInteger getBigQ() {
		return bigQ;
	}

	public void setBigQ(BigInteger bigQ) {
		this.bigQ = bigQ;
	}

	public BigInteger getBigP() {
		return bigP;
	}

	public void setBigP(BigInteger bigP) {
		this.bigP = bigP;
	}

	public BigInteger getBigF() {
		return bigF;
	}

	public void setBigF(BigInteger bigF) {
		this.bigF = bigF;
	}

	public BigInteger getBigG() {
		return bigG;
	}

	public void setBigG(BigInteger bigG) {
		this.bigG = bigG;
	}

	public BigInteger getBigH() {
		return bigH;
	}

	public void setBigH(BigInteger bigH) {
		this.bigH = bigH;
	}

	public Integer getPublicKeyId() {
		return publicKeyId;
	}

	public void setPublicKeyId(Integer publicKeyId) {
		this.publicKeyId = publicKeyId;
	}

}
