/*
    STACK360 - Web-based Business Management System
    Copyright (C) 2024 Arahant LLC

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses.
*/

/*
*/



package com.arahant.services.main;

import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;

/**
 *
 *
 *
 */
public class SavePreferencesInput extends TransmitInputBase {

	// The int passed in is unsigned, but since Java does not support unsigned ints and the number will not
	// exceed 16,777,216 I use a normal int.
	@Validation(required = false)
	private int presetColorThemeChoice;
	@Validation(required = false)
	private int usePresetColorTheme;
	@Validation(required = false)
	private int backgroundColor1;
	@Validation(required = false)
	private int backgroundColor2;
	@Validation(required = false)
	private int unhighlightedButtonColor;
	@Validation(required = false)
	private int highlightedButtonColor;
	@Validation(required = false)
	private int unhighlightedTextColor;
	@Validation(required = false)
	private int highlightedTextColor;
	@Validation(required = false)
	private int menuBarShowAnimationStyle;
	@Validation(required = false)
	private int menuBarHideAnimationStyle;
	@Validation(required = false)
	private int historyBarShowAnimationStyle;
	@Validation(required = false)
	private int historyBarHideAnimationStyle;
	@Validation(required = false)
	private int layout;

	public int getPresetColorThemeChoice() {
		return presetColorThemeChoice;
	}

	public void setPresetColorThemeChoice(int presetColorThemeChoice) {
		this.presetColorThemeChoice = presetColorThemeChoice;
	}

	public int getUsePresetColorTheme() {
		return usePresetColorTheme;
	}

	public void setUsePresetColorTheme(int usePresetColorTheme) {
		this.usePresetColorTheme = usePresetColorTheme;
	}

	public int getLayout() {
		return layout;
	}

	public void setLayout(final int layout) {
		this.layout = layout;
	}

	public int getMenuBarShowAnimationStyle() {
		return menuBarShowAnimationStyle;
	}

	public void setMenuBarShowAnimationStyle(final int menuBarShowAnimationStyle) {
		this.menuBarShowAnimationStyle = menuBarShowAnimationStyle;
	}

	public int getMenuBarHideAnimationStyle() {
		return menuBarHideAnimationStyle;
	}

	public void setMenuBarHideAnimationStyle(final int menuBarHideAnimationStyle) {
		this.menuBarHideAnimationStyle = menuBarHideAnimationStyle;
	}

	public int getHistoryBarShowAnimationStyle() {
		return historyBarShowAnimationStyle;
	}

	public void setHistoryBarShowAnimationStyle(final int historyBarShowAnimationStyle) {
		this.historyBarShowAnimationStyle = historyBarShowAnimationStyle;
	}

	public int getHistoryBarHideAnimationStyle() {
		return historyBarHideAnimationStyle;
	}

	public void setHistoryBarHideAnimationStyle(final int historyBarHideAnimationStyle) {
		this.historyBarHideAnimationStyle = historyBarHideAnimationStyle;
	}

	public int getBackgroundColor1() {
		return backgroundColor1;
	}

	public void setBackgroundColor1(int backgroundColor1) {
		this.backgroundColor1 = backgroundColor1;
	}

	public int getBackgroundColor2() {
		return backgroundColor2;
	}

	public void setBackgroundColor2(int backgroundColor2) {
		this.backgroundColor2 = backgroundColor2;
	}

	public int getUnhighlightedButtonColor() {
		return unhighlightedButtonColor;
	}

	public void setUnhighlightedButtonColor(int unhighlightedButtonColor) {
		this.unhighlightedButtonColor = unhighlightedButtonColor;
	}

	public int getHighlightedButtonColor() {
		return highlightedButtonColor;
	}

	public void setHighlightedButtonColor(int highlightedButtonColor) {
		this.highlightedButtonColor = highlightedButtonColor;
	}

	public int getUnhighlightedTextColor() {
		return unhighlightedTextColor;
	}

	public void setUnhighlightedTextColor(int unhighlightedTextColor) {
		this.unhighlightedTextColor = unhighlightedTextColor;
	}

	public int getHighlightedTextColor() {
		return highlightedTextColor;
	}

	public void setHighlightedTextColor(int highlightedTextColor) {
		this.highlightedTextColor = highlightedTextColor;
	}
}
