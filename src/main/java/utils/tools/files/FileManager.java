package utils.tools.files;

import java.io.File;

public class FileManager {

    public enum ResultFolder {
        REPORT_FOLDER("reports");

        private String folderName;

        ResultFolder(String folderName) {
            this.folderName = folderName;
        }

        @Override
        public String toString() {
            return this.folderName;
        }

        public File getLocalDir() {
            return getDirectory(this);
        }

    }

    private static File getDirectory(ResultFolder folder) {
        File directory = new File(folder.toString());
        if (!directory.exists() && !directory.mkdirs()) {
            System.out.println("Unable to create destination folder : " + folder.toString());
            return null;
        }
        return directory;
    }
}
