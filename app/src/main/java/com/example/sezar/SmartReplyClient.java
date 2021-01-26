/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.example.sezar;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.WorkerThread;

import com.example.sezar.model.SmartReply;
import com.example.sezar.utility.AssetsUtil;
import com.getkeepsafe.relinker.ReLinker;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/** Interface to load TfLite model and provide predictions. */
public class SmartReplyClient implements AutoCloseable {
  private static final String MODEL_PATH = "smartreply.tflite";
  private static final String BACKOFF_PATH = "backoff_response.txt";
  private static final String JNI_LIB = "smartreply_jni";
  private static final String TAG="smart_reply";
  private static String [] repyList;
  private final Context context;
  private static long storage;
  private MappedByteBuffer model;

  private volatile boolean isLibraryLoaded;

  public SmartReplyClient(Context context) {
    this.context = context;
  }

  public boolean isLoaded() {
    return storage != 0;
  }

  @WorkerThread
  public synchronized void loadModel() {
//    if (!isLibraryLoaded) {
      try {
//        System.setProperty("java.library.path","C:\\Users\\CASPER\\Desktop\\Brutus\\app\\libs\\cc");
//         System.loadLibrary("C:\\Users\\CASPER\\Desktop\\Brutus\\app\\libs\\cc\\smartreply_jni.cc");
       System.loadLibrary(JNI_LIB);
        System.out.println(System.getProperty("java.library.path"));
//        ReLinker.loadLibrary(context,JNI_LIB);
//        ReLinker.log(null).loadLibrary(context, "mylibrary");
//        isLibraryLoaded = true;
      } catch (Exception e) {
        e.printStackTrace();
      }
    //}


    try {
      model = loadModelFile();
      String[] backoff = loadBackoffList();
      repyList=backoff;
//      storage=373285860;
      storage = loadJNI(model, backoff);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

    @WorkerThread
    public synchronized SmartReply[] predict(String[] input) {
        if (storage != 0) {
            return predictJNI(storage, input);
        } else {
            return new SmartReply[] {};
        }
    }

    public static String[] getRepyList() {
        return repyList;
    }

    @WorkerThread
  public synchronized void unloadModel() {
    close();
  }

  @Override
  public synchronized void close() {
    if (storage != 0) {
      unloadJNI(storage);
      storage = 0;
    }
  }

  private MappedByteBuffer loadModelFile() throws IOException {
    try (AssetFileDescriptor fileDescriptor =
            AssetsUtil.getAssetFileDescriptorOrCached(context, MODEL_PATH);
         FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor())) {
      FileChannel fileChannel = inputStream.getChannel();
      long startOffset = fileDescriptor.getStartOffset();
      long declaredLength = fileDescriptor.getDeclaredLength();
      return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
  }

  private String[] loadBackoffList() throws IOException {
    List<String> labelList = new ArrayList<String>();
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(context.getAssets().open(BACKOFF_PATH)))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (!line.isEmpty()) {
          labelList.add(line);
        }
      }
    }
    String[] ans = new String[labelList.size()];
    labelList.toArray(ans);
    return ans;
  }

  @Keep
  private native long loadJNI(MappedByteBuffer buffer, String[] backoff);

  @Keep
  private native SmartReply[] predictJNI(long storage, String[] text);

  @Keep
  private native void unloadJNI(long storage);
}
