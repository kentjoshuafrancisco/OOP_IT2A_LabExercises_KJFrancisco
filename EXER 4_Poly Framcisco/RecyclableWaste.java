public class RecyclableWaste extends WasteItem {
    private String type; // e.g., PET, HDPE, Cardboard

    public RecyclableWaste(String desc, double weight, String type) {
        super(desc, weight);
        this.type = type;
    }

    // * METHOD OVERRIDING: Specific implementation for Recyclable *
    @Override
    public void processAtMRF() {
        System.out.println("-> [Sorting Section] Processing Recyclable Waste: " 
                            + getDescription() + " (" + getWeight() + " kg).");
        System.out.println("   * Result: Segregated by type (" + type + ") and baled for selling.");
    }
}