package WSD.farsnet.schema;

import WSD.farsnet.service.SynsetService;

public class SynsetRelation {
    private int id;
    private String type;
    private String synsetWords1;
    private String synsetWords2;
    private int synsetId1;
    private int synsetId2;
    private String reverseType;

    public SynsetRelation(int id, String type, String synsetWords1, String synsetWords2, int synsetId1, int synsetId2, String reverseType) {
        this.id = id;
        this.type = type;
        this.synsetWords1 = synsetWords1;
        this.synsetWords2 = synsetWords2;
        this.synsetId1 = synsetId1;
        this.synsetId2 = synsetId2;
        this.reverseType = reverseType;
    }

    public SynsetRelation() {
    }

    public int getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getSynsetWords1() {
        return this.synsetWords1;
    }

    public String getSynsetWords2() {
        return this.synsetWords2;
    }

    public int getSynsetId1() {
        return this.synsetId1;
    }

    public int getSynsetId2() {
        return this.synsetId2;
    }

    public String getReverseType() {
        return this.reverseType;
    }

    public Synset getSynset1() {
        return SynsetService.getSynsetById(this.synsetId1);
    }

    public Synset getSynset2() {
        return SynsetService.getSynsetById(this.synsetId2);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSynsetWords1(String synsetWords1) {
        this.synsetWords1 = synsetWords1;
    }

    public void setSynsetWords2(String synsetWords2) {
        this.synsetWords2 = synsetWords2;
    }

    public void setSynsetId1(int synsetId1) {
        this.synsetId1 = synsetId1;
    }

    public void setSynsetId2(int synsetId2) {
        this.synsetId2 = synsetId2;
    }

    public void setReverseType(String reverseType) {
        this.reverseType = reverseType;
    }
}

