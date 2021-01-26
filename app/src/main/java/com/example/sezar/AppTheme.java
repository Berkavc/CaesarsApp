package com.example.sezar;

import android.app.Application;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.preference.PreferenceManager;

import com.zc.logger.LogManager;
import com.zc.logger.config.Config;
import com.zc.logger.config.LogManagerConfig;
import com.zc.logger.config.OnFileFullListener;
import com.zc.logger.format.DefaultFormatter;
import com.zc.logger.log.ConsoleLogger;
import com.zc.logger.log.FileLogger;
import com.zc.logger.model.LogModule;

import java.io.File;

import static com.example.sezar.BuildConfig.DEBUG;

public class AppTheme extends Application {
    public static final String TAG = "AppTheme";
    private boolean nightmode;

    @Override
    public void onCreate() {
        super.onCreate();
        // We load the Night Mode state here
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

    }

    private void initLogManager() {
        LogManagerConfig config = new LogManagerConfig.Builder(this)
                .minLevel(Config.LEVEL_VERBOSE) // default, lowest log level is v
                .maxLevel(Config.LEVEL_ASSERT) // default, highest log level is a
                .enableModuleFilter(!DEBUG) // if false, all logs are printed; if true, only added module logs are printed
                .addModule(new LogModule("Caesar"))
                .writeLogs(true) // display Logger internal logs
                .setFileLevel(10) // create at most 10 log files
                .setFileSize(100000) // create every log file smaller than 100000Byte
                .addOnFileFullListener(new OnFileFullListener() {
                    @Override
                    public void onFileFull(File file) {
                        // called in ui thread when one log file is full, you can upload it to your log server
                    }
                })
                .formatter(new DefaultFormatter()) // log format is the same as adb logcat
                .addLogger(new FileLogger()) // print log to file
                .addLogger(new ConsoleLogger()) // print log to console
                .build();
        LogManager.getInstance().init(config);
    }

}