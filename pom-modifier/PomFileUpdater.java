import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PomFileUpdater {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: PomFileUpdater <path> <groupId> <artifactId> <version>");
            System.exit(1);
        }

        String path = args[0];
        String groupId = args[1];
        String artifactId = args[2];
        String version = args[3];

        try (Stream<Path> pomFiles = Files.walk(Path.of(path))) {
            List<Path> pomFilePaths = pomFiles
                .filter(p -> p.toString().endsWith("pom.xml") && !p.toString().contains("src/"))
                .collect(Collectors.toList());

            for (Path pomFilePath : pomFilePaths) {
                updatePomFile(pomFilePath, groupId, artifactId, version);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updatePomFile(Path pomFilePath, String groupId, String artifactId, String version) {
        try {
            List<String> lines = Files.readAllLines(pomFilePath);
            List<String> updatedLines = lines.stream()
                .map(line -> {
                    if (line.contains("<dependencies>")) {
                        line = "      <dependency>\n"
                                + "        <groupId>" + groupId + "</groupId>\n"
                                + "        <artifactId>" + artifactId + "</artifactId>\n"
                                + "        <version>" + version + "</version>\n"
                                + "      </dependency>";
                    }
                    return line;
                })
                .collect(Collectors.toList());

            Files.write(pomFilePath, updatedLines);
            System.out.println("Updated: " + pomFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
