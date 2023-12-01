import java.util.Comparator;

public class DogLauncher {
    public static void main(String[] args) {
        Dog ceobe = new Dog(31, "Ceobe");
        Dog karty = new Dog(42, "Karty");
        Dog bante = new Dog(53, "Bante");
        Dog[] dogList = new Dog[]{ceobe, karty, bante};
        Dog biggestDog = (Dog) Maximizer.max(dogList);
        System.out.println(biggestDog.size);

        // Dog.SizeComparator scmp = new Dog.SizeComparator();
        Comparator<Dog> scmp = Dog.getSizeComparator();
        if (scmp.compare(ceobe, karty) < 0) {
            ceobe.bark();
        } else {
            karty.bark();
        }
    }
}