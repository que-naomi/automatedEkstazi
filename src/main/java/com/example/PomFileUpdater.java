import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PomFileUpdater {
    public static void main(String[] args) {
        String groupId = args[0];
        String artifactId = args[1];
        String version = args[2];

        updatePomXml(groupId, artifactId, version);
    }

    private static void updatePomXml(String groupId, String artifactId, String version) {
        Path pomFilePath = Paths.get("pom.xml");

        try {
            String pomContent = Files.readString(pomFilePath);
            String updatedPomContent = addBuildAndDependency(pomContent, groupId, artifactId, version);

            Files.write(pomFilePath, updatedPomContent.getBytes());
            System.out.println("Updated pom.xml successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String addBuildAndDependency(String pomContent, String groupId, String artifactId, String version) {
        String buildAndDependency = "<build>\n" +
                "  <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->\n" +
                "    <!-- ... existing plugins ... -->\n" +
                "  </pluginManagement>\n" +
                "</build>\n" +
                "<dependencies>\n" +
                "  <!-- ... existing dependencies ... -->\n" +
                "  <dependency>\n" +
                "    <groupId>" + groupId + "</groupId>\n" +
                "    <artifactId>" + artifactId + "</artifactId>\n" +
                "    <version>" + version + "</version>\n" +
                "  </dependency>\n" +
                "</dependencies>";

        int dependenciesStart = pomContent.indexOf("<dependencies>");
        int dependenciesEnd = pomContent.indexOf("</dependencies>") + "</dependencies>".length();

        StringBuilder updatedPomContent = new StringBuilder(pomContent);
        updatedPomContent.insert(dependenciesEnd, buildAndDependency);

        return updatedPomContent.toString();
    }
}
