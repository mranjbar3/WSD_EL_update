package WSD.farsnet.schema;

import WSD.farsnet.service.SenseService;
import java.util.List;

public class Word {
    private int id;
    private String pos;
    private String defaultPhonetic;
    private String defaultValue;

    public Word() {
    }

    public Word(int id, String pos, String defaultPhonetic, String defaultValue) {
        this.id = id;
        this.defaultPhonetic = defaultPhonetic;
        this.defaultValue = defaultValue;
        this.pos = pos;
    }

    public int getId() {
        return this.id;
    }

    public String getPos() {
        return this.pos;
    }

    public String getDefaultPhonetic() {
        return this.defaultPhonetic;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public List<WrittenForm> getWrittenForms() {
        return SenseService.getWrittenFormsByWord(this.id);
    }

    public List<PhoneticForm> getPhoneticForms() {
        return SenseService.getPhoneticFormsByWord(this.id);
    }
}
