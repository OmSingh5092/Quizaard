package com.andronauts.quizard.utils.pdfGenerators;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.dataModels.Result;
import com.andronauts.quizard.dataModels.Student;

import com.andronauts.quizard.utils.DateFormatter;
import com.andronauts.quizard.utils.FileManager;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.Line;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import androidx.core.content.FileProvider;

public class ReportFacultyGenerator {
    Context context;

    public ReportFacultyGenerator(Context context) {
        this.context = context;
    }

    public void createReportPdf(Quiz quiz, List<Result> results, Map<String,Student>students){
        FileManager fileManager = new FileManager(context);

        File file = fileManager.getFacultyReportPdfFile();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document,new FileOutputStream(file));
            document.open();

            addTitle(document,quiz);
            addData(document,results,students,quiz);
            document.close();


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        fileManager.openPdfFile(file);
    }

    private void addTitle(Document document,Quiz quiz) throws DocumentException {
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

    private void addData(Document document,List<Result> results,Map<String,Student> students,Quiz quiz) throws DocumentException {
        Paragraph titlePara = new Paragraph();
        titlePara.add("Student Response Data");
        document.add(titlePara);

        for(int i =0 ; i<results.size(); i++){
            Result result = results.get(i);
            Student student =students.get(result.getStudent());
            Paragraph resultPara = new Paragraph();
            addEmptyLine(resultPara,3);
            resultPara.add(String.valueOf(i+1));
            resultPara.add("Enrollment Number-  "+student.getRegistrationNumber());
            addEmptyLine(resultPara,1);
            resultPara.add("Name-  "+student.getName());
            addEmptyLine(resultPara,2);

            document.add(resultPara);
            addResultTable(document,result,quiz);
            addScores(document,result,quiz);
        }

    }

    private void addResultTable(Document document,Result result,Quiz quiz) throws DocumentException {
        PdfPTable table = new PdfPTable(4);

        PdfPCell c1 = new PdfPCell(new Phrase("Question No."));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Response"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Correct Response"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Awarded Marks"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        List<Quiz.Question> questions = quiz.getQuestion();
        int [] responses = result.getResponses();

        int score =0;

        for(int i =0 ; i<result.getResponses().length ; i++){
            Quiz.Question question = questions.get(i);
            //Adding Serial Number
            table.addCell(String.valueOf(i+1));
            //Adding response
            table.addCell(String.valueOf(responses[i]));
            //Adding correct response
            table.addCell(String.valueOf(questions.get(i).getCorrect()));

            //Adding marks
            int marks;
            if(responses[i] == questions.get(i).getCorrect()){
                marks = question.getPositive();
            }else{
                marks =-1* question.getNegative();
            }
            //Adding all the marks
            score+=marks;

            table.addCell(String.valueOf(marks));
        }

        document.add(table);

    }

    private void addScores(Document document, Result result, Quiz quiz) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph,2);

        int total = 0;
        for(Quiz.Question question: quiz.getQuestion()){
            total+=question.getPositive();
        }

        paragraph.add("Score - "+String.valueOf(result.getScore())+" / " +String.valueOf(total));
        document.add(paragraph);
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


}
