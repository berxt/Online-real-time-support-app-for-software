import java.util.ArrayList;
import java.util.List;

public class HelpDeskSystem {
    private List<TechAssistant> techAssistants = new ArrayList<>();  // Store all available assistants

    public void registerTechAssistant(TechAssistant assistant) {
        techAssistants.add(assistant);
    }

    public List<TechAssistant> getTechAssistants() {
        return  techAssistants;
    }
    public List<TechAssistant> getAvailableAssistants(Software userSoftware) {
        System.out.println(techAssistants);


        List<TechAssistant> availableAssistants = new ArrayList<>();
        System.out.println(availableAssistants);
        for (TechAssistant assistant : techAssistants) {
            System.out.println("Assistant expertise: " + String.join(", ", assistant.getExpertise()));
            System.out.println("Checking if assistant can assist with: " + userSoftware.getName());
            if (assistant.canAssist(userSoftware.getName()) && assistant.isAvailable()) {
                availableAssistants.add(assistant);
            }
        }
        return availableAssistants;
    }
}
