package com.codebutler.farebot.persist.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.codebutler.farebot.persist.CardPersister;
import com.codebutler.farebot.persist.db.model.SavedCard;
import com.codebutler.farebot.persist.db.model.SavedCardModel;
import com.squareup.sqldelight.SqlDelightQuery;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DbCardPersister implements CardPersister {

    @NonNull private final SupportSQLiteOpenHelper openHelper;

    public DbCardPersister(@NonNull SupportSQLiteOpenHelper openHelper) {
        this.openHelper = openHelper;
    }

    @NonNull
    @Override
    public List<SavedCard> getCards() {
        List<SavedCard> result = new ArrayList<>();
        try (SupportSQLiteDatabase db = openHelper.getReadableDatabase()) {
            SqlDelightQuery query = SavedCard.SELECT_ALL;
            try (Cursor cursor = db.query(query)) {
                while (cursor.moveToNext()) {
                    result.add(SavedCard.SELECT_ALL_MAPPER.map(cursor));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Nullable
    @Override
    public SavedCard getCard(long id) {
        try (SupportSQLiteDatabase db = openHelper.getReadableDatabase()) {
            SqlDelightQuery query = SavedCard.FACTORY.select_by_id(id);
            try (Cursor cursor = db.query(query)) {
                if (cursor.moveToFirst()) {
                    return SavedCard.SELECT_ALL_MAPPER.map(cursor);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public long insertCard(@NonNull SavedCard savedCard) {
        try (SupportSQLiteDatabase db = openHelper.getWritableDatabase()) {
            SavedCardModel.Insert_row insertRow = new SavedCardModel.Insert_row(db, SavedCard.FACTORY);
            insertRow.bind(
                    savedCard.type(),
                    savedCard.serial(),
                    savedCard.data(),
                    savedCard.scanned_at());
            return insertRow.executeInsert();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCard(@NonNull SavedCard savedCard) {
        try (SupportSQLiteDatabase db = openHelper.getWritableDatabase()) {
            SavedCardModel.Delete_by_id deleteRow = new SavedCardModel.Delete_by_id(db);
            deleteRow.bind(savedCard._id());
            deleteRow.executeUpdateDelete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
