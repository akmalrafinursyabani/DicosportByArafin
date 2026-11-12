package com.example.core.data.source.local.room;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.core.data.source.local.entity.NewsEntity;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NewsDao_Impl implements NewsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<NewsEntity> __insertionAdapterOfNewsEntity;

  private final EntityDeletionOrUpdateAdapter<NewsEntity> __updateAdapterOfNewsEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateFavouriteStatus;

  public NewsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNewsEntity = new EntityInsertionAdapter<NewsEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `news` (`id`,`title`,`description`,`imageUrl`,`badgeUrl`,`stadium`,`formedYear`,`league`,`isFavourite`,`isCached`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NewsEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        statement.bindString(4, entity.getImageUrl());
        statement.bindString(5, entity.getBadgeUrl());
        statement.bindString(6, entity.getStadium());
        statement.bindString(7, entity.getFormedYear());
        statement.bindString(8, entity.getLeague());
        final int _tmp = entity.isFavourite() ? 1 : 0;
        statement.bindLong(9, _tmp);
        final int _tmp_1 = entity.isCached() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
      }
    };
    this.__updateAdapterOfNewsEntity = new EntityDeletionOrUpdateAdapter<NewsEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `news` SET `id` = ?,`title` = ?,`description` = ?,`imageUrl` = ?,`badgeUrl` = ?,`stadium` = ?,`formedYear` = ?,`league` = ?,`isFavourite` = ?,`isCached` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NewsEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        statement.bindString(4, entity.getImageUrl());
        statement.bindString(5, entity.getBadgeUrl());
        statement.bindString(6, entity.getStadium());
        statement.bindString(7, entity.getFormedYear());
        statement.bindString(8, entity.getLeague());
        final int _tmp = entity.isFavourite() ? 1 : 0;
        statement.bindLong(9, _tmp);
        final int _tmp_1 = entity.isCached() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        statement.bindString(11, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateFavouriteStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE news SET isFavourite = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertNews(final List<NewsEntity> news,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfNewsEntity.insert(news);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateNews(final NewsEntity news, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfNewsEntity.handle(news);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateFavouriteStatus(final String id, final boolean isFavourite,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFavouriteStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isFavourite ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateFavouriteStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<NewsEntity>> getAllNews() {
    final String _sql = "SELECT * FROM news";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"news"}, new Callable<List<NewsEntity>>() {
      @Override
      @NonNull
      public List<NewsEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfBadgeUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "badgeUrl");
          final int _cursorIndexOfStadium = CursorUtil.getColumnIndexOrThrow(_cursor, "stadium");
          final int _cursorIndexOfFormedYear = CursorUtil.getColumnIndexOrThrow(_cursor, "formedYear");
          final int _cursorIndexOfLeague = CursorUtil.getColumnIndexOrThrow(_cursor, "league");
          final int _cursorIndexOfIsFavourite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavourite");
          final int _cursorIndexOfIsCached = CursorUtil.getColumnIndexOrThrow(_cursor, "isCached");
          final List<NewsEntity> _result = new ArrayList<NewsEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NewsEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpBadgeUrl;
            _tmpBadgeUrl = _cursor.getString(_cursorIndexOfBadgeUrl);
            final String _tmpStadium;
            _tmpStadium = _cursor.getString(_cursorIndexOfStadium);
            final String _tmpFormedYear;
            _tmpFormedYear = _cursor.getString(_cursorIndexOfFormedYear);
            final String _tmpLeague;
            _tmpLeague = _cursor.getString(_cursorIndexOfLeague);
            final boolean _tmpIsFavourite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavourite);
            _tmpIsFavourite = _tmp != 0;
            final boolean _tmpIsCached;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsCached);
            _tmpIsCached = _tmp_1 != 0;
            _item = new NewsEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpImageUrl,_tmpBadgeUrl,_tmpStadium,_tmpFormedYear,_tmpLeague,_tmpIsFavourite,_tmpIsCached);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<NewsEntity>> getFavouriteNews() {
    final String _sql = "SELECT * FROM news WHERE isFavourite = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"news"}, new Callable<List<NewsEntity>>() {
      @Override
      @NonNull
      public List<NewsEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfBadgeUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "badgeUrl");
          final int _cursorIndexOfStadium = CursorUtil.getColumnIndexOrThrow(_cursor, "stadium");
          final int _cursorIndexOfFormedYear = CursorUtil.getColumnIndexOrThrow(_cursor, "formedYear");
          final int _cursorIndexOfLeague = CursorUtil.getColumnIndexOrThrow(_cursor, "league");
          final int _cursorIndexOfIsFavourite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavourite");
          final int _cursorIndexOfIsCached = CursorUtil.getColumnIndexOrThrow(_cursor, "isCached");
          final List<NewsEntity> _result = new ArrayList<NewsEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NewsEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpBadgeUrl;
            _tmpBadgeUrl = _cursor.getString(_cursorIndexOfBadgeUrl);
            final String _tmpStadium;
            _tmpStadium = _cursor.getString(_cursorIndexOfStadium);
            final String _tmpFormedYear;
            _tmpFormedYear = _cursor.getString(_cursorIndexOfFormedYear);
            final String _tmpLeague;
            _tmpLeague = _cursor.getString(_cursorIndexOfLeague);
            final boolean _tmpIsFavourite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavourite);
            _tmpIsFavourite = _tmp != 0;
            final boolean _tmpIsCached;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsCached);
            _tmpIsCached = _tmp_1 != 0;
            _item = new NewsEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpImageUrl,_tmpBadgeUrl,_tmpStadium,_tmpFormedYear,_tmpLeague,_tmpIsFavourite,_tmpIsCached);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Boolean> isFavourite(final String id) {
    final String _sql = "SELECT EXISTS(SELECT * FROM news WHERE id = ? AND isFavourite = 1)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"news"}, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp != 0;
          } else {
            _result = false;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
