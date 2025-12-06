import java.util.ArrayList;

public class ProjectTester {
    public static void main(String[] args) {
        
        // =================================================================
        // 1. RUN-TIME POLYMORPHISM (Dynamic Method Dispatch)
        // =================================================================
        System.out.println("--- 1. Testing Run-time Polymorphism (MRF Processing) ---");

        // Create a collection of the base type (WasteItem)
        ArrayList<WasteItem> collectedWaste = new ArrayList<>();
        
        // Add objects of different derived types to the same collection
        collectedWaste.add(new BiodegradableWaste("Leftover food scraps", 5.5));
        collectedWaste.add(new RecyclableWaste("PET Soda Bottles", 1.2, "PET (#1)"));
        collectedWaste.add(new ResidualWaste("Dirty diapers and ceramics", 3.0));
        collectedWaste.add(new BiodegradableWaste("Dried leaves and branches", 8.0));


        // Iterate through the collection and call the polymorphic method
        for (WasteItem item : collectedWaste) {
            // The actual method executed is determined at run-time based 
            // on the actual object type (Book, DVD, etc.), not the reference type (LibraryItem).
            item.processAtMRF(); 
        }
        
        System.out.println("\n======================================================\n");
        
        // =================================================================
        // 2. COMPILE-TIME POLYMORPHISM (Method Overloading)
        // =================================================================
        System.out.println("--- 2. Testing Compile-time Polymorphism (Personnel Tasks) ---");
        
        MRFPersonnel sorter = new MRFPersonnel("Ate Mila");

        // Call Overload 1 (No arguments)
        sorter.sortWaste(); 
        
        // Call Overload 2 (String argument)
        sorter.sortWaste("clear glass bottles"); 
        
        // Call Overload 3 (Double argument)
        double recordedWeight = sorter.sortWaste(45.5);
        System.out.println("   * Final Inventory Update: " + recordedWeight + " kg added.");
    }
}