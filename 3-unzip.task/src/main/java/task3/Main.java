package task3;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {

    private static final String TEL_PATTERN = "[+]?\\d*\\s*[(]\\d*[)]\\s*\\d*[-|\\s*]\\d*[-|\\s*]\\d*";
    private static final List<String> telephones = new ArrayList<>();
    private static final List<String> emails = new ArrayList<>();

    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }

    public static void doSomething(String filename) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(filename))) {
            File unzippedFolder = new File("./unzipped");
            deleteDir(unzippedFolder);
            unzippedFolder.mkdirs();

            unzip(zin);

            telephones.clear();
            emails.clear();

            readData(unzippedFolder);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void readData(File folder) {
        File[] folderEntries = folder.listFiles();
        for (File entry : folderEntries)
        {
            if (entry.isDirectory())
            {
                readData(entry);
                continue;
            }

            try {
                FileReader exprFile = new FileReader(entry);
                BufferedReader reader = new BufferedReader(exprFile);

                while (reader.ready()) {
                    String line = reader.readLine();
                    if (line.contains("@")) {
                        String tel = Pattern.compile(TEL_PATTERN)
                                .matcher(line)
                                .results()
                                .map(MatchResult::group)
                                .toArray(String[]::new)[0];
                        String[] nums = Pattern.compile("/d")
                                .matcher(line)
                                .results()
                                .map(MatchResult::group)
                                .toArray(String[]::new);
//                        System.out.println(line + " : " + tel);
//                        StringBuilder sb = new StringBuilder("+" + nums[0] + " (" + nums[1] + nums[2] + nums[3] + ") ");
//                        for (int i = 4; i < nums.length; i++) {
//                            sb.append(nums[i]);
//                        }
//                        System.out.println(sb);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            }
        }
    }

    private static void unzip(ZipInputStream zin) throws IOException {
        ZipEntry entry;
        String name;

        while ((entry = zin.getNextEntry()) != null) {

            name = entry.getName();
            System.out.printf("File name: %s\n", name);

            // unpacking
            if (entry.isDirectory()) {
                new File("./unzipped/" + name).mkdir();
            } else {
                FileOutputStream fileOut = new FileOutputStream("./unzipped/" + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fileOut.write(c);
                }
                fileOut.flush();
                zin.closeEntry();
                fileOut.close();
            }
        }
    }

    public static void main(String[] args) {
        String filename = "3-unzip.task/input/contacts-0.zip";
        doSomething(filename);
    }

}
