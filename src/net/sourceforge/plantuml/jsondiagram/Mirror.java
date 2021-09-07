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
 * 
 */
package net.sourceforge.plantuml.jsondiagram;

import java.awt.geom.Point2D;

public class Mirror {

	private final double max;

	public Mirror(double max) {
		this.max = max;
	}

	public double inv(double v) {
		if (v < 0 || v > max) {
			System.err.println("BAD VALUE IN Mirror");
		}
		return max - v;
	}

	public Point2D invAndXYSwitch(Point2D pt) {
		final double x = inv(pt.getY());
		final double y = pt.getX();
		return new Point2D.Double(x, y);
	}

	public Point2D invGit(Point2D pt) {
		final double x = pt.getX();
		final double y = inv(pt.getY());
		return new Point2D.Double(x, y);
	}

}