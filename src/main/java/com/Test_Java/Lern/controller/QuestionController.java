package com.Test_Java.Lern.controller;

import com.Test_Java.Lern.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/questions")
    public String showQuestions() {
        return "fileSelection";
    }

    @GetMapping("/question")
    public String showQuestion(Model model, @RequestParam("filename") String filename, HttpSession session) throws IOException {
        questionService.initializeQuestions(filename);

        String question = questionService.getQuestionFromWord();
        if (question != null) {
            model.addAttribute("question", question);
        } else {
            model.addAttribute("question", "Все вопросы из файла " + filename + " закончились.");
        }

        // Сохраняем имя файла в атрибутах сеанса, чтобы использовать его при следующих запросах
        session.setAttribute("currentFilename", filename);

        return "question"; // Имя представления для отображения вопроса
    }

    @GetMapping("/nextQuestion")
    public String getNextQuestion(Model model, HttpSession session) {
        // Получаем имя файла из атрибутов сеанса
        String filename = (String) session.getAttribute("currentFilename");

        // Проверяем, не равно ли имя файла null, иначе возвращаем сообщение
        if (filename == null) {
            model.addAttribute("question", "Выберите файл с вопросами сначала.");
        } else {
            // Иначе получаем следующий вопрос
            String question = questionService.getQuestionFromWord();
            if (question != null) {
                model.addAttribute("question", question);
            } else {
                model.addAttribute("question", "Все вопросы из файла " + filename + " закончились.");
            }
        }
        return "question"; // Имя представления для отображения вопроса
    }
    @GetMapping("/randomQuestion")
    public String showRandomQuestion(Model model, @RequestParam("filename") String filename,HttpSession session) throws IOException {
        questionService.initializeQuestions(filename);
        model.addAttribute("question",questionService.getRandomQuestion());
        model.addAttribute("currentQuestion",questionService.getCurrentQuestionIndex());

        return "boss"; // Имя представления для отображения вопроса
    }

}
