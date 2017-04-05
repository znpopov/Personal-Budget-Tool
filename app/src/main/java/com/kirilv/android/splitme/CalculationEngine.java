package com.kirilv.android.splitme;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.kirilv.android.splitme.model.Section;
import com.kirilv.android.splitme.model.User;

/**
 * 
 * @author kirilv
 */
public class CalculationEngine {

	private static final CalculationEngine instance = new CalculationEngine();
	private static final DecimalFormat formatter = new DecimalFormat("0.00");

	public static final int PERCENTAGE_BASE = 10000; // 100%
	public static final int PERCENTAGE_DIVIDER = 100;

	private CalculationEngine() {
	}

	public static CalculationEngine getInstance() {
		return instance;
	}

	private void reset(int percentageTotal, List<User> list) {
		int amount = list.size();
		if (amount > percentageTotal) {
			performProportionalDistribution(percentageTotal, list, 1);
			percentageTotal = 0;
		} else {
			performProportionalDistribution(amount, list, 1);
			percentageTotal -= amount;
		}
		if (percentageTotal > 0) {
			reset(percentageTotal, list);
		}
	}

	public void resetPercentages(List<User> list) {
		int unit = PERCENTAGE_BASE / list.size();
		int remainder = PERCENTAGE_BASE % list.size();
		for (User user : list) {
			user.setPercentage(unit);
		}
		reset(remainder, list);
	}

	/* Removes/Adds the unit from/to each member of the list, based on the sign */
	private void performProportionalDistribution(int percentageTotal,
			List<User> list, int sign) {
		final int unit = 1;
		int result = percentageTotal;
		for (User user : list) {
			if (result == 0) {
				break;
			}
			if (result - unit > 0) {
				user.setPercentage(user.getPercentage() + (sign * unit));
				result -= unit;
			} else {
				user.setPercentage(user.getPercentage() + (sign * result));
				result = 0;
			}
		}
	}

	public void recalculatePercentages(Section section, int newUser,
			int newPercentage) {
		final List<User> listOfNonZeros = new ArrayList<User>();
		final List<User> listOfZeros = new ArrayList<User>();
		final List<User> list = section.getUserList();
		final User targetUser = list.get(newUser);
		/* What total percentage we should add/remove from all other candidates */
		int percentageToBeDistributed = (int) Math.abs(newPercentage
				- targetUser.getPercentage());
		/* + means add to all, - means remove from all */
		int sign = newPercentage > targetUser.getPercentage() ? -1 : 1;
		/* Set the new percentage for the user we modify */
		targetUser.setPercentage(newPercentage);

		/* Fill the map and the listOfZeroKeys the first time */
		recalculateObjects(list, newUser, listOfNonZeros, listOfZeros);

		if (percentageToBeDistributed > 0) {
			do {
				if (!listOfNonZeros.isEmpty()) {
					for (User u : listOfNonZeros) {
						if (percentageToBeDistributed > 0) {
							u.setPercentage(u.getPercentage() + (sign * 1));
							percentageToBeDistributed--;
						}
					}
					recalculateObjects(list, newUser, listOfNonZeros,
							listOfZeros);
				} else {
					// Always add +1 to the list of zeros
					int amount = listOfZeros.size();
					if (amount > percentageToBeDistributed) {
						performProportionalDistribution(
								percentageToBeDistributed, listOfZeros, 1);
						percentageToBeDistributed = 0;
					} else {
						performProportionalDistribution(amount, listOfZeros, 1);
						percentageToBeDistributed -= amount;
					}
				}
			} while (percentageToBeDistributed > 0);
		}
	}

	private void recalculateObjects(final List<User> list, final int newUser,
			final List<User> listOfNonZeroKeys, final List<User> listOfZeroKeys) {
		listOfNonZeroKeys.clear();
		listOfZeroKeys.clear();
		for (int i = 0; i < list.size(); i++) {
			User user = list.get(i);
			if (i != newUser && !user.isLocked()) {
				int key = user.getPercentage();
				if (key > 0) {
					listOfNonZeroKeys.add(user);
				} else {
					listOfZeroKeys.add(user);
				}
			}
		}
	}

	public static float getPrice(int percentage, float price) {
		float p = (float) percentage / PERCENTAGE_DIVIDER;
		return Float.valueOf(getFormatterObject((p * price) / 100));
	}
	
	public static String getFormatterObject(Object obj) {
		String value = formatter.format(obj);
		value = value.replaceAll("\\,", "\\.");
		return value;
		
	}
}
