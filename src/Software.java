public class Software {
    private String id;
    private String name;
    private String version;

    public Software(String id, String name, String version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }

    public Software(String name) {
        this.id = "0000";
        this.name = name;
        this.version = "v0.0.0";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return name + " (Version: " + version + ")";
    }
}
