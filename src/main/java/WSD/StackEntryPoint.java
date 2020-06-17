package WSD;

import edu.stanford.nlp.ling.TaggedWord;
import ir.ac.iust.nlp.jhazm.POSTagger;
import py4j.GatewayServer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StackEntryPoint {

    private FNetAPI fnet;

    public StackEntryPoint() {
        fnet = new FNetAPI();
    }

    public FNetAPI getFnet() {
        return fnet;
    }

    public List<TaggedWord> getPosTagger(String[] str) {
        POSTagger posTagger;
        try {
            posTagger = new POSTagger();
            return posTagger.batchTag(Arrays.asList(str));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new StackEntryPoint());
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }
}