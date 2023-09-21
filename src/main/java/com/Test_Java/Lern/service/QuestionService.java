package com.Test_Java.Lern.service;

import lombok.Getter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import org.springframework.core.io.ClassPathResource;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class QuestionService {

    private List<String> questions;
   private int currentQuestionIndex=1;


    public void initializeQuestions(String filename) throws IOException {
        // Открываем указанный файл Word из ресурсов и инициализируем список вопросов
        ClassPathResource resource = new ClassPathResource("file/" + filename);
        try (InputStream is = resource.getInputStream()) {
            try (var document = new XWPFDocument(is)) {
                questions = new ArrayList<>();

                // Парсим документ Word
                int questionNumber = 1;
                for (XWPFParagraph paragraph : document.getParagraphs()) {
                    String questionText = paragraph.getText().trim();
                    if (!questionText.isEmpty()) {
                        questions.add(questionNumber + ". " + questionText);
                        questionNumber++;
                    }

                }
            }
        }

        currentQuestionIndex = 0;
    }

    public String getQuestionFromWord() {
        if (currentQuestionIndex < questions.size()) {
            String question = questions.get(currentQuestionIndex);
            currentQuestionIndex++;
            return question;
        }
        return null; // Все вопросы закончились
    }

    int randomQuestionCount = 0;

    public String getRandomQuestion() {
        if (randomQuestionCount >= 15) {
            resetRandomQuestionCount();
            return null; // Если достигнут лимит в 15, сбрасываем счетчик и возвращаем null
        }
        if (!questions.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(questions.size());

            randomQuestionCount++;


            return questions.get(randomIndex);

        }

        return null; // Если список вопросов пуст
    }

    public void resetRandomQuestionCount() {
        randomQuestionCount = 0;


    }

    public int getCurrentQuestionIndex() {


        return randomQuestionCount;

    }


}
