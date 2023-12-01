import java.util.Comparator;

public class Dog implements OurComparable{
    public int size;
    public String name;

    public Dog(int s, String n) {
        size = s;
        name = n;
    }

    public int compareTo(Object obj) {
        Dog randomDog = (Dog) obj;
        return this.size - randomDog.size;
    }

    public void bark() {
        System.out.print(name + " " + "Says: ");
        System.out.println("Bark!");
    }
    private static class SizeComparator implements Comparator<Dog> {
        public int compare(Dog a, Dog b) {
            return a.size - b.size;
        }
    }

    public static Comparator<Dog> getSizeComparator() {
        return new SizeComparator();
    }
}
