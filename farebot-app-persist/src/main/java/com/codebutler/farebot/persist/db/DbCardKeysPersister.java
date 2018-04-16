package com.codebutler.farebot.persist.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.codebutler.farebot.persist.CardKeysPersister;
import com.codebutler.farebot.persist.db.model.SavedKey;
import com.codebutler.farebot.persist.db.model.SavedKeyModel;
import com.squareup.sqldelight.SqlDelightQuery;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DbCardKeysPersister implements CardKeysPersister {

    @NonNull private final SupportSQLiteOpenHelper openHelper;

    public DbCardKeysPersister(@NonNull SupportSQLiteOpenHelper openHelper) {
        this.openHelper = openHelper;
    }

    @NonNull
    @Override
    public List<SavedKey> getSavedKeys() {
        List<SavedKey> result = new ArrayList<>();
        SupportSQLiteDatabase db = openHelper.getReadableDatabase();
        SqlDelightQuery query = SavedKey.SELECT_ALL;
        try (Cursor cursor = db.query(query)) {
            while (cursor.moveToNext()) {
                result.add(SavedKey.SELECT_ALL_MAPPER.map(cursor));
            }
        }
        return result;
    }

    @Nullable
    @Override
    public SavedKey getForTagId(@NonNull String tagId) {
        SupportSQLiteDatabase db = openHelper.getReadableDatabase();
        SqlDelightQuery query = SavedKey.FACTORY.select_by_card_id(tagId);
        try (Cursor cursor = db.query(query)) {
            if (cursor.moveToFirst()) {
                return SavedKey.SELECT_ALL_MAPPER.map(cursor);
            }
        }
        return null;
    }

    @Override
    public long insert(@NonNull SavedKey savedKey) {
        try (SupportSQLiteDatabase db = openHelper.getWritableDatabase()) {
            SavedKey.Insert_row insertRow = new SavedKeyModel.Insert_row(db, SavedKey.FACTORY);
            insertRow.bind(
                    savedKey.card_id(),
                    savedKey.card_type(),
                    savedKey.key_data(),
                    savedKey.created_at());
            return insertRow.executeInsert();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NonNull SavedKey savedKey) {
        try (SupportSQLiteDatabase db = openHelper.getWritableDatabase()) {
            SavedKey.Delete_by_id deleteRow = new SavedKeyModel.Delete_by_id(db);
            deleteRow.bind(savedKey._id());
            deleteRow.executeUpdateDelete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
