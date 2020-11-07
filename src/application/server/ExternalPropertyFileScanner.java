package application.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Polivakha
 * @created 11/6/2020
 */

@Component
public class ExternalPropertyFileScanner {

    private final String PROPERTIES_FILE_NAME = "server.properties";
    private final String SOURCE_FOLDER_NAME = "out";

    @Autowired
    private PropertiesAssignResolver propertiesAssignResolver;

    public void scanPropertiesFile() throws URISyntaxException, FileNotFoundException {
        File currentSourceCodeFile = new File(Server.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
        );

        if (currentSourceCodeFile.getPath().contains(SOURCE_FOLDER_NAME)) {
            while (!currentSourceCodeFile.getName().equals(SOURCE_FOLDER_NAME)) {
                currentSourceCodeFile = currentSourceCodeFile.getParentFile();
            }

            currentSourceCodeFile = currentSourceCodeFile.getParentFile();
            String pathOfPropertiesFile = currentSourceCodeFile.getPath() + "\\" + PROPERTIES_FILE_NAME;
            
            try {
                setPropertiesFromFile(pathOfPropertiesFile);
            } catch (IOException e) {
                setDefaultProperties();
                printWarningInConsole();
            }
            
        } else {
            throw new FileNotFoundException("Folder 'src' was not found. If you have renamed it - please, return " +
                    "old folder name");
        }
    }

    private void printWarningInConsole() {
    }

    private void setDefaultProperties() {
    }

    private void setPropertiesFromFile(String pathOfPropertiesFile) throws IOException {
        try {
            FileChannel fileChannel = FileChannel.open(Path.of(pathOfPropertiesFile));
            ByteBuffer byteBuffer = ByteBuffer.allocate(256);
            fileChannel.read(byteBuffer);
            final String properties = new String(byteBuffer.array()).trim();

            final String[] propertiesArray = properties.split("=");
            final Map<String, String> propertiesMap = buildPropertiesMap(propertiesArray);

            propertiesAssignResolver.setPropertiesMap(propertiesMap);
            propertiesAssignResolver.assignProperties();

        } catch (IOException exception) {
            throw new IOException("Unable to open PropertiesFile");
        }
    }

    private Map<String, String> buildPropertiesMap(String[] propertiesArray) {
        Map<String, String> propertiesMap = new HashMap<>();
        for (int keyIndex = 0; keyIndex < propertiesArray.length; keyIndex += 2) {
            propertiesMap.put(propertiesArray[keyIndex], propertiesArray[keyIndex + 1]);
        }
        return propertiesMap;
    }
}
