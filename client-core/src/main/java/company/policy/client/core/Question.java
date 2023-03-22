package company.policy.client.core;

import java.io.Serializable;
import static java.text.MessageFormat.format;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Question implements Serializable {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("i18n/messages", Locale.ITALIAN);
    private static final long serialVersionUID = 1L;
    private final int number;
    private final String staffName;
    private final String topic;
    private final String subTopic;
    private final String question;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;
    private final String optionE;
    private final int correctAnswer;
    private final int givenAnswer;
    private final Attempt attempt;

    public Question(int number, String topic, String subTopic, String question, String optionA, String optionB, String optionC, String optionD, String optionE, int correctAnswer) {
        this(number, format(BUNDLE.getString("unknown")), topic, subTopic, question, optionA, optionB, optionC, optionD, optionE, correctAnswer, -1, new Attempt(0, 0));
    }

    public Question(int number, String staffName, String topic, String subTopic, String question, String optionA, String optionB, String optionC, String optionD, String optionE, int correctAnswer, int givenAnswer, Attempt attempt) {
        this.number = number;
        this.staffName = staffName;
        this.topic = topic;
        this.subTopic = subTopic;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.optionE = optionE;
        this.correctAnswer = correctAnswer;
        this.givenAnswer = givenAnswer;
        this.attempt = attempt;
    }

    public static Question from(Question other) {
        return new Question(other.number, other.staffName, other.topic, other.subTopic, other.question, other.optionA, other.optionB, other.optionC, other.optionD, other.optionE, other.correctAnswer, other.givenAnswer, other.attempt);
    }

    public Question from(String staffName, int givenAnswer) {
        return new Question(
                number,
                staffName,
                topic,
                subTopic,
                question,
                optionA,
                optionB,
                optionC,
                optionD,
                optionE,
                correctAnswer,
                givenAnswer,
                attempt
        );
    }

    public Question from(Attempt attempt) {
        return new Question(
                number,
                staffName,
                topic,
                subTopic,
                question,
                optionA,
                optionB,
                optionC,
                optionD,
                optionE,
                correctAnswer,
                givenAnswer,
                attempt
        );
    }

    public int getNumber() {
        return number;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getTopic() {
        return topic;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getOptionE() {
        return optionE;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public int getGivenAnswer() {
        return givenAnswer;
    }

    public Attempt getAttempt() {
        return attempt;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.number;
        hash = 29 * hash + Objects.hashCode(this.staffName);
        hash = 29 * hash + Objects.hashCode(this.topic);
        hash = 29 * hash + Objects.hashCode(this.subTopic);
        hash = 29 * hash + Objects.hashCode(this.question);
        hash = 29 * hash + Objects.hashCode(this.optionA);
        hash = 29 * hash + Objects.hashCode(this.optionB);
        hash = 29 * hash + Objects.hashCode(this.optionC);
        hash = 29 * hash + Objects.hashCode(this.optionD);
        hash = 29 * hash + Objects.hashCode(this.optionE);
        hash = 29 * hash + this.correctAnswer;
        hash = 29 * hash + this.givenAnswer;
        hash = 29 * hash + Objects.hashCode(this.attempt);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Question other = (Question) obj;
        if (this.number != other.number) {
            return false;
        }
        if (this.correctAnswer != other.correctAnswer) {
            return false;
        }
        if (this.givenAnswer != other.givenAnswer) {
            return false;
        }
        if (!Objects.equals(this.staffName, other.staffName)) {
            return false;
        }
        if (!Objects.equals(this.topic, other.topic)) {
            return false;
        }
        if (!Objects.equals(this.subTopic, other.subTopic)) {
            return false;
        }
        if (!Objects.equals(this.question, other.question)) {
            return false;
        }
        if (!Objects.equals(this.optionA, other.optionA)) {
            return false;
        }
        if (!Objects.equals(this.optionB, other.optionB)) {
            return false;
        }
        if (!Objects.equals(this.optionC, other.optionC)) {
            return false;
        }
        if (!Objects.equals(this.optionD, other.optionD)) {
            return false;
        }
        if (!Objects.equals(this.optionE, other.optionE)) {
            return false;
        }
        return Objects.equals(this.attempt, other.attempt);
    }

    @Override
    public String toString() {
        return "Question{" + "number=" + number + ", staffName=" + staffName + ", topic=" + topic + ", subTopic=" + subTopic + ", question=" + question + ", optionA=" + optionA + ", optionB=" + optionB + ", optionC=" + optionC + ", optionD=" + optionD + ", optionE=" + optionE + ", correctAnswer=" + correctAnswer + ", givenAnswer=" + givenAnswer + ", attempt=" + attempt + '}';
    }

}
