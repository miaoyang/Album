package com.ym.album.room.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ym.album.room.TypeFactory;
import com.ym.album.room.model.UserInfo;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserInfo> __insertionAdapterOfUserInfo;

  private final EntityDeletionOrUpdateAdapter<UserInfo> __deletionAdapterOfUserInfo;

  private final EntityDeletionOrUpdateAdapter<UserInfo> __updateAdapterOfUserInfo;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByUserName;

  public UserDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserInfo = new EntityInsertionAdapter<UserInfo>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `t_user` (`id`,`user_name`,`password`,`telephone`,`email`,`age`,`birth_time`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserInfo value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
        if (value.getUserName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getUserName());
        }
        if (value.getPassword() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPassword());
        }
        if (value.getTelephone() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTelephone());
        }
        if (value.getEmail() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getEmail());
        }
        stmt.bindLong(6, value.getAge());
        final Long _tmp = TypeFactory.date2Long(value.getBirthTime());
        if (_tmp == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, _tmp);
        }
      }
    };
    this.__deletionAdapterOfUserInfo = new EntityDeletionOrUpdateAdapter<UserInfo>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `t_user` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserInfo value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
      }
    };
    this.__updateAdapterOfUserInfo = new EntityDeletionOrUpdateAdapter<UserInfo>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `t_user` SET `id` = ?,`user_name` = ?,`password` = ?,`telephone` = ?,`email` = ?,`age` = ?,`birth_time` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserInfo value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
        if (value.getUserName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getUserName());
        }
        if (value.getPassword() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPassword());
        }
        if (value.getTelephone() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTelephone());
        }
        if (value.getEmail() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getEmail());
        }
        stmt.bindLong(6, value.getAge());
        final Long _tmp = TypeFactory.date2Long(value.getBirthTime());
        if (_tmp == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, _tmp);
        }
        if (value.getId() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindLong(8, value.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteByUserName = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "delete from t_user where user_name=?";
        return _query;
      }
    };
  }

  @Override
  public void insertUserInfo(final UserInfo userInfo) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUserInfo.insert(userInfo);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertListUserInfo(final List<UserInfo> userInfoList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUserInfo.insert(userInfoList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteByUserInfo(final UserInfo userInfo) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfUserInfo.handle(userInfo);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteListUserInfo(final List<UserInfo> userInfoList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfUserInfo.handleMultiple(userInfoList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateUserInfo(final UserInfo userInfo) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfUserInfo.handle(userInfo);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteByUserName(final String userName) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByUserName.acquire();
    int _argIndex = 1;
    if (userName == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, userName);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteByUserName.release(_stmt);
    }
  }

  @Override
  public List<UserInfo> selectAllUserInfo() {
    final String _sql = "select * from t_user";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "user_name");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfTelephone = CursorUtil.getColumnIndexOrThrow(_cursor, "telephone");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
      final int _cursorIndexOfBirthTime = CursorUtil.getColumnIndexOrThrow(_cursor, "birth_time");
      final List<UserInfo> _result = new ArrayList<UserInfo>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final UserInfo _item;
        final String _tmpUserName;
        if (_cursor.isNull(_cursorIndexOfUserName)) {
          _tmpUserName = null;
        } else {
          _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
        }
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        final String _tmpTelephone;
        if (_cursor.isNull(_cursorIndexOfTelephone)) {
          _tmpTelephone = null;
        } else {
          _tmpTelephone = _cursor.getString(_cursorIndexOfTelephone);
        }
        final String _tmpEmail;
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _tmpEmail = null;
        } else {
          _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        }
        final int _tmpAge;
        _tmpAge = _cursor.getInt(_cursorIndexOfAge);
        final Date _tmpBirthTime;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfBirthTime)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfBirthTime);
        }
        _tmpBirthTime = TypeFactory.long2Date(_tmp);
        _item = new UserInfo(_tmpUserName,_tmpPassword,_tmpTelephone,_tmpEmail,_tmpAge,_tmpBirthTime);
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public UserInfo selectByUsername(final String userName) {
    final String _sql = "select * from t_user where user_name=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "user_name");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfTelephone = CursorUtil.getColumnIndexOrThrow(_cursor, "telephone");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
      final int _cursorIndexOfBirthTime = CursorUtil.getColumnIndexOrThrow(_cursor, "birth_time");
      final UserInfo _result;
      if(_cursor.moveToFirst()) {
        final String _tmpUserName;
        if (_cursor.isNull(_cursorIndexOfUserName)) {
          _tmpUserName = null;
        } else {
          _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
        }
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        final String _tmpTelephone;
        if (_cursor.isNull(_cursorIndexOfTelephone)) {
          _tmpTelephone = null;
        } else {
          _tmpTelephone = _cursor.getString(_cursorIndexOfTelephone);
        }
        final String _tmpEmail;
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _tmpEmail = null;
        } else {
          _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        }
        final int _tmpAge;
        _tmpAge = _cursor.getInt(_cursorIndexOfAge);
        final Date _tmpBirthTime;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfBirthTime)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfBirthTime);
        }
        _tmpBirthTime = TypeFactory.long2Date(_tmp);
        _result = new UserInfo(_tmpUserName,_tmpPassword,_tmpTelephone,_tmpEmail,_tmpAge,_tmpBirthTime);
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _result.setId(_tmpId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public UserInfo selectByTelephone(final String tel) {
    final String _sql = "select * from t_user where telephone=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (tel == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tel);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "user_name");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfTelephone = CursorUtil.getColumnIndexOrThrow(_cursor, "telephone");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
      final int _cursorIndexOfBirthTime = CursorUtil.getColumnIndexOrThrow(_cursor, "birth_time");
      final UserInfo _result;
      if(_cursor.moveToFirst()) {
        final String _tmpUserName;
        if (_cursor.isNull(_cursorIndexOfUserName)) {
          _tmpUserName = null;
        } else {
          _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
        }
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        final String _tmpTelephone;
        if (_cursor.isNull(_cursorIndexOfTelephone)) {
          _tmpTelephone = null;
        } else {
          _tmpTelephone = _cursor.getString(_cursorIndexOfTelephone);
        }
        final String _tmpEmail;
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _tmpEmail = null;
        } else {
          _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        }
        final int _tmpAge;
        _tmpAge = _cursor.getInt(_cursorIndexOfAge);
        final Date _tmpBirthTime;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfBirthTime)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfBirthTime);
        }
        _tmpBirthTime = TypeFactory.long2Date(_tmp);
        _result = new UserInfo(_tmpUserName,_tmpPassword,_tmpTelephone,_tmpEmail,_tmpAge,_tmpBirthTime);
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _result.setId(_tmpId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
