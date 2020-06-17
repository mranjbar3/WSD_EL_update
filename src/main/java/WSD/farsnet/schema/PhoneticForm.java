package WSD.farsnet.schema;

public class PhoneticForm {
    private int id;
    private String value;

    public PhoneticForm() {
    }

    public PhoneticForm(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return this.id;
    }

    public String getValue() {
        return this.value;
    }
}

