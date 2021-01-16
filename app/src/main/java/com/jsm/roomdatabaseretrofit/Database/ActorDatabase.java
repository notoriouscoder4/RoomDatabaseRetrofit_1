package com.jsm.roomdatabaseretrofit.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.jsm.roomdatabaseretrofit.Dao.ActorDao;
import com.jsm.roomdatabaseretrofit.Modal.Actor;

@Database(entities = {Actor.class}, version = 2)
public abstract class ActorDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "ActorDatabase";

    public abstract ActorDao actorDao();

    private static volatile ActorDatabase INSTANCE;

    public static ActorDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ActorDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, ActorDatabase.class,
                            DATABASE_NAME)
                            .addCallback(callback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(INSTANCE);
        }
    };

    static class PopulateAsyncTask extends AsyncTask<Void, Void, Void>{

        private ActorDao actorDao;

        PopulateAsyncTask(ActorDatabase actorDatabase) {
            actorDao = actorDatabase.actorDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            actorDao.deleteAllActors();
            return null;
        }
    }
}
