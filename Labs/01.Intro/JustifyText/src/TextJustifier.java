import java.util.Arrays;

public class TextJustifier {
    public static String[] justifyText(String[] words, int maxWidth) {
        int wordIndex = 0;
        int startIndex = 0;

        StringBuilder formatedText = new StringBuilder();

        if (words.length == 0) {
            return new String[0];
        }

        while (wordIndex < words.length) {
            int wordsCount = 0;
            int currentWidth = 0;

            while (wordIndex < words.length && (currentWidth + words[wordIndex].length() + wordsCount <= maxWidth)) {
                currentWidth += words[wordIndex].length();
                wordsCount++;
                wordIndex++;
            }

            int freePositionsCount = maxWidth - currentWidth;
            int neededSpaces = wordsCount - 1;

            if (neededSpaces > 0 && wordIndex < words.length) {
                int minimumSpacesPerPlace = freePositionsCount / neededSpaces;
                int extraSpaces = freePositionsCount % neededSpaces;

                for (int i = startIndex; i < wordIndex - 1; i++) {
                    formatedText.append(words[i]);
                    formatedText.append(" ".repeat(minimumSpacesPerPlace));

                    if (extraSpaces > 0) {
                        formatedText.append(" ");
                        extraSpaces--;
                    }
                }
                formatedText.append(words[wordIndex - 1]);
            }
            else {
                for (int i = startIndex; i < wordIndex; i++) {
                    formatedText.append(words[i]);

                    if (i < wordIndex - 1) {
                        formatedText.append(" ");
                    }
                }

                formatedText.repeat(" ", freePositionsCount - neededSpaces);
            }

            formatedText.append("\n");
            startIndex = wordIndex;
        }

        return formatedText.toString().split("\n");
    }
}

class Main {
    public static void main(String[] args) {
        String[] test1 = {"One", "line"};
        int maxWidth = 10;
        String[] result1 = TextJustifier.justifyText(test1, maxWidth);
        System.out.println(Arrays.toString(result1));
    }
}