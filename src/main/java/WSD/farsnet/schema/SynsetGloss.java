package WSD.farsnet.schema;

public class SynsetGloss {
    private int id;
    private String content;
    private String lexicon;

    public SynsetGloss() {
    }

    public SynsetGloss(int id, String content, String lexicon) {
        this.id = id;
        this.content = content;
        this.lexicon = lexicon;
    }

    public int getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public String getLexicon() {
        return this.lexicon;
    }
}

