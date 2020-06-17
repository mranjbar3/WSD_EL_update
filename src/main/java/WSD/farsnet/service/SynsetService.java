package WSD.farsnet.service;

import WSD.farsnet.database.SqlLiteDbUtility;
import WSD.farsnet.schema.Synset;
import WSD.farsnet.schema.SynsetExample;
import WSD.farsnet.schema.SynsetGloss;
import WSD.farsnet.schema.SynsetRelation;
import WSD.farsnet.schema.SynsetRelationType;
import WSD.farsnet.schema.WordNetSynset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SynsetService {
    public SynsetService() {
    }

    public static List<Synset> getSynsetsByWord(String searchStyle, String searchKeyword) {
        List<Synset> results = new ArrayList();
        String sql = "SELECT id, pos, semanticCategory, example, gloss, nofather, noMapping FROM synset WHERE synset.id IN (SELECT synset.id as synset_id FROM word INNER JOIN sense ON sense.word = word.id INNER JOIN synset ON sense.synset = synset.id LEFT OUTER JOIN value ON value.word = word.id WHERE word.search_value @SearchStyle '@SearchValue' OR (value.search_value) @SearchStyle '@SearchValue')  OR synset.id IN (SELECT sense.synset AS synset_id FROM sense INNER JOIN sense_relation ON sense.id = sense_relation.sense INNER JOIN sense AS sense_2 ON sense_2.id = sense_relation.sense2 INNER JOIN word ON sense_2.word = word.id WHERE sense_relation.type =  'Refer-to' AND word.search_value LIKE  '@SearchValue') OR synset.id IN (SELECT sense_2.synset AS synset_id FROM sense INNER JOIN sense_relation ON sense.id = sense_relation.sense INNER JOIN sense AS sense_2 ON sense_2.id = sense_relation.sense2 INNER JOIN word ON sense.word = word.id WHERE sense_relation.type =  'Refer-to' AND word.search_value LIKE  '@SearchValue')";
        searchKeyword = SecureValue(NormalValue(searchKeyword));
        if (searchStyle.equals("LIKE") || searchStyle.equals("START") || searchStyle.equals("END")) {
            sql = sql.replace("@SearchStyle", "LIKE");
            if (searchStyle.equals("LIKE")) {
                searchKeyword = "%" + searchKeyword + "%";
            }

            if (searchStyle.equals("START")) {
                searchKeyword = searchKeyword + "%";
            }

            if (searchStyle.equals("END")) {
                searchKeyword = "%" + searchKeyword;
            }
        }

        if (searchStyle.equals("EXACT")) {
            sql = sql.replace("@SearchStyle", "=");
        }

        sql = sql.replace("@SearchValue", searchKeyword);

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                results.add(new Synset(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
            }
        } catch (SQLException var7) {
            var7.printStackTrace();
        }

        return results;
    }

    public static List<Synset> getAllSynsets() {
        List<Synset> results = new ArrayList();
        String sql = "SELECT id, pos, semanticCategory, example, gloss, nofather, noMapping FROM synset ";

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                results.add(new Synset(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
            }
        } catch (SQLException var5) {
            var5.printStackTrace();
        }

        return results;
    }

    public static Synset getSynsetById(int synsetId) {
        Synset result = null;

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            String sql = "SELECT id, pos, semanticCategory, example, gloss, nofather, noMapping FROM synset WHERE id=" + synsetId;
            PreparedStatement stmt = conn.prepareStatement(sql);

            for(ResultSet rs = stmt.executeQuery(); rs.next(); result = new Synset(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7))) {
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }

        return result;
    }

    public static List<SynsetRelation> getSynsetRelationsById(int synsetId) {
        List<SynsetRelation> results = new ArrayList();
        String sql = "SELECT id, type, synsetWords1, synsetWords2, synset, synset2, reverse_type FROM synset_relation WHERE synset=" + synsetId + " OR synset2=" + synsetId;

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                results.add(new SynsetRelation(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getString(7)));
            }
        } catch (SQLException var12) {
            var12.printStackTrace();
        }

        List<SynsetRelation> resultsArr = new ArrayList();

        for(int i = 0; i < results.size(); ++i) {
            SynsetRelation temp = (SynsetRelation)results.get(i);
            if (temp.getSynsetId1() != synsetId) {
                String type = temp.getType();
                int synsetId2 = temp.getSynsetId2();
                int synsetId1 = temp.getSynsetId1();
                String synsetWords2 = temp.getSynsetWords2();
                String synsetWords1 = temp.getSynsetWords1();
                String reverseType = temp.getReverseType();
                temp.setReverseType(type);
                temp.setSynsetId1(synsetId2);
                temp.setSynsetId2(synsetId1);
                temp.setSynsetWords1(synsetWords2);
                temp.setSynsetWords2(synsetWords1);
                temp.setType(reverseType);
            }

            resultsArr.add(temp);
        }

        return resultsArr;
    }

    public static List<SynsetRelation> getSynsetRelationsByType(int synsetId, SynsetRelationType[] types) {
        List<SynsetRelation> results = new ArrayList();
        String _types = "";
        String _revTypes = "";
        SynsetRelationType[] var8 = types;
        int var7 = types.length;

        for(int var6 = 0; var6 < var7; ++var6) {
            SynsetRelationType type = var8[var6];
            _types = _types + "'" + RelationValue(type) + "',";
            _revTypes = _revTypes + "'" + RelationValue(ReverseRelationType(type)) + "',";
        }

        _types = _types + "'not_type'";
        _revTypes = _revTypes + "'not_type'";
        String sql = "SELECT id, type, synsetWords1, synsetWords2, synset, synset2, reverse_type FROM synset_relation WHERE (synset = " + synsetId + " AND type in (" + _types + ")) OR (synset2 = " + synsetId + " AND type in (" + _revTypes + "))" + " ORDER BY synset";

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                results.add(new SynsetRelation(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getString(7)));
            }
        } catch (SQLException var15) {
            var15.printStackTrace();
        }

        List<SynsetRelation> resultsArr = new ArrayList();

        for(int i = 0; i < results.size(); ++i) {
            SynsetRelation temp = (SynsetRelation)results.get(i);
            if (temp.getSynsetId1() != synsetId) {
                String type = temp.getType();
                int synsetId2 = temp.getSynsetId2();
                int synsetId1 = temp.getSynsetId1();
                String synsetWords2 = temp.getSynsetWords2();
                String synsetWords1 = temp.getSynsetWords1();
                String reverseType = temp.getReverseType();
                temp.setReverseType(type);
                temp.setSynsetId1(synsetId2);
                temp.setSynsetId2(synsetId1);
                temp.setSynsetWords1(synsetWords2);
                temp.setSynsetWords2(synsetWords1);
                temp.setType(reverseType);
            }

            resultsArr.add(temp);
        }

        return results;
    }

    public static List<WordNetSynset> getWordNetSynsets(int synsetId) {
        List<WordNetSynset> results = new ArrayList();
        String sql = "SELECT id, wnPos, wnOffset, example, gloss, synset, type FROM wordnetsynset WHERE synset=" + synsetId;

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                results.add(new WordNetSynset(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getString(7)));
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }

        return results;
    }

    public static List<SynsetExample> getSynsetExamples(int synsetId) {
        List<SynsetExample> results = new ArrayList();
        String sql = "SELECT gloss_and_example.id, content, lexicon.title FROM gloss_and_example INNER JOIN lexicon ON gloss_and_example.lexicon=lexicon.id WHERE type='EXAMPLE' and synset=" + synsetId;

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                results.add(new SynsetExample(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }

        return results;
    }

    public static List<SynsetGloss> getSynsetGlosses(int synsetId) {
        List<SynsetGloss> results = new ArrayList();
        String sql = "SELECT gloss_and_example.id, content, lexicon.title FROM gloss_and_example INNER JOIN lexicon ON gloss_and_example.lexicon=lexicon.id WHERE type='GLOSS' and synset=" + synsetId;

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                results.add(new SynsetGloss(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }

        return results;
    }

    private static String NormalValue(String Value) {
        String NormalValue = Value.replace("ی", "ي");
        NormalValue = NormalValue.replace("ى", "ي");
        NormalValue = NormalValue.replace("ك", "ک");
        NormalValue = NormalValue.replace("'", "");
        NormalValue = NormalValue.replace("\"", "");
        NormalValue = NormalValue.replace(" ", "");
        NormalValue = NormalValue.replace("\u200c", "");
        NormalValue = NormalValue.replace("\u200c\u200cء", "");
        NormalValue = NormalValue.replace("\u200c\u200cٔ", "");
        NormalValue = NormalValue.replace("\u200c\u200cؤ", "و");
        NormalValue = NormalValue.replace("\u200c\u200cئ", "ي");
        NormalValue = NormalValue.replace("آ", "ا");
        NormalValue = NormalValue.replace("\u200c\u200cأ", "ا");
        NormalValue = NormalValue.replace("إ", "ا");
        NormalValue = NormalValue.replace("ۀ", "ه");
        NormalValue = NormalValue.replace("ة", "ه");
        NormalValue = NormalValue.replace("َ", "");
        NormalValue = NormalValue.replace("ُ", "");
        NormalValue = NormalValue.replace("ِ", "");
        NormalValue = NormalValue.replace("ً", "");
        NormalValue = NormalValue.replace("ٌ", "");
        NormalValue = NormalValue.replace("ٍ", "");
        NormalValue = NormalValue.replace("ّ", "");
        NormalValue = NormalValue.replace("ْ", "");
        NormalValue = NormalValue.replace("ِّ", "");
        NormalValue = NormalValue.replace("ٍّ", "");
        NormalValue = NormalValue.replace("َّ", "");
        NormalValue = NormalValue.replace("ًّ", "");
        NormalValue = NormalValue.replace("ُّ", "");
        NormalValue = NormalValue.replace("ٌّ", "");
        NormalValue = NormalValue.replace("u200D", "%");
        NormalValue = NormalValue.replace("ء", "");
        NormalValue = NormalValue.replace("أ", "ا");
        NormalValue = NormalValue.replace("ئ", "ي");
        return NormalValue;
    }

    private static String SecureValue(String Value) {
        if (Value == null) {
            return "";
        } else {
            Value = Value.replace("\u0000", "");
            Value = Value.replace("'", "");
            Value = Value.replace("\"", "");
            Value = Value.replace("\b", "");
            Value = Value.replace("\n", "");
            Value = Value.replace("\r", "");
            Value = Value.replace("\t", "");
            Value = Value.replace("\\", "");
            Value = Value.replace("/", "");
            Value = Value.replace("%", "");
            Value = Value.replace("_", "");
            Value = Value.replace("ـ", "");
            Value = Value.replace("!", "");
            Value = Value.replace(";", "");
            Value = Value.replace("?", "");
            Value = Value.replace("=", "");
            Value = Value.replace("<", "");
            Value = Value.replace(">", "");
            Value = Value.replace("&", "");
            Value = Value.replace("#", "");
            Value = Value.replace("@", "");
            Value = Value.replace("$", "");
            Value = Value.replace("^", "");
            Value = Value.replace("*", "");
            Value = Value.replace("+", "");
            return Value;
        }
    }

    private static String RelationValue(SynsetRelationType type) {
        if (type.toString() != "Related_to" && type.toString() != "Has-Unit" && type.toString().substring(3) != "Is_") {
            return type.toString() == "Has_Salient_defining_feature" ? "Has-Salient defining feature" : type.toString().replace("_", " ");
        } else {
            return type.toString().replace("_", "-");
        }
    }

    private static SynsetRelationType ReverseRelationType(SynsetRelationType type) {
        if (SynsetRelationType.Agent == type) {
            return SynsetRelationType.Is_Agent_of;
        } else if (SynsetRelationType.Is_Agent_of == type) {
            return SynsetRelationType.Agent;
        } else if (SynsetRelationType.Hypernym == type) {
            return SynsetRelationType.Hyponym;
        } else if (SynsetRelationType.Hyponym == type) {
            return SynsetRelationType.Hypernym;
        } else if (SynsetRelationType.Instance_hyponym == type) {
            return SynsetRelationType.Instance_hypernym;
        } else if (SynsetRelationType.Instance_hypernym == type) {
            return SynsetRelationType.Instance_hyponym;
        } else if (SynsetRelationType.Part_holonym == type) {
            return SynsetRelationType.Part_meronym;
        } else if (SynsetRelationType.Part_meronym == type) {
            return SynsetRelationType.Part_holonym;
        } else if (SynsetRelationType.Member_holonym == type) {
            return SynsetRelationType.Member_meronym;
        } else if (SynsetRelationType.Member_meronym == type) {
            return SynsetRelationType.Member_holonym;
        } else if (SynsetRelationType.Substance_holonym == type) {
            return SynsetRelationType.Substance_meronym;
        } else if (SynsetRelationType.Substance_meronym == type) {
            return SynsetRelationType.Substance_holonym;
        } else if (SynsetRelationType.Portion_holonym == type) {
            return SynsetRelationType.Portion_meronym;
        } else if (SynsetRelationType.Portion_meronym == type) {
            return SynsetRelationType.Portion_holonym;
        } else if (SynsetRelationType.Domain == type) {
            return SynsetRelationType.Is_Domain_of;
        } else if (SynsetRelationType.Is_Domain_of == type) {
            return SynsetRelationType.Domain;
        } else if (SynsetRelationType.Cause == type) {
            return SynsetRelationType.Is_Caused_by;
        } else if (SynsetRelationType.Is_Caused_by == type) {
            return SynsetRelationType.Cause;
        } else if (SynsetRelationType.Is_Instrument_of == type) {
            return SynsetRelationType.Instrument;
        } else if (SynsetRelationType.Instrument == type) {
            return SynsetRelationType.Is_Instrument_of;
        } else if (SynsetRelationType.Is_Entailed_by == type) {
            return SynsetRelationType.Entailment;
        } else if (SynsetRelationType.Entailment == type) {
            return SynsetRelationType.Is_Entailed_by;
        } else if (SynsetRelationType.Location == type) {
            return SynsetRelationType.Is_Location_of;
        } else if (SynsetRelationType.Is_Location_of == type) {
            return SynsetRelationType.Location;
        } else if (SynsetRelationType.Has_Salient_defining_feature == type) {
            return SynsetRelationType.Salient_defining_feature;
        } else if (SynsetRelationType.Salient_defining_feature == type) {
            return SynsetRelationType.Has_Salient_defining_feature;
        } else if (SynsetRelationType.Is_Attribute_of == type) {
            return SynsetRelationType.Attribute;
        } else if (SynsetRelationType.Attribute == type) {
            return SynsetRelationType.Is_Attribute_of;
        } else if (SynsetRelationType.Unit == type) {
            return SynsetRelationType.Has_Unit;
        } else if (SynsetRelationType.Has_Unit == type) {
            return SynsetRelationType.Unit;
        } else if (SynsetRelationType.Is_Patient_of == type) {
            return SynsetRelationType.Patient;
        } else {
            return SynsetRelationType.Patient == type ? SynsetRelationType.Is_Patient_of : type;
        }
    }
}
