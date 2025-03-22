public class User {
    private String id;
    private String name;
    private String contact;  // New field for contact
    private Software software;  // Field for software

    public User(String id, String name, String contact, Software software) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.software = software;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
    public Software getSoftware() {
        return software;
    }

    @Override
    public String toString() {
        return "User: " + name + " | Contact: " + contact + " | Software: " + software.getName() + " (Version: " + software.getVersion() + ")";
    }
}
