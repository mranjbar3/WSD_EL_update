package WSD.farsnet.schema;

public class WrittenForm {
    private int id;
    private String value;

    public WrittenForm() {
    }

    public WrittenForm(int id, String value) {
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
