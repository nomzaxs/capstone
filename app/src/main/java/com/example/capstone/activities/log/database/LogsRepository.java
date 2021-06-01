package com.example.capstone.activities.log.database;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.capstone.model.Logs;

import java.util.List;

public class LogsRepository {

    private LogsDao logsDao;
    private LiveData<List<Logs>> logLiveData;

    public LogsRepository(Application application) {
        LogsRoomDatabase db = LogsRoomDatabase.getDatabase(application);
        logsDao = db.logsDao();
        logLiveData = logsDao.getOrderedLogs();
    }

    public LiveData<List<Logs>> getLogs() {
        // Room 은 모든 쿼리를 별도의 쓰레드에서 실행합니다.
        // LiveData 가 가지고 있는 데이터에 변경사항이 발생하면 Observer 객체에 변경을 알립니다.
        return logLiveData;
    }

    public void insertLog(Logs logs) {
        // UI가 아닌 스레드에서 호출해야 합니다. 그렇지 않으면 앱에서 예외가 발생합니다.
        // Room 은 UI를 차단하여 기본 스레드에서 장시간 실행되는 작업을 수행하지 않습니다.
        LogsRoomDatabase.databaseWriteExecutor.execute(() -> {
            logsDao.insert(logs);
        });
    }

    // UI가 아닌 스레드에서 호출해야 합니다. 그렇지 않으면 앱에서 예외가 발생합니다.
    // Room 은 UI를 차단하여 기본 스레드에서 장시간 실행되는 작업을 수행하지 않습니다.
    public void clearLogs() {
        LogsRoomDatabase.databaseWriteExecutor.execute(() -> {
            logsDao.clearAllLogs();
        });
    }
}

