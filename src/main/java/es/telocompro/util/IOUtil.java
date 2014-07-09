package es.telocompro.util;

import java.io.File;

import es.telocompro.model.item.Item;

public class IOUtil {
	
	/**
     * Calculates a unique filename using id as differentiation
     * @param base Base to calculate the file name
     * @param id Unique id number to differentiate files
     */
	@Deprecated
    public static String calculateFileName(String base, int id) {
        String fileName = "";

        String codeValues = base.substring(0, 3);

        for (char c : codeValues.toCharArray()) {
            fileName += String.valueOf((int) c);
        }

        return String.valueOf( Integer.valueOf(fileName) + id);
    }
	
	public static String calculateFileName(Item item) {
		return new File("imgs/" + item.getUser().getUserId() + "/" + 
				item.getSubCategory().getCategory().getCategoryId() + "/" + 
				item.getSubCategory().getId()).getPath();
	}
	
	public static String calculateAvatarFilePath() {
		return new File("imgs/avatars/").getPath();
	}

}
