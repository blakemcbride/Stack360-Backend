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

package com.arahant.rest;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Author: Blake McBride
 * Date: 12/31/22
 *
 * This defines certain REST services that are to skip the built-in authentication.
 * It is assumed that either the skipped service will perform its own authentication or that
 * no authentication is required for the specified service.
 */
public class NoBuiltinAuthentication {

    private final static HashMap<String, HashSet<String>> list;

    static {
        list = new HashMap<>();
        add("com.arahant.services.custom.waytogo.worker", "Login");

        add("com.arahant.services.standard.main", "NewApplicant");

        add("com.arahant.services.custom.waytogo.apply", "CreateLogin");
        add("com.arahant.services.custom.waytogo.apply", "CreatePassword");
        add("com.arahant.services.custom.waytogo.apply", "EmailCheck");
        add("com.arahant.services.custom.waytogo.apply", "ResendAuthEmail");
        add("com.arahant.services.custom.waytogo.apply", "ResetPassword");
        add("com.arahant.services.custom.waytogo.apply", "WhatDataNeeded");

        add("com.arahant.services.standard.ping", "Ping");
    }

    private static void add(String pkg, String cls) {
        HashSet<String> classes = list.get(pkg);
        if (classes == null) {
            classes = new HashSet<>();
            classes.add(cls);
            list.put(pkg, classes);
        } else
            classes.add(cls);
    }

    public static boolean bypassBuiltinAuthentication(String pkg, String cls) {
        if (pkg == null  ||  cls == null)
            return false;
        final HashSet<String> classes = list.get(pkg);
        return classes != null && classes.contains(cls);
    }

}
