package com.andronauts.quizzard.utils.pdfGenerators;

import android.content.Context;

import com.andronauts.quizzard.dataModels.Quiz;
import com.andronauts.quizzard.dataModels.Result;
import com.andronauts.quizzard.utils.DateFormatter;
import com.andronauts.quizzard.utils.FileManager;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ReportStudentGenerator {

    private Context context;
    private Quiz quiz;
    private Document document;
    private Result result;

    public ReportStudentGenerator(Context context) {
        this.context = context;
    }

    public void createReport(Quiz quiz, Result result){
        FileManager fileManager = new FileManager(context);
        this.quiz = quiz;
        this.result = result;
        File file = fileManager.getStudentReportPdfFile();
        document = new Document();
        try {
            PdfWriter.getInstance(document,new FileOutputStream(file));
            document.open();

            addTitle();
            addData();
            document.close();


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        fileManager.openPdfFile(file);
    }

    private void addTitle() throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface,1);
        preface.add(new Paragraph("Result Report"));
        addEmptyLine(preface,1);

        preface.add(new Paragraph(
                "This document shows the performance of all the students who appeared in the test"));

        addEmptyLine(preface,1);
        preface.add(new Paragraph("Title:- " +quiz.getTitle()));
        preface.add(new Paragraph("Description:- "+ quiz.getDescription()));
        preface.add(new Paragraph("StartTime:- "+ new DateFormatter(quiz.getStartTime()).getDateAndTime()));
        preface.add(new Paragraph("EndTime:- "+new DateFormatter(quiz.getEndTime()).getDateAndTime()));

        document.add(preface);
        document.newPage();
    }

    private void addData() throws DocumentException {
        int[] responses = result.getResponses();

        for(int i =0 ;i<quiz.getQuestion().size(); i++){
            Quiz.Question question = quiz.getQuestion().get(i);

            Paragraph paragraph = new Paragraph();
            addEmptyLine(paragraph,2);
            paragraph.add(String.valueOf(i+1)+".  ");
            paragraph.add(question.getQuestion());
            paragraph.add("       "+"+"+question.getPositive()+"   "+"-"+question.getNegative());

            for(int j = 0; j<question.getOptions().size(); j++){
                addEmptyLine(paragraph,1);
                String option = question.getOptions().get(j);
                paragraph.add(String.valueOf(j+1)+"  ");
                paragraph.add(option);
            }

            addEmptyLine(paragraph,2);
            paragraph.add("Correct: "+question.getCorrect());
            paragraph.add("      ");

            paragraph.add("Your Response: ");
            if(responses[i]  == 0){
                paragraph.add("None");
            }else{
                paragraph.add(String.valueOf(responses[i]));
            }
            addEmptyLine(paragraph,1);

            int marks;
            boolean isPositive;
            if(responses[i] == question.getCorrect()){
                marks = question.getPositive();
                isPositive = true;
            }else{
                isPositive = false;
                marks = question.getNegative();
            }

            paragraph.add("Marks Awarded: "+(isPositive?"+":"-")+marks);
            document.add(paragraph);
        }

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph,2);

        int total =0;

        for(Quiz.Question question: quiz.getQuestion()){
            total+=question.getPositive();
        }
        paragraph.add("Score- "+result.getScore()+" / "+ total);

        document.add(paragraph);


    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
