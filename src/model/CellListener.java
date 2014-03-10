package model;

import java.util.Date;

public abstract class CellListener {
	/**
	 * Inform client user has clicked on a date
	 * 
	 * @param date
	 * @param view
	 */
	public abstract void onSelectDate(Date date);
	public abstract void onLongSelectDate(Date date);

}
