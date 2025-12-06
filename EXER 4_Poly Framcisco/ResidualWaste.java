public class ResidualWaste extends WasteItem {
    public ResidualWaste(String desc, double weight) {
        super(desc, weight);
    }

    // * METHOD OVERRIDING: Specific implementation for Residual *
    @Override
    public void processAtMRF() {
        System.out.println("-> [Hauling Area] Processing Residual Waste: " 
                            + getDescription() + " (" + getWeight() + " kg).");
        System.out.println("   * Result: Stored temporarily and scheduled for hauling to the Sanitary Landfill.");
    }
}