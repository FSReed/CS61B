public class WordsIntoInteger {
    private static int charToInt(String s, int position) {
        int tmp = s.charAt(position);
        if (tmp < 'a' || tmp > 'z') {

        }
        return tmp - 'a' + 1;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Please give exactly 1 input string.");
        }
        String s = args[0];
        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            result = result * 27;
            result += charToInt(s, i);
        }
        System.out.println("Result = " + result);
    }
}