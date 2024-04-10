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


/**
 * 
 */
package com.arahant.services.main;

import com.arahant.services.TransmitReturnBase;

/**
 * 
 *
 *
 */
public class LoadPreferencesReturn extends TransmitReturnBase {

    private int layout;
    private int presetColorThemeChoice;
    private int usePresetColorTheme;
    private int backgroundColor1;
    private int backgroundColor2;
    private int unhighlightedButtonColor;
    private int highlightedButtonColor;
    private int unhighlightedTextColor;
    private int highlightedTextColor;
    private int menuBarShowAnimationStyle;
    private int menuBarHideAnimationStyle;
    private int historyBarShowAnimationStyle;
    private int historyBarHideAnimationStyle;
    private int inactiveUserMaxSeconds;
    private int inactiveUserAlertMaxSeconds;
    private String inventoryLabel;
	private String colorThemeOverride;
	private String helpUrl;
	private Boolean araChat;

        public Boolean getAraChat() {
                return araChat;
        }

        public void setAraChat(Boolean araChat) {
            this.araChat = araChat;
    }

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

    public int getInactiveUserAlertMaxSeconds() {
        return inactiveUserAlertMaxSeconds;
    }

    public void setInactiveUserAlertMaxSeconds(int inactiveUserAlertMaxSeconds) {
        this.inactiveUserAlertMaxSeconds = inactiveUserAlertMaxSeconds;
    }

    public int getInactiveUserMaxSeconds() {
        return inactiveUserMaxSeconds;
    }

    public void setInactiveUserMaxSeconds(int inactiveUserMaxSeconds) {
        this.inactiveUserMaxSeconds = inactiveUserMaxSeconds;
    }

    public String getInventoryLabel() {
        return inventoryLabel;
    }

    public void setInventoryLabel(String inventoryLabel) {
        this.inventoryLabel = inventoryLabel;
    }

    public String getColorThemeOverride() {
            return colorThemeOverride;
    }

    public void setColorThemeOverride(String colorThemeOverride) {
            this.colorThemeOverride = colorThemeOverride;
    }

    public String getHelpUrl() {
            return helpUrl;
    }

    public void setHelpUrl(String helpUrl) {
            this.helpUrl = helpUrl;
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
