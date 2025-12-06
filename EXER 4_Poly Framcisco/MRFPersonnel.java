public class MRFPersonnel {
    private String name;

    public MRFPersonnel(String name) {
        this.name = name;
    }

    // * METHOD OVERLOADING 1: Standard sorting instruction *
    public void sortWaste() {
        System.out.println("\n[Personnel Action] " + name + " is performing general waste segregation at the main intake area.");
    }

    // * METHOD OVERLOADING 2: Sorting instruction with specific material type *
    public void sortWaste(String materialType) {
        System.out.println("[Personnel Action] " + name + " is focusing on sorting and cleaning the " + materialType + " materials.");
    }

    // * METHOD OVERLOADING 3: Inventory instruction with weight *
    public double sortWaste(double weightBaled) {
        System.out.println("[Personnel Action] " + name + " finished baling a batch. Recording " + weightBaled + " kg for inventory.");
        return weightBaled;
    }
}