package company.policy.client.core;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class PolicyQuestionModel implements Serializable, Comparable<PolicyQuestionModel> {

    private static final long serialVersionUID = 1L;

    private final String staffName;
    private final int questionNumber;
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

    public PolicyQuestionModel() {
        this(0, "", "");
    }

    public PolicyQuestionModel(int policyQuestionNumber, String policyTopic, String policySubTopic) {
        this(policyQuestionNumber, policyTopic, policySubTopic, "", "", "", "", "", "", 0);
    }

    public PolicyQuestionModel(
            int questionNumber,
            String topic,
            String subTopic,
            String question,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String optionE,
            int correctAnswer) {
        this("Unknown", questionNumber, topic, subTopic, question, optionA, optionB, optionC, optionD, optionE, correctAnswer, -1, new Attempt(0, 0));
    }

    public PolicyQuestionModel(
            String staffName,
            int questionNumber,
            String topic,
            String subTopic,
            String question,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String optionE,
            int correctAnswer,
            int givenAnswer,
            Attempt attempt) {
        this.staffName = staffName;
        this.questionNumber = questionNumber;
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

    public String getStaffName() {
        return staffName;
    }

    public int getQuestionNumber() {
        return questionNumber;
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
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.staffName);
        hash = 71 * hash + this.questionNumber;
        hash = 71 * hash + Objects.hashCode(this.topic);
        hash = 71 * hash + Objects.hashCode(this.subTopic);
        hash = 71 * hash + Objects.hashCode(this.question);
        hash = 71 * hash + Objects.hashCode(this.optionA);
        hash = 71 * hash + Objects.hashCode(this.optionB);
        hash = 71 * hash + Objects.hashCode(this.optionC);
        hash = 71 * hash + Objects.hashCode(this.optionD);
        hash = 71 * hash + Objects.hashCode(this.optionE);
        hash = 71 * hash + this.correctAnswer;
        hash = 71 * hash + this.givenAnswer;
        hash = 71 * hash + Objects.hashCode(this.attempt);
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
        final PolicyQuestionModel other = (PolicyQuestionModel) obj;
        if (this.questionNumber != other.questionNumber) {
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
        return "PolicyQuestionModel{" + "staffName=" + staffName + ", questionNumber=" + questionNumber + ", topic=" + topic + ", subTopic=" + subTopic + ", question=" + question + ", optionA=" + optionA + ", optionB=" + optionB + ", optionC=" + optionC + ", optionD=" + optionD + ", optionE=" + optionE + ", correctAnswer=" + correctAnswer + ", givenAnswer=" + givenAnswer + ", attempt=" + attempt + '}';
    }

//    private int attemptedCount;
//
//    public int getAttemptedCount() {
//        return attemptedCount;
//    }
//
//    public void setAttemptedCount(int attemptedCount) {
//        this.attemptedCount = attemptedCount;
//    }
//
//    private int answeredCorrectCount;
//
//    public int getAnsweredCorrectCount() {
//        return answeredCorrectCount;
//    }
//
//    public void setAnsweredCorrectCount(int answeredCorrectCount) {
//        this.answeredCorrectCount = answeredCorrectCount;
//    }
    @Override
    public int compareTo(PolicyQuestionModel that) {
        return Comparator.comparing(PolicyQuestionModel::getQuestionNumber).compare(this, that);
    }

//    @Override
//    public String toString() {
//        return new AsString().apply(this);
//    }
//
//    public static PolicyQuestionModel valueOf(String val) {
//        return new AsModel().apply(val);
//    }
//
//    private static class AsString implements Function<PolicyQuestionModel, String> {
//
//        @Override
//        public String apply(PolicyQuestionModel model) {
//
//            String asString = List.of(model.getStaffName(),
//                    Integer.toString(model.getPolicyQuestionNumber()),
//                    model.getPolicyTopic(),
//                    model.getPolicySubTopic(),
//                    model.getPolicyQuestionText(),
//                    model.getAnswerOptionA(),
//                    model.getAnswerOptionB(),
//                    model.getAnswerOptionC(),
//                    model.getAnswerOptionD(),
//                    model.getAnswerOptionE(),
//                    Integer.toString(model.getCorrectAnswer()),
//                    Integer.toString(model.getGivenAnswer()))
//                    .stream()
//                    .collect(Collectors.joining(System.lineSeparator()));
//
//            System.out.printf("company.policy.client.core.PolicyQuestionModel.AsString.apply(); asString = %s\n", asString);
//
//            return asString;
//        }
//
//    }
//
//    private static class AsModel implements Function<String, PolicyQuestionModel> {
//
//        @Override
//        public PolicyQuestionModel apply(String s) {
//            PolicyQuestionModel model = new PolicyQuestionModel();
//            String[] fields = s.split(System.lineSeparator());
//
//            if (fields.length == 12) {
//                model.setStaffName(fields[0]);
//                model.setPolicyQuestionNumber(Integer.parseInt(fields[1]));
//                model.setPolicyTopic(fields[2]);
//                model.setPolicySubTopic(fields[3]);
//                model.setPolicyQuestionText(fields[4]);
//                model.setAnswerOptionA(fields[5]);
//                model.setAnswerOptionB(fields[6]);
//                model.setAnswerOptionC(fields[7]);
//                model.setAnswerOptionD(fields[8]);
//                model.setAnswerOptionE(fields[9]);
//                model.setCorrectAnswer(Integer.parseInt(fields[10]));
//                model.setGivenAnswer(Integer.parseInt(fields[11]));
//            }
//
//            return model;
//        }
//
//    }
}
