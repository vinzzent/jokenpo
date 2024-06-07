package server;

import java.util.Map;

public class QAService extends EntryService {
    private String question;
    private String[] validWords;
    private String exitWord;
    private Long milliseconds;
    private volatile String answer;
    private Map<String, String> mapOptions;
    private String extraWord;
    private String qAServiceType;

    protected QAService(UserAgent uA, String question, String[] validWords, String exitWord, Long milliseconds) {
        super(uA);
        setQuestion(question);
        setValidWords(validWords);
        setExitWord(exitWord);
        setMilliseconds(milliseconds);
        setQAServiceType("ValidWords");
    }

    protected QAService(UserAgent uA, String question, Map<String, String> mapOptions, String extraWord, String exitWord, Long milliseconds) {
        super(uA);
        setQuestion(question);
        setMapOptions(mapOptions);
        setExtraWord(extraWord);
        setExitWord(exitWord);
        setMilliseconds(milliseconds);
        setQAServiceType("MapOptions");
    }

    private String getQAServiceType() {
        return qAServiceType;
    }

    private void setQAServiceType(String qAServiceType) {
        this.qAServiceType = qAServiceType;
    }

    private String getExtraWord() {
        return extraWord;
    }

    private void setExtraWord(String extraWord) {
        this.extraWord = extraWord;
    }

    private Map<String, String> getMapOptions() {
        return mapOptions;
    }

    private void setMapOptions(Map<String, String> mapOptions) {
        this.mapOptions = mapOptions;
    }


    private void setQuestion(String question) {
        this.question = question;
    }

    private void setValidWords(String[] validWords) {
        this.validWords = validWords;
    }

    private void setExitWord(String exitWord) {
        this.exitWord = exitWord;
    }

    private void setMilliseconds(Long milliseconds) {
        this.milliseconds = milliseconds;
    }
    
    protected String getAnswer() {
        return answer;
    }

    private String getQuestion() {
        return question;
    }

    private String[] getValidWords() {
        return validWords;
    }

    private String getExitWord() {
        return exitWord;
    }

    private long getMilliseconds() {
        return milliseconds;
    }

    private void setAnswerUsingValidWords() {
        this.answer =  uA.qABucleForAWhile(getQuestion(), getValidWords(), getExitWord(), getMilliseconds());
    }
    
    private void setAnswerUsingMapOptions() {
        this.answer = uA.qABucleForAWhile(getQuestion(), getMapOptions(),getExtraWord(), getExitWord(), getMilliseconds());
    }

    @Override
    public void run() {
        if (getQAServiceType().equals("ValidWords")) {
            setAnswerUsingValidWords();
        } else {
            setAnswerUsingMapOptions();
        }
    }
}
