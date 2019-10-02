package com.ggkttd.kolmakov.testSystem.utils;

import com.ggkttd.kolmakov.testSystem.domain.*;
import com.ggkttd.kolmakov.testSystem.exceptions.NotFoundException;
import com.ibm.icu.text.CharsetDetector;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class TestUtils {
    private static final Logger LOGGER = Logger.getLogger(TestUtils.class);

    @Value(value = "${docxHome}")
    private String docxHome;
    @Value("${test.data.home}")
    private String homeDir;

    public String getUserDirectory(User user) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        String currentPath = user.getLogin() + "\\" + formatter.format(date) + "\\";
        new File(currentPath).mkdirs();
        return currentPath;
    }

    public File saveMultipartFile(MultipartFile file, String currentDir) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName = homeDir + "\\" + currentDir + UUID.randomUUID().toString() + "." + extension;
        File savedFile = new File(newFileName);
        file.transferTo(savedFile);

        return savedFile;
    }

    public List<Question> getResultFromUserAnswers(Test test, List<AnswerLog> logsFromDb) {
        List<Question> questions = new LinkedList<>();

        for (Question question : test.getQuestions()) {
            boolean right = true;
            for (AnswerLog answerLog : getLogsByQuestion(logsFromDb, question)) {
                if (!answerLog.isRight()) {
                    right = false;
                    break;
                }
            }
            question.setAnsweredRight(right);
            questions.add(question);
        }
        return questions;
    }

    /**
     * Method to get all logs which contains info about current question
     *
     * @param logs     - all test logs, contains info about all questions
     * @param question - required question
     * @return - logs with required question info, or empty list if logs not found
     */
    public List<AnswerLog> getLogsByQuestion(List<AnswerLog> logs, Question question) {
        Collections.sort(logs, (o1, o2) -> Math.toIntExact(o1.getQuestion().getId() - o2.getQuestion().getId()));
        List<AnswerLog> logList = new LinkedList<>();
        for (AnswerLog answerLog : logs) {
            if (answerLog.getQuestion().getId().equals(question.getId())) {
                logList.add(answerLog);
            }
        }
        return logList;
    }

    public Question getQuestionById(List<Question> questions, Long id) {
        for (Question question : questions) {
            if (question.getId().equals(id)) {
                return question;
            }
        }

        throw new NotFoundException("QUESTION #" + id + " NOT FOUND");
    }

    public Answer getAnswerById(List<Answer> answers, Long id) {
        for (Answer answer : answers) {
            if (answer.getId().equals(id)) {
                return answer;
            }
        }

        throw new NotFoundException("ANSWER #" + id + " NOT FOUND");
    }

    public File saveTest2File(Test test) {
        new File(docxHome).mkdirs();
        String fileName = docxHome + test.getOwner().getName() + "\\ " + test.getOwner().getSurname() + " " + test.getName() + ".docx";
        try {
            File file = new File(fileName);
            file.createNewFile();
            write2Document(test, file);
            return file;
        } catch (IOException e) {
            LOGGER.warn("CAN'T SAVE TEST TO FILE CAUSE OF: ",e);
        }
        return null;
    }

    public String convert2Utf8(String string) {
        CharsetDetector detector = new CharsetDetector();
        detector.setText(string.getBytes());
        return detector.getString(string.getBytes(), "UTF_8");
//        return new String(string.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    private static final String cTAbstractNumDecimalXML =
            "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
                    + "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
                    + "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
                    + "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"lowerLetter\"/><w:lvlText w:val=\"%2.\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
                    + "</w:abstractNum>";

    private void write2Document(Test test, File file) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            XWPFDocument document = new XWPFDocument();
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun title = paragraph.createRun();


            //set test name format
            title.setFontSize(16);
            title.setBold(true);
            title.setFontFamily("Times new Roman");
            title.setText(test.getName().toUpperCase());
            paragraph.setAlignment(ParagraphAlignment.CENTER);

            //multilayer list
            CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
            CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
            XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
            XWPFNumbering numbering = document.createNumbering();
            BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
            BigInteger numID = numbering.addNum(abstractNumID);

            //first level format: 1. Question
            for (Question question : test.getQuestions()) {

                paragraph = document.createParagraph();
                paragraph.setNumID(numID);
                XWPFRun run = paragraph.createRun();
                run.setText(question.getName());
                paragraph.setSpacingBefore(24);

                //second level format: a. Answer
                for (int i = 0; i < question.getAnswers().size(); i++) {
                    Answer answer = question.getAnswers().get(i);
                    paragraph = document.createParagraph();
                    paragraph.setNumID(numID);
                    paragraph.getCTP().getPPr().getNumPr().addNewIlvl().setVal(BigInteger.valueOf(1));

                    XWPFRun run1 = paragraph.createRun();
                    run1.setText(answer.getName());

                }
            }
            document.write(out);
        } catch (IOException | XmlException e) {
            e.printStackTrace();
        }
    }

}
