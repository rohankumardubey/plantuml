/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 */
package net.sourceforge.plantuml.nwdiag.legacy;

public class Footprint {

	private final int min;
	private final int max;

	public Footprint(int min, int max) {
		if (max < min) {
			throw new IllegalArgumentException();
		}
		assert max >= min;
		this.min = min;
		this.max = max;
	}

	@Override
	public String toString() {
		return "" + min + " -> " + max;
	}

	public Footprint intersection(Footprint other) {
		if (this.max < other.min) {
			return null;
		}
		if (this.min > other.max) {
			return null;
		}
		return new Footprint(Math.max(this.min, other.min), Math.min(this.max, other.max));
	}

	public final int getMin() {
		return min;
	}

	public final int getMax() {
		return max;
	}

}
