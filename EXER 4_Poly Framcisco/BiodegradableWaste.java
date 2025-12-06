public class BiodegradableWaste extends WasteItem {
    public BiodegradableWaste(String desc, double weight) {
        super(desc, weight);
    }

    // * METHOD OVERRIDING: Specific implementation for Biodegradable *
    @Override
    public void processAtMRF() {
        System.out.println("-> [Composting Station] Processing Biodegradable Waste: " 
                            + getDescription() + " (" + getWeight() + " kg).");
        System.out.println("   * Result: Directed to the Composting area to create organic fertilizer.");
    }
}