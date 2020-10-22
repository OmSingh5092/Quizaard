package com.andronauts.quizard.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

import com.andronauts.quizard.dataModels.Quiz;

import java.time.Instant;

public class CalendarManager {
    Context context;

    public CalendarManager(Context context) {
        this.context = context;
    }

    public void addQuizEvent(Quiz quiz){
        Intent i = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, quiz.getStartTime())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,quiz.getEndTime())
                .putExtra(CalendarContract.Events.TITLE, quiz.getTitle())
                .putExtra(CalendarContract.Events.DESCRIPTION, quiz.getDescription());

        context.startActivity(i);
    }
}
