public abstract class WasteItem {
    private String description;
    private double weightKg;
    
    public WasteItem(String desc, double weight) {
        this.description = desc;
        this.weightKg = weight;
    }
    
    public String getDescription() {
        return description;
    }
    public double getWeight() {
        return weightKg;
    }

    // * RUN-TIME POLYMORPHISM: Abstract Method to be Overridden *
    // This defines the common interface for "processing."
    public abstract void processAtMRF(); 
}