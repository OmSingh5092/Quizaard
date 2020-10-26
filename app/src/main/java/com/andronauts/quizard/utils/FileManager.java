package com.andronauts.quizard.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import androidx.core.content.FileProvider;

public class FileManager {
    Context context;

    File parentFile;

    public FileManager(Context context) {
        this.context = context;

        parentFile = new File(Environment.getExternalStorageDirectory(),"Quizzard");
        if(!parentFile.exists()){
            parentFile.mkdir();
        }
    }

    public File getFacultyReportPdfFile(){
        String name = "report_"+"faculty_"+new DateFormatter(System.currentTimeMillis()).getDateAndTime()+".pdf";
        File file = new File(parentFile,name);
        return file;
    }

    public File getStudentReportPdfFile(){
        String name = "report_"+"student_"+new DateFormatter(System.currentTimeMillis()).getDateAndTime()+".pdf";
        File file = new File(parentFile,name);
        return file;
    }

    public File getStudentQuestionPdfFile(){
        String name = "questions_"+new DateFormatter(System.currentTimeMillis()).getDateAndTime()+".pdf";
        File file = new File(parentFile,name);
        return file;
    }

    public File getQuestionPaperPdfFile(){
        String name = "questionPaper"+new DateFormatter(System.currentTimeMillis()).getDateAndTime()+".pdf";
        File file = new File(parentFile,name);
        return file;
    }

    public void openPdfFile(File file){
        Uri contentUri = FileProvider.getUriForFile(context,context.getPackageName()+".provider",file);
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.setDataAndType(contentUri,"application/pdf");
        i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(i);
    }
}
