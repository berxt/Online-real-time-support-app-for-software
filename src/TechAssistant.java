import java.util.List;

public class TechAssistant {
    private String id;
    private String name;
    private List<String> expertise;
    private boolean available = true;

    public TechAssistant(String id, String name, List<String> expertise) {
        this.id = id;
        this.name = name;
        this.expertise = expertise;
    }

    public boolean canAssist(String software) {
        return expertise.contains(software);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<String> getExpertise() {
        return expertise;
    }
}
