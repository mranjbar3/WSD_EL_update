package WSD.farsnet.service;

import WSD.farsnet.database.SqlLiteDbUtility;
import WSD.farsnet.schema.PhoneticForm;
import WSD.farsnet.schema.Sense;
import WSD.farsnet.schema.SenseRelation;
import WSD.farsnet.schema.SenseRelationType;
import WSD.farsnet.schema.WrittenForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SenseService {
    public SenseService() {
    }

    public static List<Sense> getSensesByWord(String searchStyle, String searchKeyword) {
        List<Sense> results = new ArrayList();
        String sql = "SELECT sense.id, seqId, vtansivity, vactivity, vtype, synset, vpastStem, vpresentStem, category, goupOrMokassar, esmeZamir, adad, adverb_type_1, adverb_type_2, adj_pishin_vijegi, adj_type, noe_khas, nounType, adj_type_sademorakkab, vIssababi, vIsIdiom, vGozaraType, kootah_nevesht, mohavere, word.id as wordId, word.defaultValue, word.avaInfo, word.pos FROM sense INNER JOIN word ON sense.word = word.id WHERE sense.id IN (SELECT sense.id FROM word INNER JOIN sense ON sense.word = word.id LEFT OUTER JOIN value ON value.word = word.id WHERE word.search_value @SearchStyle '@SearchValue' OR value.search_value @SearchStyle '@SearchValue') OR sense.id IN (SELECT sense.id FROM sense INNER JOIN sense_relation ON sense.id = sense_relation.sense INNER JOIN sense AS sense_2 ON sense_2.id = sense_relation.sense2 INNER JOIN word ON sense_2.word = word.id WHERE sense_relation.type =  'Refer-to' AND word.search_value LIKE  '@SearchValue') OR sense.id IN (SELECT sense_2.id FROM sense INNER JOIN sense_relation ON sense.id = sense_relation.sense INNER JOIN sense AS sense_2 ON sense_2.id = sense_relation.sense2 INNER JOIN word ON sense.word = word.id WHERE sense_relation.type =  'Refer-to' AND word.search_value LIKE  '@SearchValue') ";
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
                results.add(new Sense(rs.getInt("id"), rs.getString("seqId"), rs.getString("pos"), rs.getString("defaultValue"), rs.getInt("wordId"), rs.getString("avaInfo"), getVtansivity(rs.getString("vtansivity")), getVactivity(rs.getString("vactivity")), getVtype(rs.getString("vtype")), getNormalValue(rs.getString("synset")), getNormalValue(rs.getString("vpastStem")), getNormalValue(rs.getString("vpresentStem")), getCategory(rs.getString("category")), getGoupOrMokassar(rs.getString("goupOrMokassar")), getEsmeZamir(rs.getString("esmeZamir")), getAdad(rs.getString("adad")), getAdverbType1(rs.getString("adverb_type_1")), getAdverbType2(rs.getString("adverb_type_2")), getAdjPishinVijegi(rs.getString("adj_pishin_vijegi")), getAdjType(rs.getString("adj_type")), getNoeKhas(rs.getString("noe_khas")), getNounType(rs.getString("nounType")), getAdjTypeSademorakkab(rs.getString("adj_type_sademorakkab")), rs.getBoolean("vIssababi"), rs.getBoolean("vIsIdiom"), getVGozaraType(rs.getString("vGozaraType")), rs.getBoolean("kootah_nevesht"), rs.getBoolean("mohavere")));
            }
        } catch (SQLException var7) {
            var7.printStackTrace();
        }

        return results;
    }

    public static List<Sense> getSensesBySynset(int synsetId) {
        List<Sense> results = new ArrayList();
        String sql = "SELECT sense.id, seqId, vtansivity, vactivity, vtype, synset, vpastStem, vpresentStem, category, goupOrMokassar, esmeZamir, adad, adverb_type_1, adverb_type_2, adj_pishin_vijegi, adj_type, noe_khas, nounType, adj_type_sademorakkab, vIssababi, vIsIdiom, vGozaraType, kootah_nevesht, mohavere, word.id as wordId, word.defaultValue, word.avaInfo, word.pos FROM sense INNER JOIN word ON sense.word = word.id WHERE sense.synset = " + synsetId;

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                results.add(new Sense(rs.getInt("id"), rs.getString("seqId"), rs.getString("pos"), rs.getString("defaultValue"), rs.getInt("wordId"), rs.getString("avaInfo"), getVtansivity(rs.getString("vtansivity")), getVactivity(rs.getString("vactivity")), getVtype(rs.getString("vtype")), getNormalValue(rs.getString("synset")), getNormalValue(rs.getString("vpastStem")), getNormalValue(rs.getString("vpresentStem")), getCategory(rs.getString("category")), getGoupOrMokassar(rs.getString("goupOrMokassar")), getEsmeZamir(rs.getString("esmeZamir")), getAdad(rs.getString("adad")), getAdverbType1(rs.getString("adverb_type_1")), getAdverbType2(rs.getString("adverb_type_2")), getAdjPishinVijegi(rs.getString("adj_pishin_vijegi")), getAdjType(rs.getString("adj_type")), getNoeKhas(rs.getString("noe_khas")), getNounType(rs.getString("nounType")), getAdjTypeSademorakkab(rs.getString("adj_type_sademorakkab")), rs.getBoolean("vIssababi"), rs.getBoolean("vIsIdiom"), getVGozaraType(rs.getString("vGozaraType")), rs.getBoolean("kootah_nevesht"), rs.getBoolean("mohavere")));
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }

        return results;
    }

    public static Sense getSenseById(int senseId) {
        Sense result = null;
        String sql = "SELECT sense.id, seqId, vtansivity, vactivity, vtype, synset, vpastStem, vpresentStem, category, goupOrMokassar, esmeZamir, adad, adverb_type_1, adverb_type_2, adj_pishin_vijegi, adj_type, noe_khas, nounType, adj_type_sademorakkab, vIssababi, vIsIdiom, vGozaraType, kootah_nevesht, mohavere, word.id as wordId, word.defaultValue, word.avaInfo, word.pos FROM sense INNER JOIN word ON sense.word = word.id WHERE sense.id = " + senseId;

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            for(ResultSet rs = stmt.executeQuery(); rs.next(); result = new Sense(rs.getInt("id"), rs.getString("seqId"), rs.getString("pos"), rs.getString("defaultValue"), rs.getInt("wordId"), rs.getString("avaInfo"), getVtansivity(rs.getString("vtansivity")), getVactivity(rs.getString("vactivity")), getVtype(rs.getString("vtype")), getNormalValue(rs.getString("synset")), getNormalValue(rs.getString("vpastStem")), getNormalValue(rs.getString("vpresentStem")), getCategory(rs.getString("category")), getGoupOrMokassar(rs.getString("goupOrMokassar")), getEsmeZamir(rs.getString("esmeZamir")), getAdad(rs.getString("adad")), getAdverbType1(rs.getString("adverb_type_1")), getAdverbType2(rs.getString("adverb_type_2")), getAdjPishinVijegi(rs.getString("adj_pishin_vijegi")), getAdjType(rs.getString("adj_type")), getNoeKhas(rs.getString("noe_khas")), getNounType(rs.getString("nounType")), getAdjTypeSademorakkab(rs.getString("adj_type_sademorakkab")), rs.getBoolean("vIssababi"), rs.getBoolean("vIsIdiom"), getVGozaraType(rs.getString("vGozaraType")), rs.getBoolean("kootah_nevesht"), rs.getBoolean("mohavere"))) {
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }

        return result;
    }

    public static List<Sense> getAllSenses() {
        List<Sense> results = new ArrayList();
        String sql = "SELECT sense.id, seqId, vtansivity, vactivity, vtype, synset, vpastStem, vpresentStem, category, goupOrMokassar, esmeZamir, adad, adverb_type_1, adverb_type_2, adj_pishin_vijegi, adj_type, noe_khas, nounType, adj_type_sademorakkab, vIssababi, vIsIdiom, vGozaraType, kootah_nevesht, mohavere, word.id as wordId, word.defaultValue, word.avaInfo, word.pos FROM sense INNER JOIN word ON sense.word = word.id";

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                results.add(new Sense(rs.getInt("id"), rs.getString("seqId"), rs.getString("pos"), rs.getString("defaultValue"), rs.getInt("wordId"), rs.getString("avaInfo"), getVtansivity(rs.getString("vtansivity")), getVactivity(rs.getString("vactivity")), getVtype(rs.getString("vtype")), getNormalValue(rs.getString("synset")), getNormalValue(rs.getString("vpastStem")), getNormalValue(rs.getString("vpresentStem")), getCategory(rs.getString("category")), getGoupOrMokassar(rs.getString("goupOrMokassar")), getEsmeZamir(rs.getString("esmeZamir")), getAdad(rs.getString("adad")), getAdverbType1(rs.getString("adverb_type_1")), getAdverbType2(rs.getString("adverb_type_2")), getAdjPishinVijegi(rs.getString("adj_pishin_vijegi")), getAdjType(rs.getString("adj_type")), getNoeKhas(rs.getString("noe_khas")), getNounType(rs.getString("nounType")), getAdjTypeSademorakkab(rs.getString("adj_type_sademorakkab")), rs.getBoolean("vIssababi"), rs.getBoolean("vIsIdiom"), getVGozaraType(rs.getString("vGozaraType")), rs.getBoolean("kootah_nevesht"), rs.getBoolean("mohavere")));
            }
        } catch (SQLException var5) {
            var5.printStackTrace();
        }

        return results;
    }

    public static List<SenseRelation> getSenseRelationsById(int senseId) {
        List<SenseRelation> results = new ArrayList();
        String sql = "SELECT id, type, sense, sense2, senseWord1, senseWord2 FROM sense_relation WHERE sense = " + senseId + " OR sense2 = " + senseId;

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                results.add(new SenseRelation(rs.getInt("id"), rs.getInt("sense"), rs.getInt("sense2"), rs.getString("senseWord1"), rs.getString("senseWord2"), rs.getString("type")));
            }
        } catch (SQLException var11) {
            var11.printStackTrace();
        }

        List<SenseRelation> resultsArr = new ArrayList();

        for(int i = 0; i < results.size(); ++i) {
            SenseRelation temp = (SenseRelation)results.get(i);
            if (temp.getSenseId1() != senseId) {
                String type = temp.getType();
                int senseId2 = temp.getSenseId2();
                int senseId1 = temp.getSenseId1();
                String senseWord2 = temp.getSenseWord2();
                String senseWord1 = temp.getSenseWord1();
                temp.setType(ReverseSRelationType(type));
                temp.setSenseId1(senseId2);
                temp.setSenseId2(senseId1);
                temp.setSenseWord1(senseWord2);
                temp.setSenseWord2(senseWord1);
            }

            resultsArr.add(temp);
        }

        return resultsArr;
    }

    public static List<SenseRelation> getSenseRelationsByType(int senseId, SenseRelationType[] types) {
        List<SenseRelation> results = new ArrayList();
        String _types = "";
        String _revTypes = "";
        SenseRelationType[] var8 = types;
        int var7 = types.length;

        for(int var6 = 0; var6 < var7; ++var6) {
            SenseRelationType type = var8[var6];
            _types = _types + "'" + RelationValue(type) + "',";
            _revTypes = _revTypes + "'" + RelationValue(ReverseRelationType(type)) + "',";
        }

        _types = _types + "'not_type'";
        _revTypes = _revTypes + "'not_type'";
        String sql = "SELECT id, type, sense, sense2, senseWord1, senseWord2 FROM sense_relation WHERE (sense = " + senseId + " AND type in (" + _types + ")) OR (sense2 = " + senseId + " AND type in (" + _revTypes + "))" + " ORDER BY sense";

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                results.add(new SenseRelation(rs.getInt("id"), rs.getInt("sense"), rs.getInt("sense2"), rs.getString("senseWord1"), rs.getString("senseWord2"), rs.getString("type")));
            }
        } catch (SQLException var14) {
            var14.printStackTrace();
        }

        List<SenseRelation> resultsArr = new ArrayList();

        for(int i = 0; i < results.size(); ++i) {
            SenseRelation temp = (SenseRelation)results.get(i);
            if (temp.getSenseId1() != senseId) {
                String type = temp.getType();
                int senseId2 = temp.getSenseId2();
                int senseId1 = temp.getSenseId1();
                String senseWord2 = temp.getSenseWord2();
                String senseWord1 = temp.getSenseWord1();
                temp.setType(ReverseSRelationType(type));
                temp.setSenseId1(senseId2);
                temp.setSenseId2(senseId1);
                temp.setSenseWord1(senseWord2);
                temp.setSenseWord2(senseWord1);
            }

            resultsArr.add(temp);
        }

        return resultsArr;
    }

    public static List<PhoneticForm> getPhoneticFormsByWord(int wordId) {
        List<PhoneticForm> results = new ArrayList();
        String sql = "SELECT id, value FROM speech WHERE word = " + wordId;

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                results.add(new PhoneticForm(rs.getInt("id"), rs.getString("value")));
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }

        return results;
    }

    public static List<WrittenForm> getWrittenFormsByWord(int wordId) {
        List<WrittenForm> results = new ArrayList();
        String sql = "SELECT id, value FROM value WHERE word = " + wordId;

        try {
            Connection conn = SqlLiteDbUtility.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                results.add(new WrittenForm(rs.getInt("id"), rs.getString("value")));
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

    private static String getVtansivity(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing")) {
            switch(value.hashCode()) {
                case -1724158427:
                    if (value.equals("transitive")) {
                        return "Transitive";
                    }
                    break;
                case 841936170:
                    if (value.equals("inTransitive")) {
                        return "Intransitive";
                    }
                    break;
                case 1845861045:
                    if (value.equals("dovajhi")) {
                        return "Causative/Anticausative";
                    }
            }

            return value;
        } else {
            return "";
        }
    }

    private static String getVactivity(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing")) {
            switch(value.hashCode()) {
                case -1422950650:
                    if (value.equals("active")) {
                        return "Active";
                    }
                    break;
                case -792039641:
                    if (value.equals("passive")) {
                        return "Passive";
                    }
            }

            return value;
        } else {
            return "";
        }
    }

    private static String getVtype(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing")) {
            switch(value.hashCode()) {
                case -1431524879:
                    if (value.equals("simpleVerb")) {
                        return "Simple";
                    }
                    break;
                case -1054772775:
                    if (value.equals("pishvandiVerb")) {
                        return "Phrasal";
                    }
                    break;
                case -570810619:
                    if (value.equals("auxiliaryVerb")) {
                        return "Auxiliary";
                    }
                    break;
                case 530219749:
                    if (value.equals("copulaVerb")) {
                        return "Copula";
                    }
                    break;
                case 1665965418:
                    if (value.equals("compoundVerb")) {
                        return "Complex";
                    }
            }

            return value;
        } else {
            return "";
        }
    }

    private static String getCategory(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing")) {
            switch(value.hashCode()) {
                case -49458414:
                    if (value.equals("category_masdari")) {
                        return "Infinitival";
                    }
                    break;
                case 318357937:
                    if (value.equals("category_esmZamir")) {
                        return "Pronoun";
                    }
                    break;
                case 338298407:
                    if (value.equals("category_adad")) {
                        return "Numeral";
                    }
                    break;
                case 338599184:
                    if (value.equals("category_khAs")) {
                        return "Specific";
                    }
                    break;
                case 1537779501:
                    if (value.equals("category_Am")) {
                        return "General";
                    }
            }

            return value;
        } else {
            return "";
        }
    }

    private static String getGoupOrMokassar(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing")) {
            switch(value.hashCode()) {
                case -826418989:
                    if (value.equals("am_khas_esmejam")) {
                        return "MassNoun";
                    }
                    break;
                case 134174425:
                    if (value.equals("am_khas_jam")) {
                        return "Regular";
                    }
                    break;
                case 1892681254:
                    if (value.equals("am_khas_mokassar")) {
                        return "Irregular";
                    }
            }

            return value;
        } else {
            return "";
        }
    }

    private static String getEsmeZamir(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing")) {
            switch(value.hashCode()) {
                case -969140441:
                    if (value.equals("gheir_moshakhas")) {
                        return "Indefinite";
                    }
                    break;
                case 534797624:
                    if (value.equals("motaghabel")) {
                        return "Reciprocal";
                    }
                    break;
                case 714990811:
                    if (value.equals("noun_type_morakab")) {
                        return "";
                    }
                    break;
                case 1224364290:
                    if (value.equals("moakkad")) {
                        return "Emphatic";
                    }
            }

            return value;
        } else {
            return "";
        }
    }

    private static String getAdad(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing")) {
            switch(value.hashCode()) {
                case -1537886559:
                    if (value.equals("tartibi")) {
                        return "Ordinal";
                    }
                    break;
                case 3003695:
                    if (value.equals("asli")) {
                        return "Cardinal";
                    }
            }

            return value;
        } else {
            return "";
        }
    }

    private static String getAdverbType1(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing")) {
            switch(value.hashCode()) {
                case -1609563487:
                    if (value.equals("moshtagh_morakab")) {
                        return "DerivationalCompound";
                    }
                    break;
                case -221942702:
                    if (value.equals("morakkab")) {
                        return "Compound";
                    }
                    break;
                case -186590203:
                    if (value.equals("moshtagh")) {
                        return "Derivative";
                    }
                    break;
                case 109191060:
                    if (value.equals("saade")) {
                        return "Simple";
                    }
            }

            return value;
        } else {
            return "";
        }
    }

    private static String getNormalValue(String value) {
        return value != null && !value.equals("") && !value.equals("Nothing") ? value : "";
    }

    private static String getAdverbType2(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing")) {
            String res = " ";
            switch(value.charAt(0)) {
                case '0':
                    res = res;
                    break;
                case '1':
                    res = res + "AdjectiveModifying,";
                    break;
                default:
                    res = res + value.charAt(0) + ",";
            }

            switch(value.charAt(1)) {
                case '0':
                    res = res;
                    break;
                case '1':
                    res = res + "AdverbModifying,";
                    break;
                default:
                    res = res + value.charAt(1) + ",";
            }

            switch(value.charAt(2)) {
                case '0':
                    res = res;
                    break;
                case '1':
                    res = res + "VerbModifying,";
                    break;
                default:
                    res = res + value.charAt(2) + ",";
            }

            switch(value.charAt(3)) {
                case '0':
                    res = res;
                    break;
                case '1':
                    res = res + "SentenceModifying,";
                    break;
                default:
                    res = res + value.charAt(3) + ",";
            }

            return res.substring(0, res.length() - 1);
        } else {
            return "";
        }
    }

    private static String getAdjPishinVijegi(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing") && !value.equals("No")) {
            switch(value.hashCode()) {
                case -282395544:
                    if (value.equals("Yes_taajobi")) {
                        return "Exclamatory";
                    }
                    break;
                case 770492981:
                    if (value.equals("Yes_Nothing")) {
                        return "Simple";
                    }
                    break;
                case 1795033874:
                    if (value.equals("Yes_eshare")) {
                        return "Demonstrative";
                    }
                    break;
                case 2020200460:
                    if (value.equals("Yes_mobham")) {
                        return "Indefinite";
                    }
            }

            return value;
        } else {
            return "";
        }
    }

    private static String getAdjType(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing") && value != "No") {
            switch(value.hashCode()) {
                case -1735880873:
                    if (value.equals("bartarin")) {
                        return "Superlative";
                    }
                    break;
                case -1396218190:
                    if (value.equals("bartar")) {
                        return "Comparative";
                    }
                    break;
                case 1241931560:
                    if (value.equals("motlagh")) {
                        return "Absolute";
                    }
            }

            return value;
        } else {
            return "";
        }
    }

    private static String getNoeKhas(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing") && value != "No") {
            switch(value.hashCode()) {
                case -533828350:
                    if (value.equals("noe_khas_ensan")) {
                        return "Human";
                    }
                    break;
                case -526835153:
                    if (value.equals("noe_khas_makan")) {
                        return "Place";
                    }
                    break;
                case -514827458:
                    if (value.equals("noe_khas_zaman")) {
                        return "Time";
                    }
                    break;
                case 708964732:
                    if (value.equals("noe_khas_heyvan")) {
                        return "Animal";
                    }
            }

            return value;
        } else {
            return "";
        }
    }

    private static String getAdjTypeSademorakkab(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing") && value != "No") {
            switch(value.hashCode()) {
                case -1398986386:
                    if (value.equals("adj_type_morakab")) {
                        return "Compound";
                    }
                    break;
                case -383542830:
                    if (value.equals("adj_type_moshtagh")) {
                        return "Derivative";
                    }
                    break;
                case -264504089:
                    if (value.equals("adj_type_saade")) {
                        return "Simple";
                    }
                    break;
                case 1930601326:
                    if (value.equals("adj_type_moshtagh_morakab")) {
                        return "DerivatinalCompound";
                    }
            }

            return value;
        } else {
            return "";
        }
    }

    private static String getVGozaraType(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing")) {
            String res = " ";
            switch(value.charAt(0)) {
                case '0':
                    res = res;
                    break;
                case '1':
                    res = res + "WithComplement,";
                    break;
                default:
                    res = res + value.charAt(0) + ",";
            }

            switch(value.charAt(1)) {
                case '0':
                    res = res;
                    break;
                case '1':
                    res = res + "WithObject,";
                    break;
                default:
                    res = res + value.charAt(1) + ",";
            }

            switch(value.charAt(2)) {
                case '0':
                    res = res;
                    break;
                case '1':
                    res = res + "WithPredicate,";
                    break;
                default:
                    res = res + value.charAt(2) + ",";
            }

            return res.substring(0, res.length() - 1);
        } else {
            return "";
        }
    }

    private static String getNounType(String value) {
        if (value != null && !value.equals("") && !value.equals("Nothing") && value != "No") {
            switch(value.hashCode()) {
                case -1881033151:
                    if (value.equals("noun_type_ebarat")) {
                        return "Phrasal";
                    }
                    break;
                case -601968748:
                    if (value.equals("noun_type_saade")) {
                        return "Simple";
                    }
                    break;
                case 714990811:
                    if (value.equals("noun_type_morakab")) {
                        return "Compound";
                    }
                    break;
                case 725240837:
                    if (value.equals("noun_type_moshtagh")) {
                        return "Derivative";
                    }
                    break;
                case 1576628897:
                    if (value.equals("noun_type_moshtagh_morakab")) {
                        return "DerivatinalCompound";
                    }
            }

            return value;
        } else {
            return "";
        }
    }

    private static String RelationValue(SenseRelationType type) {
        return type.toString() == "Derivationally_related_form" ? "Derivationally related form" : type.toString().replace("_", "-");
    }

    private static SenseRelationType ReverseRelationType(SenseRelationType type) {
        if (SenseRelationType.Refer_to == type) {
            return SenseRelationType.Is_Referred_by;
        } else if (SenseRelationType.Is_Referred_by == type) {
            return SenseRelationType.Refer_to;
        } else if (SenseRelationType.Verbal_Part == type) {
            return SenseRelationType.Is_Verbal_Part_of;
        } else if (SenseRelationType.Is_Verbal_Part_of == type) {
            return SenseRelationType.Verbal_Part;
        } else if (SenseRelationType.Is_Non_Verbal_Part_of == type) {
            return SenseRelationType.Non_Verbal_Part;
        } else {
            return SenseRelationType.Non_Verbal_Part == type ? SenseRelationType.Is_Non_Verbal_Part_of : type;
        }
    }

    private static String ReverseSRelationType(String type) {
        if (type.equals("Refer-to")) {
            return "Is-Referred-by";
        } else if (type.equals("Is-Referred-by")) {
            return "Refer-to";
        } else if (type.equals("Verbal-Part")) {
            return "Is-Verbal-Part-of";
        } else if (type.equals("Is-Verbal-Part-of")) {
            return "Verbal-Part";
        } else if (type.equals("Non-Verbal-Part")) {
            return "Is-Non-Verbal-Part-of";
        } else {
            return type.equals("Is-Non-Verbal-Part-of") ? "Non-Verbal-Part" : type;
        }
    }
}
