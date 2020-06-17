package WSD;

import WSD.farsnet.schema.Synset;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class WordSenseDisambiguatedMain {

    public String main1(String inputString) {

        FNetAPI fNetAPI = new FNetAPI();
        Disambiguator da = new Disambiguator();
        Preprocessor preprocessor = new Preprocessor();
//        String inputString = "شیر سلطان جنگل سرسبز است. من جنگل را دوست داشتن.";
        //String inputString = "من غذا خوردن سیر شدن";

        //pre-processing the input text
        inputString = preprocessor.normalizer(inputString);
        Vector<String> tokens = preprocessor.Tokenizer(inputString);
        try {
            Vector<String> inputPOS = preprocessor.POStag(inputString);
            Vector<List<String>> glosses = new Vector<>();
            Vector<List<String>> elements = new Vector<>();
            Vector<List<Double>> scores = new Vector<>();
            Vector<Integer> maxScoreIndex = new Vector<>();
            Vector<List<String>> examples = new Vector<>();
            Vector<List<String>> pos = new Vector<>();
            Vector<List<String>> semCat = new Vector<>();
            Vector<List<Integer>> ids = new Vector<>();
            Vector<List<Synset>> synsets = new Vector<>();
            for (int i = 0; i < tokens.size(); i++) {
                System.out.println("Word:" + tokens.get(i) + "_POS:" + inputPOS.get(i));
                synsets.add(fNetAPI.getSynsets(tokens.get(i)));
                ids.add(fNetAPI.getSynsetIds(synsets.get(i)));
                glosses.add(fNetAPI.getGlosses(synsets.get(i)));
                examples.add(fNetAPI.getExamples(synsets.get(i)));
                pos.add(fNetAPI.getPOS(synsets.get(i)));
                semCat.add(fNetAPI.getSemCategory(synsets.get(i)));
            }
            da.checkPOS(synsets, inputPOS, pos, ids, glosses, semCat, examples);
            for (int i = 0; i < tokens.size(); i++) {
                Vector<Double> score = new Vector<>();
                double max = 0.0;
                maxScoreIndex.add(0);
                for (int j = 0; j < synsets.get(i).size(); j++) {

                    //fNetAPI.printSynsetElement(synsets.get(i).get(j));
                    elements.add(fNetAPI.getSynsetElement(synsets.get(i).get(j)));
                    Integer score1 = da.scoreBasedOnSynsets(elements.lastElement(), inputString);
                    Double score2 = da.cosineSimm(tokens, examples.get(i).get(j), glosses.get(i).get(j));
                    Integer score3 = da.scoreBasedOnSynsets(fNetAPI.getSynsetsRelations(synsets.get(i).get(j)), inputString);
                    //System.out.print(score1+score2+score3+":"+j);
                    score.add(score1 + score2 + score3);
                    if (max < score1 + score2 + score3) {

                        maxScoreIndex.remove(maxScoreIndex.size() - 1);
                        maxScoreIndex.add(j);
                        max = score1 + score2 + score3;
                        //System.out.println(j);

                    }
                    System.out.println("Id:" + ids.get(i).get(j) + "_" + pos.get(i).get(j) + "_" + semCat.get(i).get(j) + "_gloss:" + glosses.get(i).get(j));
                    System.out.println("Score:" + score1 + "_" + score2 + "_" + score3);
                    System.out.println("Examples:" + examples.get(i).get(j));

                }
                scores.add(score);
            }

            StringBuilder out = new StringBuilder("{");
            for (int i = 0; i < tokens.size(); i++) {

                Integer index = maxScoreIndex.get(i);
                if (glosses.get(i).size() != 0 && scores.get(i).size() != 0) {
                    out.append('"').append(tokens.get(i)).append('"').append(":").append('"').append(glosses.get(i).get(index)).append('"');
                    System.out.println("Word:" + tokens.get(i) + "_Score:" + scores.get(i).get(index) + "_selected gloss:" + glosses.get(i).get(index));
                } else {
                    out.append('"').append(tokens.get(i)).append('"').append(":").append("یافت نشد").append('"');
                    System.out.println("Word:" + tokens.get(i) + "_Score:None_selected gloss:None");
                }
                out.append(",");

            }
            out.append("}");
            out = new StringBuilder(out.toString().replace(",}", "}"));
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String main2(String inputString) {

        FNetAPI fnet = new FNetAPI();
        Disambiguator da = new Disambiguator();
        Preprocessor preprocessor = new Preprocessor();
        //String inputString = "شیر سلطان جنگل سرسبز است";
        //String inputString = "من غذا خوردن سیر شدن";
        //pre-processing the input text
        inputString = preprocessor.normalizer(inputString);
        Vector<String> tokens = preprocessor.Tokenizer(inputString);
        try {
            Vector<String> inputPOS = preprocessor.POStag(inputString);
            Vector<List<String>> glosses = new Vector<>();
            Vector<Vector<String>> elements = new Vector<>();
            Vector<Vector<Double>> scores = new Vector<>();
            Vector<Integer> maxScoreIndex = new Vector<>();
            Vector<List<String>> examples = new Vector<>();
            Vector<List<String>> pos = new Vector<>();
            Vector<List<String>> semCat = new Vector<>();
            Vector<List<Integer>> ids = new Vector<>();
            Vector<List<Synset>> synsets = new Vector<>();
            for (int i = 0; i < tokens.size(); i++) {
                synsets.add(fnet.getSynsets(tokens.get(i)));
                ids.add(fnet.getSynsetIds(synsets.get(i)));
                glosses.add(fnet.getGlosses(synsets.get(i)));
                examples.add(fnet.getExamples(synsets.get(i)));
                pos.add(fnet.getPOS(synsets.get(i)));
                semCat.add(fnet.getSemCategory(synsets.get(i)));
            }
            da.checkPOS(synsets, inputPOS, pos, ids, glosses, semCat, examples);
            for (int i = 0; i < tokens.size(); i++) {
                Vector<Double> score = new Vector<>();
                double max = 0.0;
                maxScoreIndex.add(0);
                for (int j = 0; j < synsets.get(i).size(); j++) {
                    elements.add(fnet.getSynsetElement(synsets.get(i).get(j)));
                    Integer score1 = da.scoreBasedOnSynsets(elements.lastElement(), inputString);
                    Double score2 = da.cosineSimm(tokens, examples.get(i).get(j), glosses.get(i).get(j));
                    Integer score3 = da.scoreBasedOnSynsets(fnet.getSynsetsRelations(synsets.get(i).get(j)), inputString);
                    score.add(score1 + score2 + score3);
                    if (max < score1 + score2 + score3) {
                        maxScoreIndex.remove(maxScoreIndex.size() - 1);
                        maxScoreIndex.add(j);
                        max = score1 + score2 + score3;
                    }
                }
                scores.add(score);
            }

            StringBuilder out = new StringBuilder();
            for (int i = 0; i < tokens.size(); i++) {
                Integer index = maxScoreIndex.get(i);
                if (glosses.get(i).size() != 0 && +scores.get(i).size() != 0) {
                    out.append(ids.get(i).get(index)).append(",");
                } else {
                    out.append("0,");
                }
            }
            out.append("}");
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
