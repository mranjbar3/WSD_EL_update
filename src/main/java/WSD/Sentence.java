package WSD;

import java.util.Vector;

public class Sentence {

    private String content;
    private Vector<String> meaning;

    public Sentence() {
    }

    public String getContent() {
        System.out.println(content);
        if (content!=null)
            content = new WordSenseDisambiguatedMain().main1(content);
        return content;
    }

    public Vector<String> getMeaning() {
        content =  content+' ';
        content = content.replace(" ","<br>")+"--";
        return meaning;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public void setMeaning(Vector<String> meaning) {
        this.meaning = meaning;
    }
}
