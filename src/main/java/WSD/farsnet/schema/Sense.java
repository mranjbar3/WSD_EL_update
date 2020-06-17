package WSD.farsnet.schema;

import WSD.farsnet.service.SenseService;
import WSD.farsnet.service.SynsetService;
import java.util.List;

public class Sense {
    int id;
    String seqId;
    String value;
    Word word;
    String verbTransitivity;
    String verbActivePassive;
    String verbType;
    String synset;
    String verbPastStem;
    String verbPresentStem;
    String nounCategory;
    String nounPluralType;
    String pronoun;
    String nounNumeralType;
    String adverbType1;
    String adverbType2;
    String preNounAdjectiveType;
    String adjectiveType2;
    String nounSpecifityType;
    String nounType;
    String adjectiveType1;
    Boolean isCausative;
    Boolean isIdiomatic;
    String transitiveType;
    Boolean isAbbreviation;
    Boolean isColloquial;

    public Sense() {
    }

    public Sense(int id, String seqId, String pos, String defaultValue, int wordId, String defaultPhonetic, String verbTransitivity, String verbActivePassive, String verbType, String synset, String verbPastStem, String verbPresentStem, String nounCategory, String nounPluralType, String pronoun, String nounNumeralType, String adverbType1, String adverbType2, String preNounAdjectiveType, String adjectiveType2, String nounSpecifityType, String nounType, String adjectiveType1, Boolean isCausative, Boolean isIdiomatic, String transitiveType, Boolean isAbbreviation, Boolean isColloquial) {
        this.id = id;
        this.isColloquial = isColloquial;
        this.isAbbreviation = isAbbreviation;
        this.transitiveType = transitiveType;
        this.isIdiomatic = isIdiomatic;
        this.isCausative = isCausative;
        this.adjectiveType1 = adjectiveType1;
        this.nounType = nounType;
        this.nounSpecifityType = nounSpecifityType;
        this.adjectiveType2 = adjectiveType2;
        this.preNounAdjectiveType = preNounAdjectiveType;
        this.adverbType1 = adverbType1;
        this.adverbType2 = adverbType2;
        this.nounNumeralType = nounNumeralType;
        this.pronoun = pronoun;
        this.nounPluralType = nounPluralType;
        this.nounCategory = nounCategory;
        this.verbPastStem = verbPastStem;
        this.verbPresentStem = verbPresentStem;
        this.synset = synset;
        this.verbType = verbType;
        this.verbActivePassive = verbActivePassive;
        this.verbTransitivity = verbTransitivity;
        this.id = id;
        this.seqId = seqId;
        this.value = defaultValue;
        this.word = new Word(wordId, pos, defaultPhonetic, defaultValue);
    }

    public int getId() {
        return this.id;
    }

    public String getSeqId() {
        return this.seqId;
    }

    public String getValue() {
        return this.value;
    }

    public String getVerbActivePassive() {
        return this.verbActivePassive;
    }

    public String getVerbTransitivity() {
        return this.verbTransitivity;
    }

    public String getVerbType() {
        return this.verbType;
    }

    public String getVerbPresentStem() {
        return this.verbPresentStem;
    }

    public String getVerbPastStem() {
        return this.verbPastStem;
    }

    public String getNounCategory() {
        return this.nounCategory;
    }

    public String getNounPluralType() {
        return this.nounPluralType;
    }

    public String getPronoun() {
        return this.pronoun;
    }

    public String getNounNumeralType() {
        return this.nounNumeralType;
    }

    public String getAdverbType1() {
        return this.adverbType1;
    }

    public String getAdverbType2() {
        return this.adverbType2;
    }

    public String getPreNounAdjectiveType() {
        return this.preNounAdjectiveType;
    }

    public String getAdjectiveType2() {
        return this.adjectiveType2;
    }

    public String getNounSpecifityType() {
        return this.nounSpecifityType;
    }

    public String getNounType() {
        return this.nounType;
    }

    public String getAdjectiveType1() {
        return this.adjectiveType1;
    }

    public Boolean getIsCausative() {
        return this.isCausative;
    }

    public Boolean getIsIdiomatic() {
        return this.isIdiomatic;
    }

    public String getTransitiveType() {
        return this.transitiveType;
    }

    public Boolean getIsAbbreviation() {
        return this.isAbbreviation;
    }

    public Boolean getIsColloquial() {
        return this.isColloquial;
    }

    public Word getWord() {
        return this.word;
    }

    public Synset getSynset() {
        return this.synset != null && !this.synset.equals("") ? SynsetService.getSynsetById(Integer.parseInt(this.synset)) : null;
    }

    public List<SenseRelation> getSenseRelations() {
        return SenseService.getSenseRelationsById(this.id);
    }

    public List<SenseRelation> getSenseRelations(SenseRelationType relationType) {
        SenseRelationType[] types = new SenseRelationType[]{relationType};
        return SenseService.getSenseRelationsByType(this.id, types);
    }

    public List<SenseRelation> getSenseRelations(SenseRelationType[] relationTypes) {
        return SenseService.getSenseRelationsByType(this.id, relationTypes);
    }
}
