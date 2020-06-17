package WSD;

import WSD.farsnet.schema.Sense;
import WSD.farsnet.schema.Synset;
import WSD.farsnet.schema.SynsetRelation;
import WSD.farsnet.service.SynsetService;

import java.util.List;
import java.util.Vector;

public class FNetAPI {

    public List<Synset> getSynsets(String word) {
        return SynsetService.getSynsetsByWord("EXACT", word);
    }

    public List<Integer> getSynsetIds(List<Synset> synsets) {
        Vector<Integer> ids = new Vector<>();
        for (Synset synset : synsets)
            ids.add(synset.getId());
        return ids;
    }

    public List<String> getGlosses(List<Synset> synsets) {
        Vector<String> glosses = new Vector<String>();
        for (Synset synset : synsets)
            glosses.add(synset.getGloss());
        return glosses;
    }

    public List<String> getExamples(List<Synset> synsets) {
        Vector<String> example = new Vector<String>();
        for (Synset synset : synsets)
            example.add(synset.getExample());
        return example;
    }

    public List<String> getPOS(List<Synset> synsets) {
        Vector<String> pos = new Vector<String>();
        for (Synset synset : synsets)
            pos.add(synset.getPos());
        return pos;
    }

    public List<String> getSemCategory(List<Synset> synsets) {
        Vector<String> sem = new Vector<String>();
        for (Synset synset : synsets)
            sem.add(synset.getSemanticCategory());
        return sem;
    }

    public Vector<String> getPOSfromID(Vector<Integer> fnIds) {
        Vector<String> sem = new Vector<String>();
        for (int fnId : fnIds) {
            String s = SynsetService.getSynsetById(fnId).getPos();
            sem.add(s);
        }
        return sem;

    }

    public void printSynsetElement(Synset fnSynset) {
        // System.out.print("Synset:{");
        for (int j = 0; j < fnSynset.getSenses().size(); j++) {
            Sense fnSense = fnSynset.getSenses().get(j);
            // System.out.print(fnSense.getWord().getValue().elementAt(0) + "_" + fnSense.getId() + " ,");
        }
        //   System.out.println("}");
    }

    public Vector<String> getSynsetElement(Synset fnSynset) {
        Vector<String> elements = new Vector<String>();
        for (int j = 0; j < fnSynset.getSenses().size(); j++) {
            Sense fnSense = fnSynset.getSenses().get(j);
            String s = fnSense.getWord().getDefaultValue().trim();
            elements.add(s);
            //System.out.print(s + "_" + fnSense.getId() + " ,");
        }
        return elements;
    }

    public static Vector<String> getSynsetElement_2(Synset synset) {
        Vector<String> elements = new Vector<String>();
        for (Sense sense : synset.getSenses())
            elements.add(sense.getValue().trim());
        return elements;
    }

    public Vector<String> getSynsetsRelations(Synset synset) {
        Vector<String> relatedWords = new Vector<String>();
        List<SynsetRelation> synsetRelations = synset.getSynsetRelations();
        for (SynsetRelation synsetRelation : synsetRelations)
            if (synsetRelation.getSynsetId1() == synset.getId())
                relatedWords.addAll(getSynsetElement_2(synsetRelation.getSynset2()));
        return relatedWords;
    }

    private static void FNAPIUse(String word) {
//instantiate a FNSynsetService
        SynsetService service = new SynsetService();
//find all synset containd the word
        List<Synset> fnSynsets = SynsetService.getSynsetsByWord("LIKE", word);
//print every synset
        for (Synset fnSynset : fnSynsets) {
            // System.out.print("Synset:{");
            for (int j = 0; j < fnSynset.getSenses().size(); j++) {
                Sense fnSense = fnSynset.getSenses().get(j);
                //System.out.print(fnSense.getWord().getValue().elementAt(0)+"_"+fnSense.getId()+" ,");
            }
            // System.out.println("}");
            //  System.out.println("Father[s]:");
            List<SynsetRelation> fathers = fnSynset.getSynsetRelations();
            for (SynsetRelation synsetRelation : fathers)
                if (synsetRelation.getSynset1().getId() == fnSynset.getId()
                        && synsetRelation.getReverseType().equalsIgnoreCase("Hypernym")) {
                    // System.out.print("Synset:{");
                    Synset father = synsetRelation.getSynset2();
                    for (int j = 0; j < father.getSenses().size(); j++) {
                        Sense fnSense = father.getSenses().get(j);
                        //System.out.print(fnSense.getWord().getValue().elementAt(0)+"_"+fnSense.getId()+" ,");
                    }
                    // System.out.println("}");
                }
            // .out.println("********************");
        }
    }
}