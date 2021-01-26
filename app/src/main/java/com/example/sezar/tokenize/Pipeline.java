package com.example.sezar.tokenize;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
public class Pipeline {
    private static Properties properties;
    private static  String propertiesName = "tokenize";
    private static StanfordCoreNLP stanfordCoreNLP;

    private Pipeline(){

    }

    static {
        properties = new Properties();
        properties.setProperty("annotators",propertiesName);
    }

    public static StanfordCoreNLP getPipeline(){
        if (stanfordCoreNLP == null){
            stanfordCoreNLP = new StanfordCoreNLP(properties);
        }
        return stanfordCoreNLP;
    }
}
