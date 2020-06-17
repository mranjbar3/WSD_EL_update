package WSD.farsnet.schema;

import WSD.farsnet.service.SenseService;
import WSD.farsnet.service.SynsetService;

import java.util.List;

public class Synset {
    private int id;
    private String pos;
    private String semanticCategory;
    private String example;
    private String gloss;
    private String nofather;
    private String noMapping;

    public Synset() {
    }

    public Synset(int id, String pos, String semanticCategory, String example, String gloss, String nofather, String noMapping) {
        this.id = id;
        this.semanticCategory = semanticCategory;
        this.example = example;
        this.gloss = gloss;
        this.nofather = nofather;
        this.noMapping = noMapping;
        this.pos = pos;
    }

    public int getId() {
        return this.id;
    }

    public String getSemanticCategory() {
        return this.semanticCategory;
    }

    public String getNoMapping() {
        return this.noMapping;
    }

    public String getNofather() {
        return this.nofather;
    }

    public String getPos() {
        return this.pos;
    }

    public List<SynsetExample> getExamples() {
        return SynsetService.getSynsetExamples(this.id);
    }

    public List<SynsetGloss> getGlosses() {
        return SynsetService.getSynsetGlosses(this.id);
    }

    public List<Sense> getSenses() {
        return SenseService.getSensesBySynset(this.id);
    }

    public List<WordNetSynset> getWordNetSynsets() {
        return SynsetService.getWordNetSynsets(this.id);
    }

    public List<SynsetRelation> getSynsetRelations() {
        return SynsetService.getSynsetRelationsById(this.id);
    }

    public List<SynsetRelation> getSynsetRelations(SynsetRelationType relationType) {
        SynsetRelationType[] types = new SynsetRelationType[]{relationType};
        return SynsetService.getSynsetRelationsByType(this.id, types);
    }

    public List<SynsetRelation> getSynsetRelations(SynsetRelationType[] relationTypes) {
        return SynsetService.getSynsetRelationsByType(this.id, relationTypes);
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getGloss() {
        return gloss;
    }

    public void setGloss(String gloss) {
        this.gloss = gloss;
    }
}

