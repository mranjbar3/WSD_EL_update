package WSD;

import edu.stanford.nlp.ling.TaggedWord;
import ir.ac.iust.nlp.jhazm.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class Preprocessor {

    public Vector<String> POStag(String i) throws IOException {
        POSTagger tagger = new POSTagger();
        String[] input = Tokenizer(i).toArray(new String[0]);
        List<TaggedWord> actual = tagger.batchTag(Arrays.asList(input));
        Vector<String> pos = new Vector<>();
        for (TaggedWord x : actual)
            pos.add(x.tag().replace("N", "Noun").replace("AJ", "Adjective").replace("V", "Verb").replace("Noune", "Noun").replace("ADVerb", "Adverb").replace("PUNounC", "PUNC"));
        return pos;
    }

    public Vector<String> Tokenizer(String input) {
        Vector<String> tokenized = new Vector<>();
        List<String> actual;
        try {
            Normalizer normalizer1 = new Normalizer(true, false, false);
            Normalizer normalizer2 = new Normalizer(false, true, false);
            Normalizer normalizer3 = new Normalizer(false, false, true);
            WordTokenizer wordTokenizer = new WordTokenizer(false);
            input = normalizer1.run(normalizer2.run(normalizer3.run(input)));
            actual = wordTokenizer.tokenize(input);
            Lemmatizer lemmatizer = new Lemmatizer();
            for (String x : actual) {
                String s = lemmatizer.lemmatize(x);
                if (s.contains("#") && !s.split("#")[0].equals(""))
                    tokenized.add(s.split("#")[0] + "ن");
                else
                    tokenized.add(s.replace("#", ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokenized;
    }

    public String normalizer(String input) {
        return input;
    }
}
