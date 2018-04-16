/*
 * FareBotOpenHelper.java
 *
 * This file is part of FareBot.
 * Learn more at: https://codebutler.github.io/farebot/
 *
 * Copyright (C) 2011-2017, 2015-2016 Eric Butler <eric@codebutler.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.codebutler.farebot.persist.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import com.codebutler.farebot.persist.db.model.SavedCard;
import com.codebutler.farebot.persist.db.model.SavedKey;

public class FareBotDbCallback extends SupportSQLiteOpenHelper.Callback {

    private static final String DATABASE_NAME = "farebot.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Creates a new Callback to get database lifecycle events.
     */
    public FareBotDbCallback() {
        super(DATABASE_VERSION);
    }

    @Override public void onCreate(SupportSQLiteDatabase db) {
        db.execSQL(SavedCard.CREATE_TABLE);
        db.execSQL(SavedKey.CREATE_TABLE);
    }

    @Override public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }
}
