package com.ggkttd.kolmakov.testSystem.processor;

import com.ggkttd.kolmakov.testSystem.domain.Answer;
import com.ggkttd.kolmakov.testSystem.domain.Question;
import com.ggkttd.kolmakov.testSystem.domain.Test;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DocumentProcessor {
    private static final Logger LOGGER = Logger.getLogger(DocumentProcessor.class);
    private static final String WORD = "[a-zA-Zа-яА-Я_0-9]+";
    private static final String DEFINITION_REGEX = WORD + "\\s+-\\s+.+?\\.";
    private static final String QUESTION_REGEX = "^" + WORD + "\\s+-\\s+";

    public Test generateFromFile(File file) {
        List<String> definitions;
        List<Question> questions = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            String text;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            text = builder.toString();
            definitions = getFromRegex(DEFINITION_REGEX, text);
            for (String definition : definitions) {
                List<Answer> answers = new LinkedList<>();
                String questionText = getFromRegex(QUESTION_REGEX, definition).get(0);
                String answerText = definition.replaceAll(QUESTION_REGEX, "");
                answers.add(new Answer(answerText));
                questions.add(new Question(questionText, answers));
            }
        } catch (IOException e) {
            LOGGER.warn(e);
        }

        return new Test(questions);
    }

    private List<String> getFromRegex(String regex, String text) {
        List<String> matches = new LinkedList<>();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            matches.add(matcher.group());
        }

        return matches;
    }
}
