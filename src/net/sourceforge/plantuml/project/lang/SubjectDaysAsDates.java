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
package net.sourceforge.plantuml.project.lang;

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.DaysAsDates;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class SubjectDaysAsDates implements Subject {

	public IRegex toRegex() {
		return new RegexOr(regexTo(), regexAnd(), regexThen());

	}

	private IRegex regexTo() {
		return new RegexConcat( //
				new RegexLeaf("YEAR1", "([\\d]{4})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("MONTH1", "([\\d]{1,2})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("DAY1", "([\\d]{1,2})"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("to"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("YEAR2", "([\\d]{4})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("MONTH2", "([\\d]{1,2})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("DAY2", "([\\d]{1,2})") //
		);
	}

	private IRegex regexAnd() {
		return new RegexConcat( //
				new RegexLeaf("YEAR3", "([\\d]{4})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("MONTH3", "([\\d]{1,2})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("DAY3", "([\\d]{1,2})"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("and"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("COUNT_AND", "([\\d]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("days?") //

		);
	}

	private IRegex regexThen() {
		return new RegexConcat( //
				new RegexLeaf("then"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("COUNT_THEN", "([\\d]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("days?") //

		);
	}

	public Failable<DaysAsDates> getMe(GanttDiagram project, RegexResult arg) {
		final String countAnd = arg.get("COUNT_AND", 0);
		if (countAnd != null) {
			final Day date3 = getDate(arg, "3");
			final int nb = Integer.parseInt(countAnd);
			return Failable.ok(new DaysAsDates(project, date3, nb));
		}
		final String countThen = arg.get("COUNT_THEN", 0);
		if (countThen != null) {
			final Day date3 = project.getThenDate();
			final int nb = Integer.parseInt(countThen);
			return Failable.ok(new DaysAsDates(project, date3, nb));
		}
		final Day date1 = getDate(arg, "1");
		final Day date2 = getDate(arg, "2");
		return Failable.ok(new DaysAsDates(date1, date2));
	}

	private Day getDate(RegexResult arg, String suffix) {
		final int day = Integer.parseInt(arg.get("DAY" + suffix, 0));
		final int month = Integer.parseInt(arg.get("MONTH" + suffix, 0));
		final int year = Integer.parseInt(arg.get("YEAR" + suffix, 0));
		return Day.create(year, month, day);
	}

	public Collection<? extends SentenceSimple> getSentences() {
		return Arrays.asList(new Close(), new Open(), new InColor(), new Named());
	}

	class Close extends SentenceSimple {

		public Close() {
			super(SubjectDaysAsDates.this, Verbs.isOrAre(), new ComplementClose());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			for (Day d : (DaysAsDates) subject) {
				project.closeDayAsDate(d);
			}
			return CommandExecutionResult.ok();

		}
	}

	class Open extends SentenceSimple {

		public Open() {
			super(SubjectDaysAsDates.this, Verbs.isOrAre(), new ComplementOpen());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			for (Day d : (DaysAsDates) subject) {
				project.openDayAsDate(d);
			}
			return CommandExecutionResult.ok();

		}

	}

	class InColor extends SentenceSimple {

		public InColor() {
			super(SubjectDaysAsDates.this, Verbs.isOrAre(), new ComplementInColors2());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final HColor color = ((CenterBorderColor) complement).getCenter();
			for (Day d : (DaysAsDates) subject) {
				project.colorDay(d, color);
			}
			return CommandExecutionResult.ok();

		}

	}

	class Named extends SentenceSimple {

		public Named() {
			super(SubjectDaysAsDates.this, Verbs.isOrAreNamed(), new ComplementNamed());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final String name = (String) complement;
			final DaysAsDates days = (DaysAsDates) subject;
			for (Day d : days) {
				project.nameDay(d, name);
			}
			return CommandExecutionResult.ok();
		}

	}

}
