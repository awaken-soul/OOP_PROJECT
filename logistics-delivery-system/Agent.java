public class Agent {
    private int id;
    private String name;

    public Agent(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public void displayInfo() {
        System.out.println("Agent ID: " + id + ", Name: " + name);
    }

    // 👇 Add this
    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }
}
