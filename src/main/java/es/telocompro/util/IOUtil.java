package es.telocompro.util;

public class IOUtil {
	
	/**
     * Calculates a unique filename using id as differentiation
     * @param base Base to calculate the file name
     * @param id Unique id number to differentiate files
     */
    public static String calculateFileName(String base, int id) {
        String fileName = "";

        String codeValues = base.substring(0, 3);

        for (char c : codeValues.toCharArray()) {
            fileName += String.valueOf((int) c);
        }

        return String.valueOf( Integer.valueOf(fileName) + id);
    }

}
