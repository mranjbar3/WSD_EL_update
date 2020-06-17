package WSD;

import WSD.farsnet.schema.Synset;

import java.util.*;

public class Disambiguator {

    public Integer scoreBasedOnSynsets(List<String> elements, String tokens) {

        Integer score = 0;

        for (String element : elements) {
            if (tokens.contains(element)) {
                score++;
            }
        }
        return score;
    }

    public Double cosineSimm(Vector<String> tokens, String example, String def) {
        Vector<String> vector = new Vector<>();
        Collections.addAll(vector, example.split(" "));
        Collections.addAll(vector, def.split(" "));
        Map<CharSequence, Integer> occurrences1 = new HashMap<>();
        Map<CharSequence, Integer> occurrences2 = new HashMap<>();
        for (String word : tokens) {
            Integer oldCount = occurrences1.get(word);
            if (oldCount == null) {
                oldCount = 0;
            }
            occurrences1.put(word, oldCount + 1);
        }
        for (String word : vector) {
            Integer oldCount = occurrences1.get(word);
            if (oldCount == null) {
                oldCount = 0;
            }
            occurrences2.put(word, (oldCount + 1));
        }
        CosineSimilarity cs = new CosineSimilarity();
        return cs.cosineSimilarity(occurrences1, occurrences2);
    }

    public void checkPOS(Vector<List<Synset>> synsets, Vector<String> inputPOS, Vector<List<String>> pos, Vector<List<Integer>> ids,
                         Vector<List<String>> glosses, Vector<List<String>> semCat, Vector<List<String>> examples) {
        for (int i = 0; i < inputPOS.size(); i++) {
            String targetPos = inputPOS.get(i);
            for (int j = synsets.get(i).size() - 1; j > -1; j--) {
                if (!targetPos.equals(pos.get(i).get(j))) {
                    synsets.get(i).remove(j);
                    pos.get(i).remove(j);
                    ids.get(i).remove(j);
                    glosses.get(i).remove(j);
                    examples.get(i).remove(j);
                    semCat.get(i).remove(j);
                }
            }
        }
    }
}
